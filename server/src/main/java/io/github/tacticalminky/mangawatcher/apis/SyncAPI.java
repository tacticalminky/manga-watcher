package io.github.tacticalminky.mangawatcher.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.github.tacticalminky.mangawatcher.exceptions.MangaNotFoundException;
import io.github.tacticalminky.mangawatcher.services.SyncService;

/**
 * API mapping for the sync service
 *
 * @author Andrew Mink
 * @version Oct 9, 2023
 * @since 1.0.0-b.4
 */
@RestController
@RequestMapping("/sync")
public class SyncAPI {
    @Autowired
    private SyncService syncService;

    /**
     * Get mapping for syncing all manga
     *
     * @return http response with status message
     */
    @GetMapping
    public ResponseEntity<?> syncAllManga() {
        try {
            syncService.syncAllManga();

            return new ResponseEntity<String>("{\"message\":\"success\"}", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get mapping for syncing a single manga
     *
     * @param slug the manga's slug
     *
     * @return http response with status message
     */
    @GetMapping("/{slug}")
    public ResponseEntity<?> syncMangaBySlug(@PathVariable("slug") String slug) {
        try {
            syncService.syncMangaBySlug(slug);

            return new ResponseEntity<String>("{\"message\":\"success\"}", HttpStatus.OK);
        } catch (MangaNotFoundException ex) {
            return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
