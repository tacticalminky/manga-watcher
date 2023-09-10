package io.github.tacticalminky.mangawatcher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.github.tacticalminky.mangawatcher.db.models.Manga;
import io.github.tacticalminky.mangawatcher.services.*;

/**
 * 
 * @author Andrew Mink
 * @version Aug 29, 2023
 * @since 1.0
 */
@Component
public class ScheduledTasts {
    @Autowired
    private MangaService mangaService;

    @Autowired
    private SyncService syncService;

    @Autowired
    private TelegramService telegramService;
    
    @Scheduled(cron = "0 0 8,12,22 * * *")
    public void ScheduledSync() {
        List<Manga> mangas = mangaService.getAllManga();
        for (Manga manga : mangas) {
            List<String> addedChapters = syncService.syncManga(manga);

            if (manga.isMonitored() && addedChapters.size() != 0) {
                telegramService.notifyOfMangaUpdate(manga, addedChapters);
            }
        }
    }
}
