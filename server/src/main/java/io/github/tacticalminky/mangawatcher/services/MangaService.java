package io.github.tacticalminky.mangawatcher.services;

import java.util.List;

import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mongodb.MongoWriteException;

import io.github.tacticalminky.mangawatcher.exceptions.ChapterNotFoundException;
import io.github.tacticalminky.mangawatcher.exceptions.ChapterWriteException;
import io.github.tacticalminky.mangawatcher.exceptions.MangaNotFoundException;
import io.github.tacticalminky.mangawatcher.exceptions.MangaWriteException;
import io.github.tacticalminky.mangawatcher.db.models.Chapter;
import io.github.tacticalminky.mangawatcher.db.models.Manga;
import io.github.tacticalminky.mangawatcher.db.repos.MangaRepo;

/**
 *
 * @author Andrew Mink
 * @version Aug 24, 2023
 * @since 1.0
 */
@Service
public class MangaService {
    @Autowired
    private MangaRepo mangaRepo;

    @Autowired
    private SyncService syncService;

    public List<Manga> getAllManga() {
        return mangaRepo.findAll();
    }

    public Manga addManga(Manga manga) throws MongoWriteException {
        String slug = manga.getTitle().toLowerCase();
        slug = slug.replace(' ', '-');
        manga.setSlug(slug);

        try {
            findMangaBySlug(manga.getSlug());
            String message = "Manga already exists, can't create new";
            throw new MangaWriteException(manga.getSlug(), message);
        } catch (MangaNotFoundException ex) {
            Manga createdManga = mangaRepo.save(manga);
            syncService.syncManga(manga);
            return createdManga;
        }
    }

    public Manga findMangaBySlug(String slug) throws MangaNotFoundException {
        return mangaRepo.findMangaBySlug(slug)
            .orElseThrow(() -> new MangaNotFoundException(slug));
    }

    public Manga updateManga(Manga manga) throws MangaNotFoundException, MangaWriteException {
        Manga oldManga = findMangaBySlug(manga.getSlug());

        if (!manga.getUrl().equals(oldManga.getUrl())) {
            String message = "Can't change the manga's url";
            throw new MangaWriteException(manga.getSlug(), message);
        }

        return mangaRepo.save(manga);
    }

    public void deleteMangaBySlug(String slug) throws MangaNotFoundException {
        mangaRepo.deleteMangaBySlug(slug)
            .orElseThrow(() -> new MangaNotFoundException(slug));
    }

    public List<Chapter> findAllChaptersByMangaSlug(String slug) throws MangaNotFoundException {
        Manga manga = findMangaBySlug(slug);
        return manga.getChapters();
    }

    public Chapter addChapter(String slug, Chapter chapter) throws MangaNotFoundException, MongoWriteException, ChapterWriteException {
        Manga manga = findMangaBySlug(slug);

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
        Manga manga = findMangaBySlug(slug);

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
