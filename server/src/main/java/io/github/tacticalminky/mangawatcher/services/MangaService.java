package io.github.tacticalminky.mangawatcher.services;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.mongodb.MongoWriteException;

import io.github.tacticalminky.mangawatcher.exceptions.*;
import io.github.tacticalminky.mangawatcher.db.models.*;
import io.github.tacticalminky.mangawatcher.db.repos.MangaRepo;

/**
 * The service for getting and interacting with manga and chapters
 *
 * @author Andrew Mink
 * @version Oct 1, 2023
 * @since 1.0.0-b.4
 */
@Service
public class MangaService {
    @Autowired
    private MangaRepo mangaRepo;

    @Autowired
    private SyncService syncService;

    /**
     * Gets a list of all manga
     *
     * @return a list of manga in a minimal form
     *
     * @see MangaRepo#findAllMinimalManga()
     */
    public List<MinimalManga> getAllAsMinimalManga() {
        return mangaRepo.findAllAsMinimalMangaByRegex(".");
    }

    /**
     * Gets a list of all manga
     *
     * @return a list of manga in their full form
     *
     * @see MangaRepo#findAllFullMangas()
     */
    public List<Manga> getAllAsFullManga() {
        return mangaRepo.findAll(Sort.by("title"));
        // return mangaRepo.findAllFullManga();
    }

    /**
     * Adds a new manga to the database
     *
     * @param newManga
     *  a minimal representation of the manga to be added (title and url)
     *
     * @return the created manga in its full form
     *
     * @throws MangaWriteException
     *  when the manga already exists
     */
    public Manga addManga(NewManga newManga) throws MangaWriteException {
        Manga manga = new Manga(newManga.getTitle(), newManga.getUrl());

        String slug = manga.getTitle().toLowerCase();
        slug = slug.replace(' ', '-');
        manga.setSlug(slug);

        try {
            getFullMangaBySlug(manga.getSlug());

            String message = "Manga already exists, can't create new";
            throw new MangaWriteException(manga.getSlug(), message);
        } catch (MangaNotFoundException ex) {
            Manga createdManga = mangaRepo.save(manga);

            syncService.syncManga(manga);
            return createdManga;
        }
    }

    /**
     * Finds the manga with the provided slug
     *
     * @param slug
     *  the manga's slug
     *
     * @return the base form of the manga (excludes the list of chapters)
     *
     * @throws MangaNotFoundException
     *  when the manga does not exist in the database
     *
     * @see MangaRepo#findBaseMangaBySlug()
     */
    public Manga getBaseMangaBySlug(String slug) throws MangaNotFoundException {
        return mangaRepo.findBaseMangaBySlug(slug)
            .orElseThrow(() -> new MangaNotFoundException(slug));
    }

    /**
     * Finds the manga with the provided slug
     *
     * @param slug
     *  the manga's slug
     *
     * @return the base form of the manga (includes the list of chapters)
     *
     * @throws MangaNotFoundException
     *  when the manga does not exist in the database
     *
     * @see MangaRepo#findFullMangaBySlug()
     */
    public Manga getFullMangaBySlug(String slug) throws MangaNotFoundException {
        return mangaRepo.findFullMangaBySlug(slug)
            .orElseThrow(() -> new MangaNotFoundException(slug));
    }

    /**
     * Updates the given manga
     *
     * @param manga
     *  the new version of the manga
     *
     * @return the updated manga
     *
     * @throws MangaNotFoundException
     *  when the manga does not exist in the database
     * @throws MangaWriteException
     *  when the manga update fails
     */
    public Manga updateManga(Manga manga) throws MangaNotFoundException, MangaWriteException {
        Manga oldManga = getFullMangaBySlug(manga.getSlug());

        if (!manga.getUrl().equals(oldManga.getUrl())) {
            String message = "Can't change the manga's url";
            throw new MangaWriteException(manga.getSlug(), message);
        }

        return mangaRepo.save(manga);
    }

    /**
     * Deletes the manga with the given slug
     *
     * @param slug
     *  the manga's slug
     *
     * @throws MangaNotFoundException
     *  when the manga does not exist in the database
     */
    public void deleteMangaBySlug(String slug) throws MangaNotFoundException {
        mangaRepo.deleteMangaBySlug(slug)
            .orElseThrow(() -> new MangaNotFoundException(slug));
    }

    /**
     * Gets a list of all the chapters for a given manga
     *
     * @param slug
     *  the manga's slug
     *
     * @return the list of chapters for the manga with the given slug
     *
     * @throws MangaNotFoundException
     *  when the manga does not exist in the database
     */
    public List<Chapter> getAllChaptersByMangaSlug(String slug) throws MangaNotFoundException {
        Manga manga = getFullMangaBySlug(slug);

        return new ArrayList<>(manga.getChapters());
    }

    /**
     * Adds a new chapter to the given manga.
     * Does nothing if the chapter already exists.
     *
     * @param slug
     *  the manga's slug
     * @param chapter
     *  the chapter to add
     *
     * @return the added chapter
     *
     * @throws MangaNotFoundException
     *  when the manga does not exist in the database
     * @throws ChapterWriteException
     *  when the chapter already exists in the database
     * @throws MongoWriteException
     *  when the database write fails
     */
    public Chapter addChapter(String slug, Chapter chapter) throws MangaNotFoundException, ChapterWriteException, MongoWriteException {
        Manga manga = getFullMangaBySlug(slug);

        if (!manga.addChapter(chapter)) {
            String message = "Chapter already existed";
            throw new ChapterWriteException(manga.getSlug(), chapter.getSlug(), message);
        }
        mangaRepo.save(manga);

        return chapter;
    }

    /**
     * Adds a new chapter from a given link element
     *
     * @param slug
     *  the manga's slug
     * @param link
     *  the link element for the chapter
     *
     * @return the added chapter
     *
     * @throws MangaNotFoundException
     *  when the manga does not exist in the database
     * @throws ChapterWriteException
     *  when the chapter already exists in the database
     * @throws MongoWriteException
     *  when the database write fails
     */
    public Chapter addChapterFromLinkElement(String slug, Element link) throws MangaNotFoundException, ChapterWriteException, MongoWriteException {
        String chapterSlug = link.text().replace("Chapter ", "");
        String chapterUrl = "https://mangapill.com" + link.attr("href");

        float chapterNumber = Float.parseFloat(chapterSlug);

        Chapter chapter = new Chapter(chapterSlug, chapterNumber, chapterUrl);

        return addChapter(slug, chapter);
    }

    /**
     * Gets the chapter matching the given slugs
     *
     * @param slug
     *  the manga's slug
     * @param chapterSlug
     *  the chapter's slug
     *
     * @return the chapter matching the given inputs
     *
     * @throws ChapterNotFoundException
     *  when the chapter does not exist in the database
     */
    public Chapter getChapterByMangaAndChapterSlug(String slug, String chapterSlug) throws ChapterNotFoundException {
        return mangaRepo.findChapterByMangaAndChapterSlug(slug, chapterSlug)
            .orElseThrow(() -> new ChapterNotFoundException(slug, chapterSlug));
    }

    /**
     * Updates the given chapter
     *
     * @param slug
     *  the manga's slug
     * @param chapterSlug
     *  the chapter's slug
     *
     * @return the chapter matching the given inputs
     *
     * @throws MangaNotFoundException
     *  when the manga does not exist in the database
     * @throws ChapterNotFoundException
     *  when the chapter does not exist in the database
     * @throws ChapterWriteException
     *  when the chapter update fails
     */
    public Chapter updateChapter(String slug, Chapter chapter) throws MangaNotFoundException, ChapterNotFoundException, ChapterWriteException {
        Manga manga = getFullMangaBySlug(slug);

        Chapter oldChapter = getChapterByMangaAndChapterSlug(slug, chapter.getSlug());

        if (!chapter.getUrl().equals(oldChapter.getUrl())) {
            String message = "Can't change the chapter's url";
            throw new ChapterWriteException(slug, chapter.getSlug(), message);
        }

        manga.updateChapter(chapter);
        mangaRepo.save(manga);

        return chapter;
    }

    // TODO: remove below?

    /**
     * Deletes all the chapters for the given manga
     *
     * @param slug
     *  the manga's slug
     */
    public void deleteAllChaptersByMangaSlug(String slug) {
        mangaRepo.deleteAllChaptersByMangaSlug(slug);
    }

    /**
     * Deletes the given chapter from the manga
     *
     * @param slug
     *  the manga's slug
     * @param chapterSlug
     *  the chapter's slug
     *
     * @throws ChapterNotFoundException
     *  when the chapter does not exist in the database
     */
    public void deleteChapterByMangaAndChapterSlug(String slug, String chapterSlug) throws ChapterNotFoundException {
        mangaRepo.deleteChapterByMangaAndChapterSlug(slug, chapterSlug)
            .orElseThrow(() -> new ChapterNotFoundException(slug, chapterSlug));
    }

}
