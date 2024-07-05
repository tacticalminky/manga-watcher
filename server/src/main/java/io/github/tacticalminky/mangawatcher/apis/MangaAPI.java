package io.github.tacticalminky.mangawatcher.apis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mongodb.MongoWriteException;

import io.github.tacticalminky.mangawatcher.exceptions.*;
import io.github.tacticalminky.mangawatcher.db.models.*;
import io.github.tacticalminky.mangawatcher.services.MangaService;

/**
 * API mappings for the manga service
 *
 * @author Andrew Mink
 * @version July 5, 2024
 * @since 0.5.0
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
    public ResponseEntity<?> getAllManga() {
        try {
            List<MinimalManga> manga = mangaService.getAllAsMinimalManga();

            return new ResponseEntity<List<MinimalManga>>(manga, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Post mapping for adding a manga
     *
     * @param newManga the manga to add
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
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
        } catch (MongoWriteException ex) {
            if (ex.getCode() == 11000) {
                return new ResponseEntity<>(ex, HttpStatus.CONFLICT);
            }

            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get mapping for getting a manga
     *
     * @param slug the manga's slug
     *
     * @return http response with the manga or error message
     *
     * @see MangaService#getBaseMangaBySlug(String)
     */
    @GetMapping("/{slug}")
    public ResponseEntity<?> getMangaBySlug(@PathVariable("slug") String slug) {
        try {
            Manga manga = mangaService.getMangaBySlug(slug);

            return new ResponseEntity<Manga>(manga, HttpStatus.OK);
        } catch (MangaNotFoundException ex) {
            return new ResponseEntity<>(ex, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Put mapping for updating a manga
     *
     * @param manga the manga to update
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
            return new ResponseEntity<>(ex, HttpStatus.NOT_FOUND);
        } catch (MangaWriteException ex) {
            return new ResponseEntity<>(ex, HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Put mapping for updating a chapter
     *
     * @param slug    the manga's slug
     * @param chapter the updated chapter
     *
     * @return http response with the updated chapter or error message
     *
     * @see MangaService#updateChapter(String, Chapter)
     */
    @PutMapping("/{slug}/chapters")
    public ResponseEntity<?> updateChapter(@PathVariable("slug") String slug, @RequestBody Chapter chapter) {
        try {
            Chapter updatedChapter = mangaService.updateChapter(slug, chapter);

            return new ResponseEntity<Chapter>(updatedChapter, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete mapping for removing a manga
     *
     * @param slug the manga's slug
     *
     * @return http response with success or error message
     *
     * @see MangaService#deleteMangaBySlug(String)
     */
    @DeleteMapping("/{slug}")
    public ResponseEntity<?> deleteMangaBySlug(@PathVariable("slug") String slug) {
        try {
            mangaService.deleteMangaBySlug(slug);

            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (MangaNotFoundException ex) {
            return new ResponseEntity<>(ex, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
