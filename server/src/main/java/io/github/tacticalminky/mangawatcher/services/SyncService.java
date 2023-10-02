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
 * @version Oct 1, 2023
 * @since 1.0.0-b.4
 */
@Service
public class SyncService {
    @Autowired
    private MangaService mangaService;

    /**
     * Syncs every manga
     *
     * @throws MongoWriteException
     *  when manga database update fails
     */
    public void syncAllManga() throws MongoWriteException {
        List<Manga> magnas = mangaService.getAllAsFullManga();
        for (Manga manga : magnas) {
            syncManga(manga);
        }
    }

    /**
     * Syncs the given manga
     *
     * @param slug
     *  the manga's slug
     *
     * @throws MangaNotFoundException
     *  when the manga is not found in the database
     * @throws MongoWriteException
     *  when manga database update fails
     */
    public void syncMangaBySlug(String slug) throws MangaNotFoundException, MongoWriteException {
        Manga manga = mangaService.getFullMangaBySlug(slug);

        syncManga(manga);
    }

    /**
     * Syncs the given manga.
     * The meat and potatoes function
     *
     * @param manga
     *  the manga to be synced
     *
     * @return a list of the chapters added
     */
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
                mangaService.updateManga(manga);
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

            return addedChapters;
        } catch (IOException ex) {
            return null;
        }
    }

}
