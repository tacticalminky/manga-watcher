package io.github.tacticalminky.mangawatcher.apis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mongodb.MongoWriteException;


import io.github.tacticalminky.mangawatcher.exceptions.*;

import io.github.tacticalminky.mangawatcher.db.models.NewManga;
import io.github.tacticalminky.mangawatcher.db.models.Manga;
import io.github.tacticalminky.mangawatcher.db.models.MinimalManga;
import io.github.tacticalminky.mangawatcher.services.MangaService;

/**
 * API mapping for manga services handling manga
 *
 * @author Andrew Mink
 * @version Oct 1, 2023
 * @since 1.0.0-b.4
 */
@RestController
@RequestMapping("/api/manga")
public class MangaAPI {
    @Autowired
    private MangaService mangaService;

    /**
     * Get mapping for getting all the manga in a minimal form
     *
     * @return http response with a list of all manga
     *
     * @see MangaService#getAllAsMinimalManga()
     */
    @GetMapping
    public ResponseEntity<List<MinimalManga>> getAllManga() {
        List<MinimalManga> manga = mangaService.getAllAsMinimalManga();

        return new ResponseEntity<List<MinimalManga>>(manga, HttpStatus.OK);
    }

    /**
     * Post mapping for adding a manga
     *
     * @param manga
     *  the manga to add
     *
     * @return http response with the created manga or error message
     *
     * @see MangaService#addManga(NewManga)
     */
    @PostMapping
    public ResponseEntity<?> addManga(@RequestBody NewManga newManga) {
        try {
            Manga createdManga = mangaService.addManga(newManga);

            return new ResponseEntity<Manga>(createdManga, HttpStatus.CREATED);
        } catch (MongoWriteException ex) {
            if (ex.getCode() == 11000) {
                return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.CONFLICT);
            }

            return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (MangaWriteException ex) {
            return new ResponseEntity<>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.CONFLICT);
        } catch (DuplicateKeyException ex) {
            return new ResponseEntity<>("{\"error_message\":\"" + ex.getCause().getMessage() + "\"}", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get mapping for getting a manga
     *
     * @param slug
     *  the manga's slug
     *
     * @return http response with the manga or error message
     *
     * @see MangaService#getBaseMangaBySlug(String)
     */
    @GetMapping("/{slug}")
    public ResponseEntity<?> getMangaBySlug(@PathVariable("slug") String slug) {
        try {
            Manga manga = mangaService.getBaseMangaBySlug(slug);

            return new ResponseEntity<Manga>(manga, HttpStatus.OK);
        } catch (MangaNotFoundException ex) {
            return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Put mapping for updating a manga
     *
     * @param manga
     *  the manga to update
     *
     * @return http response with the updated manga or error message
     *
     * @see MangaService#updateManga(Manga)
     */
    @PutMapping
    public ResponseEntity<?> updateManga(@RequestBody Manga manga) {
        try {
            Manga updatedManga = mangaService.updateManga(manga);

            return new ResponseEntity<Manga>(updatedManga, HttpStatus.OK);
        } catch (MangaNotFoundException ex) {
            return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        } catch (MangaWriteException ex) {
            return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.CONFLICT);
        }
    }

    /**
     * Delete mapping for removing a manga
     *
     * @param slug
     *  the manga's slug
     *
     * @return http response with success or error message
     *
     * @see MangaService#deleteMangaBySlug(String)
     */
    @DeleteMapping("/{slug}")
    public ResponseEntity<String> deleteMangaBySlug(@PathVariable("slug") String slug) {
        try {
            mangaService.deleteMangaBySlug(slug);

            return new ResponseEntity<String>("{\"message\":\"success\"}", HttpStatus.OK);
        } catch (MangaNotFoundException ex) {
            return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

}
