package io.github.tacticalminky.mangawatcher.apis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mongodb.MongoWriteException;

import io.github.tacticalminky.mangawatcher.exceptions.MangaNotFoundException;
import io.github.tacticalminky.mangawatcher.exceptions.MangaWriteException;
import io.github.tacticalminky.mangawatcher.db.models.Manga;
import io.github.tacticalminky.mangawatcher.services.MangaService;

/**
 * 
 * @author Andrew Mink
 * @version Aug 31, 2023
 * @since 1.0
 */
@RestController
@RequestMapping("/api/manga")
public class MangaAPI {
    @Autowired
    private MangaService mangaService;

    @GetMapping
    public ResponseEntity<List<Manga>> getAllManga() {
        List<Manga> mangas = mangaService.getAllManga();
        return new ResponseEntity<List<Manga>>(mangas, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addManga(@RequestBody Manga manga) {
        try {
            Manga createdManga = mangaService.addManga(manga);
            return new ResponseEntity<Manga>(createdManga, HttpStatus.CREATED);
        } catch (MongoWriteException ex) {
            if (ex.getCode() == 11000) {
                return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}",
                        HttpStatus.CONFLICT);
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

    @GetMapping("/{slug}")
    public ResponseEntity<?> getMangaBySlug(@PathVariable("slug") String slug) {
        try {
            Manga manga = mangaService.findMangaBySlug(slug);
            return new ResponseEntity<Manga>(manga, HttpStatus.OK);
        } catch (MangaNotFoundException ex) {
            return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

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
