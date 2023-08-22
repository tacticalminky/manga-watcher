package com.example.mangawatcher.services;

import java.util.List;

import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mangawatcher.db_models.*;
import com.example.mangawatcher.exceptions.*;
import com.mongodb.MongoWriteException;
import com.example.mangawatcher.db_repos.ChapterRepo;

/**
 * 
 * @author Andrew Mink
 * @version Aug 19, 2023
 * @since 1.0
 */
@Service
public class ChapterService {
    @Autowired
    private ChapterRepo chapterRepo;

    @Autowired
    private MangaService mangaService;

    public List<Chapter> findAllChaptersByMangaSlug(String mangaSlug) throws MangaNotFoundException {
        Manga manga = mangaService.findMangaBySlug(mangaSlug);
        return chapterRepo.findChaptersByMangaSlug(manga.getSlug());
    }

    public Chapter addChapter(Chapter chapter) throws MangaNotFoundException, MongoWriteException {
        mangaService.findMangaBySlug(chapter.getMangaSlug());
        return chapterRepo.save(chapter);
    }

    public Chapter addChapterFromLinkElement(String mangaSlug, Element link) throws MangaNotFoundException, MongoWriteException {
        String chapterSlug = link.text().replace("Chapter ", "");
        String chapterID = mangaSlug + '-' + chapterSlug;
        String chapterUrl = "https://mangapill.com" + link.attr("href");

        float chapterNumber = Float.parseFloat(chapterSlug);

        Chapter chapter = new Chapter(chapterID, mangaSlug, chapterSlug, chapterNumber, chapterUrl);

        mangaService.findMangaBySlug(chapter.getMangaSlug());
        return chapterRepo.save(chapter);
    }

    public Chapter findChapterByMangaAndChapterSlug(String mangaSlug, String slug) throws MangaNotFoundException, ChapterNotFoundException {
        Manga manga = mangaService.findMangaBySlug(mangaSlug);
        return chapterRepo.findChapterByMangaAndChapterSlug(manga.getSlug(), slug)
            .orElseThrow(() -> new ChapterNotFoundException(manga.getSlug(), slug));
    }

    public Chapter updateChapter(Chapter chapter) throws MangaNotFoundException, ChapterNotFoundException {
        Chapter oldChapter = findChapterByMangaAndChapterSlug(chapter.getMangaSlug(), chapter.getSlug());

        if (!chapter.getUrl().equals(oldChapter.getUrl())) {
            String message = "Can't change the chapter's url";
            throw new ChapterUpdateException(chapter.getMangaSlug(), chapter.getSlug(), message);
        }

        return chapterRepo.save(chapter);
    }

    public void deleteChapterByMangaAndChapterSlug(String mangaSlug, String slug) throws MangaNotFoundException, ChapterNotFoundException {
        Manga manga = mangaService.findMangaBySlug(mangaSlug);
        chapterRepo.deleteChapterByMangaAndChapterSlug(manga.getSlug(), slug)
            .orElseThrow(() -> new ChapterNotFoundException(manga.getSlug(), slug));
    }

}
