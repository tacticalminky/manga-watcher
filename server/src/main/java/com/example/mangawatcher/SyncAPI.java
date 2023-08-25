package com.example.mangawatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.mangawatcher.exceptions.MangaNotFoundException;
import com.example.mangawatcher.services.SyncService;
import com.mongodb.MongoWriteException;

/**
 * 
 * @author Andrew Mink
 * @version Aug 24, 2023
 * @since 1.0
 */
@RestController
@RequestMapping("/sync")
public class SyncAPI {
    @Autowired
    private SyncService syncService;

    @GetMapping
    public ResponseEntity<?> syncAllManga() {
        try {
            syncService.syncAllManga();
            return new ResponseEntity<String>("{\"message\":\"success\"}", HttpStatus.OK);
        } catch (MongoWriteException ex) {
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{slug}")
    public ResponseEntity<String> syncMangaBySlug(@PathVariable("slug") String slug) {
        try {
            syncService.syncMangaBySlug(slug);
            return new ResponseEntity<String>("{\"message\":\"success\"}", HttpStatus.OK);
        } catch (MangaNotFoundException ex) {
            return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        } catch (DuplicateKeyException ex) {
            return new ResponseEntity<>("{\"error_message\":\"" + ex.getRootCause().getMessage() + "\"}", HttpStatus.CONFLICT);
        }
    }

}
