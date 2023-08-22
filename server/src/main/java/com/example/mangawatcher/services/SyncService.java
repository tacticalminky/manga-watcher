package com.example.mangawatcher.services;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.example.mangawatcher.db_models.*;
import com.example.mangawatcher.exceptions.MangaNotFoundException;
import com.mongodb.MongoWriteException;

/**
 * 
 * @author Andrew Mink
 * @version Aug 19, 2023
 * @since 1.0
 */
@Service
public class SyncService {
    @Autowired
    private MangaService mangaService;

    @Autowired
    private ChapterService chapterService;

    public void syncAllManga() throws MongoWriteException {
        List<Manga> magnas = mangaService.findAllManga();
        for (Manga manga : magnas) {
            syncManga(manga);
        }
    }

    public void syncMangaBySlug(String slug) throws MangaNotFoundException, MongoWriteException {
        Manga manga = mangaService.findMangaBySlug(slug);
        syncManga(manga);
    }

    private void syncManga(Manga manga) {
        try {
            Document mangaPage = Jsoup.connect(manga.getUrl()).get();

            Element chaptersDiv = mangaPage.getElementById("chapters");
            Elements links = chaptersDiv.getElementsByTag("a");
            for (Element link : links) {
                try {
                    chapterService.addChapterFromLinkElement(manga.getSlug(), link);
                    
                    // String chapterSlug = link.text().replace("Chapter ", "");
                    // float chapterNumber = Float.parseFloat(chapterSlug);
                    // String chapterUrl = "https://mangapill.com" + link.attr("href");
                    
                    // Chapter chapter = new Chapter(manga.getSlug(), chapterSlug, chapterNumber, chapterUrl);
                    // chapterService.addChapter(chapter);
                } catch (DuplicateKeyException ex) {}
            }
        } catch (IOException ex) {}
    }

}
