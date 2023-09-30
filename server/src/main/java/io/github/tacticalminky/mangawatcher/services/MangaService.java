package io.github.tacticalminky.mangawatcher.services;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mongodb.MongoWriteException;

import io.github.tacticalminky.mangawatcher.exceptions.*;
import io.github.tacticalminky.mangawatcher.db.models.*;
import io.github.tacticalminky.mangawatcher.db.repos.MangaRepo;

/**
 * The service for manga and chapters
 *
 * @author Andrew Mink
 * @version Sept 30, 2023
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
     */
    public List<Manga> getAllMinimalManga() {
        return mangaRepo.findAllMinimalMangas();
    }

    /**
     * Gets a list of all manga
     *
     * @return a list of manga in their full form
     */
    public List<Manga> getAllFullManga() {
        return mangaRepo.findAllFullMangas();
    }

    /**
     * Adds a new manga to the database
     *
     * @param minimalManga
     *  a minimal representation of the manga to be added
     *
     * @return the created manga in its full form
     *
     * @throws MongoWriteException
     *  when the manga already exists
     */
    public Manga addManga(MinimalManga minimalManga) throws MongoWriteException {
        Manga manga = (Manga) minimalManga;

        String slug = manga.getTitle().toLowerCase();
        slug = slug.replace(' ', '-');
        manga.setSlug(slug);

        try {
            findBaseMangaBySlug(manga.getSlug());

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
     */
    public Manga findBaseMangaBySlug(String slug) throws MangaNotFoundException {
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
     */
    public Manga findFullMangaBySlug(String slug) throws MangaNotFoundException {
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
        Manga oldManga = findFullMangaBySlug(manga.getSlug());

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

    // TODO: validate below

    public List<Chapter> findAllChaptersByMangaSlug(String slug) throws MangaNotFoundException {
        Manga manga = findFullMangaBySlug(slug);
        return new ArrayList<>(manga.getChapters());
    }

    public Chapter addChapter(String slug, Chapter chapter) throws MangaNotFoundException, MongoWriteException, ChapterWriteException {
        Manga manga = findFullMangaBySlug(slug);

        try {
            findChapterByMangaAndChapterSlug(slug, chapter.getSlug());

            String message = "Chapter already exists, can't create new";
            throw new ChapterWriteException(slug, chapter.getSlug(), message);
        } catch (ChapterNotFoundException ex) {
            manga.addChapter(chapter);
            mangaRepo.save(manga);

            return mangaRepo.findChapterByMangaAndChapterSlug(slug, chapter.getSlug()).get();
        }
    }

    public Chapter addChapterFromLinkElement(String slug, Element link) throws MangaNotFoundException, MongoWriteException, ChapterWriteException {
        String chapterSlug = link.text().replace("Chapter ", "");
        String chapterUrl = "https://mangapill.com" + link.attr("href");

        float chapterNumber = Float.parseFloat(chapterSlug);

        Chapter chapter = new Chapter(chapterSlug, chapterNumber, chapterUrl);

        return addChapter(slug, chapter);
    }

    public Chapter findChapterByMangaAndChapterSlug(String slug, String chapterSlug) throws MangaNotFoundException, ChapterNotFoundException {
        return mangaRepo.findChapterByMangaAndChapterSlug(slug, chapterSlug)
            .orElseThrow(() -> new ChapterNotFoundException(slug, chapterSlug));
    }

    public Chapter updateChapter(String slug, Chapter chapter) throws MangaNotFoundException, ChapterNotFoundException, ChapterWriteException {
        Manga manga = findFullMangaBySlug(slug);

        Chapter oldChapter = findChapterByMangaAndChapterSlug(slug, chapter.getSlug());

        if (!chapter.getUrl().equals(oldChapter.getUrl())) {
            String message = "Can't change the chapter's url";
            throw new ChapterWriteException(slug, chapter.getSlug(), message);
        }

        manga.updateChapter(chapter);
        mangaRepo.save(manga);

        return chapter;
    }

    public void deleteAllChaptersByMangaSlug(String slug) {
        mangaRepo.deleteAllChaptersByMangaSlug(slug);
    }

    public void deleteChapterByMangaAndChapterSlug(String slug, String chapterSlug) throws MangaNotFoundException, ChapterNotFoundException {
        mangaRepo.deleteChapterByMangaAndChapterSlug(slug, chapterSlug)
            .orElseThrow(() -> new ChapterNotFoundException(slug, chapterSlug));
    }

}
