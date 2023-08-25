package com.example.mangawatcher.services;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mongodb.MongoWriteException;

import com.example.mangawatcher.db_models.*;
import com.example.mangawatcher.exceptions.ChapterWriteException;
import com.example.mangawatcher.exceptions.MangaNotFoundException;

/**
 * 
 * @author Andrew Mink
 * @version Aug 24, 2023
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

    public void syncManga(Manga manga) {
        try {
            Document mangaPage = Jsoup.connect(manga.getUrl()).get();

            /**     Update Discription      */
            Elements metaTags = mangaPage.getElementsByTag("meta");
            for (Element metaTag : metaTags) {
                if (metaTag.attr("name").equals("description")) {
                    manga.setDescription(metaTag.attr("content"));
                }
            }

            /**     Update Image URL        */
            // Elements imgTags = mangaPage.getElementsByTag("img");
            // manga.setImageUrl(imgTags.get(0).attr("data-src"));

            /**     Update Chapters         */
            Element chaptersDiv = mangaPage.getElementById("chapters");
            Elements links = chaptersDiv.getElementsByTag("a");
            for (Element link : links) {
                try {
                    chapterService.addChapterFromLinkElement(manga.getSlug(), link);
                } catch (ChapterWriteException ex) {}
            }

            mangaService.updateManga(manga);
        } catch (IOException ex) {}
    }

}
