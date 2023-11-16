package io.github.tacticalminky.mangawatcher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.github.tacticalminky.mangawatcher.db.models.Manga;
import io.github.tacticalminky.mangawatcher.services.*;

/**
 * Sets a schedule to sync the manga every hour
 *
 * @author Andrew Mink
 * @version Sept 30, 2023
 * @since 1.0.0-b.4
 */
@Component
public class ScheduledTasks {
    @Autowired
    private MangaService mangaService;

    @Autowired
    private SyncService syncService;

    @Autowired
    private TelegramService telegramService;

    @Scheduled(cron = "0 0 * * * *")
    public void scheduledSync() {
        List<Manga> mangas = mangaService.getAllAsFullManga();
        for (Manga manga : mangas) {
            List<String> addedChapters = syncService.syncManga(manga);

            if (manga.isMonitored() && addedChapters.size() != 0) {
                telegramService.notifyOfMangaUpdate(manga, addedChapters);
            }
        }
    }
}
