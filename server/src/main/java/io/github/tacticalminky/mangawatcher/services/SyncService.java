package io.github.tacticalminky.mangawatcher.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mongodb.MongoWriteException;

import io.github.tacticalminky.mangawatcher.exceptions.ChapterWriteException;
import io.github.tacticalminky.mangawatcher.exceptions.MangaNotFoundException;
import io.github.tacticalminky.mangawatcher.db.models.*;

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

    // @Autowired
    // private ChapterService chapterService;

    public void syncAllManga() throws MongoWriteException {
        List<Manga> magnas = mangaService.getAllManga();
        for (Manga manga : magnas) {
            syncManga(manga);
        }
    }

    public void syncMangaBySlug(String slug) throws MangaNotFoundException, MongoWriteException {
        Manga manga = mangaService.findMangaBySlug(slug);
        syncManga(manga);
    }

    public List<String> syncManga(Manga manga) {
        try {
            Document mangaPage = Jsoup.connect(manga.getUrl()).get();

            /** Update Description */
            if (!manga.isDescriptionLocked()) {
                Elements metaTags = mangaPage.getElementsByTag("meta");
                for (Element metaTag : metaTags) {
                    if (metaTag.attr("name").equals("description")) {
                        manga.setDescription(metaTag.attr("content"));
                        break;
                    }
                }
            }

            /** Update Chapters */
            List<String> addedChapters = new ArrayList<String>();

            Element chaptersDiv = mangaPage.getElementById("chapters");
            Elements links = chaptersDiv.getElementsByTag("a");
            for (Element link : links) {
                try {
                    Chapter createdChapter = mangaService.addChapterFromLinkElement(manga.getSlug(), link);
                    addedChapters.add(createdChapter.getSlug());
                } catch (ChapterWriteException ex) {
                }
            }

            mangaService.updateManga(manga);

            return addedChapters;
        } catch (IOException ex) {
            return null;
        }

    }

}
