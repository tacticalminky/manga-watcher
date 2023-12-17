package io.github.tacticalminky.mangawatcher.services;

import java.util.List;

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
 * @version Dec 17, 2023
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
     * @return a list of manga in their minimal form
     *
     * @see MangaRepo#findAllAsMinimalMangaByRegex()
     */
    public List<MinimalManga> getAllAsMinimalManga() {
        return mangaRepo.findAllAsMinimalMangaByRegex(".");
    }

    /**
     * Gets a list of all manga
     *
     * @return a list of manga in their full form
     */
    public List<Manga> getAllAsFullManga() {
        return mangaRepo.findAll(Sort.by("title"));
    }

    /**
     * Adds a new manga to the database
     *
     * @param newManga a minimal representation of the manga to be added (title and
     *                 url)
     *
     * @return the created manga in its full form
     *
     * @throws MangaWriteException when the manga already exists
     */
    public Manga addManga(NewManga newManga) throws MongoWriteException {
        Manga manga = new Manga(newManga.getTitle(), newManga.getUrl());

        String slug = manga.getTitle().toLowerCase();
        slug = slug.replace(' ', '-');
        manga.setSlug(slug);

        Manga createdManga = mangaRepo.insert(manga);
        syncService.syncManga(manga);

        return createdManga;
    }

    /**
     * Finds the manga with the provided slug
     *
     * @param slug the manga's slug
     *
     * @return the manga
     *
     * @throws MangaNotFoundException when the manga does not exist in the database
     */
    public Manga getMangaBySlug(String slug) throws MangaNotFoundException {
        return mangaRepo.findMangaBySlug(slug)
                .orElseThrow(() -> new MangaNotFoundException(slug));
    }

    /**
     * Updates the given manga
     *
     * @param manga the new version of the manga
     *
     * @return the updated manga
     *
     * @throws MangaNotFoundException when the manga does not exist in the database
     * @throws MangaWriteException    when the manga update fails
     */
    public Manga updateManga(Manga manga) throws MangaNotFoundException, MangaWriteException {
        Manga oldManga = getMangaBySlug(manga.getSlug());

        if (!manga.getUrl().equals(oldManga.getUrl())) {
            String message = "Can't change the manga's url";
            throw new MangaWriteException(manga.getSlug(), message);
        }

        return mangaRepo.save(manga);
    }

    /**
     * Deletes the manga with the given slug
     *
     * @param slug the manga's slug
     *
     * @throws MangaNotFoundException when the manga does not exist in the database
     */
    public void deleteMangaBySlug(String slug) throws MangaNotFoundException {
        mangaRepo.deleteMangaBySlug(slug)
                .orElseThrow(() -> new MangaNotFoundException(slug));
    }

    /**
     * Gets a list of all the chapters for a given manga
     *
     * @param slug the manga's slug
     *
     * @return the list of chapters for the manga with the given slug
     *
     * @throws MangaNotFoundException when the manga does not exist in the database
     */
    public List<Chapter> getAllChaptersByMangaSlug(String slug) throws MangaNotFoundException {
        Manga manga = getMangaBySlug(slug);

        return manga.getChapters();
    }

    /**
     * Adds a new chapter to the given manga.
     * Does nothing if the chapter already exists.
     *
     * @param slug    the manga's slug
     * @param chapter the chapter to add
     *
     * @return the added chapter
     *
     * @throws MangaNotFoundException when the manga does not exist in the database
     * @throws ChapterWriteException  when the chapter already exists in the
     *                                database
     * @throws MongoWriteException    when the database write fails
     */
    public Chapter addChapter(String slug, Chapter chapter)
            throws MangaNotFoundException, ChapterWriteException, MongoWriteException {
        try {
            getChapterByMangaAndChapterSlug(slug, chapter.getSlug());

            String message = "Chapter already exists";
            throw new ChapterWriteException(slug, chapter.getSlug(), message);
        } catch (ChapterNotFoundException ex) {
        }

        mangaRepo.addChapterBySlug(slug, chapter);

        return chapter;
    }

    /**
     * Updates the chapters of the given Manga
     *
     * @param slug     the manga's slug
     * @param chapters the list of chapters
     *
     * @throws MongoWriteException when the database write fails
     */
    public void updateChapters(String slug, List<Chapter> chapters) throws MongoWriteException {
        mangaRepo.updateChaptersBySlug(slug, chapters);
    }

    /**
     * Gets the chapter matching the given slugs
     *
     * @param slug        the manga's slug
     * @param chapterSlug the chapter's slug
     *
     * @return the chapter matching the given inputs
     *
     * @throws MangaNotFoundException   when the manga does not exist in the
     *                                  database
     * @throws ChapterNotFoundException when the chapter does not exist in the
     *                                  database
     */
    public Chapter getChapterByMangaAndChapterSlug(String slug, String chapterSlug)
            throws MangaNotFoundException, ChapterNotFoundException {
        Manga manga = getMangaBySlug(slug);

        return manga.getChapter(chapterSlug)
                .orElseThrow(() -> new ChapterNotFoundException(slug, chapterSlug));
    }

    /**
     * Updates the given chapter
     *
     * @param slug    the manga's slug
     * @param chapter the chapter
     *
     * @return the chapter matching the given inputs
     *
     * @throws MongoWriteException when the database write fails
     */
    public Chapter updateChapter(String slug, Chapter chapter) throws MongoWriteException {
        mangaRepo.updateChapterIsReadBySlug(slug, chapter.getSlug(), chapter.getIsRead());

        return chapter;
    }

}
