package io.github.tacticalminky.mangawatcher.apis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.github.tacticalminky.mangawatcher.exceptions.*;
import io.github.tacticalminky.mangawatcher.db.models.Chapter;
import io.github.tacticalminky.mangawatcher.services.MangaService;

/**
 *
 * @author Andrew Mink
 * @version Sept 30, 2023
 * @since 1.0.0-b.4
 */
@RestController
@RequestMapping("/api/manga/{manga_slug}/chapters")
public class ChapterAPI {
    @Autowired
    private MangaService mangaService;

    @GetMapping
    public ResponseEntity<?> getAllChaptersByMangaSlug(@PathVariable("manga_slug") String mangaSlug) {
        try {
            List<Chapter> chapters = mangaService.findAllChaptersByMangaSlug(mangaSlug);

            return new ResponseEntity<List<Chapter>>(chapters, HttpStatus.OK);
        } catch (MangaNotFoundException ex) {
            return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> addChapter(@PathVariable("manga_slug") String mangaSlug, @RequestBody Chapter chapter) {
        try {
            Chapter createdChapter = mangaService.addChapter(mangaSlug, chapter);

            return new ResponseEntity<Chapter>(createdChapter, HttpStatus.CREATED);
        } catch (MangaNotFoundException ex) {
            return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        } catch (MangaWriteException ex) {
            return new ResponseEntity<>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.CONFLICT);
        } catch (DuplicateKeyException ex) {
            return new ResponseEntity<>("{\"error_message\":\"" + ex.getCause().getMessage() + "\"}", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{chapter_slug}")
    public ResponseEntity<?> getChapterByMangaAndChapterSlug(@PathVariable("manga_slug") String mangaSlug, @PathVariable("chapter_slug") String slug) {
        try {
            Chapter chapter = mangaService.findChapterByMangaAndChapterSlug(mangaSlug, slug);

            return new ResponseEntity<Chapter>(chapter, HttpStatus.OK);
        } catch (MangaNotFoundException | ChapterNotFoundException ex) {
            return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateChatper(@PathVariable("manga_slug") String mangaSlug, @RequestBody Chapter chapter) {
        try {
            Chapter updatedChapter = mangaService.updateChapter(mangaSlug, chapter);

            return new ResponseEntity<Chapter>(updatedChapter, HttpStatus.OK);
        } catch (MangaNotFoundException | ChapterNotFoundException ex) {
            return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        } catch (ChapterWriteException ex) {
            return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{chapter_slug}")
    public ResponseEntity<String> deleteChapterByMangaAndChapterSlug(@PathVariable("manga_slug") String mangaSlug, @PathVariable("chapter_slug") String slug) {
        try {
            mangaService.deleteChapterByMangaAndChapterSlug(mangaSlug, slug);

            return new ResponseEntity<String>("{\"message\":\"success\"}", HttpStatus.OK);
        } catch (MangaNotFoundException | ChapterNotFoundException ex) {
            return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

}
