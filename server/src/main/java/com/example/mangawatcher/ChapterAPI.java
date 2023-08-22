package com.example.mangawatcher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.mangawatcher.db_models.Chapter;
import com.example.mangawatcher.exceptions.*;
import com.example.mangawatcher.services.ChapterService;

/**
 * 
 * @author Andrew Mink
 * @version Aug 19, 2023
 * @since 1.0
 */
@RestController
@RequestMapping("/manga/{manga_slug}/chapters")
public class ChapterAPI {
    @Autowired
    private ChapterService chapterService;

    @GetMapping
    public ResponseEntity<?> getAllChaptersByMangaSlug(@PathVariable("manga_slug") String mangaSlug) {
        try {
            List<Chapter> chapters = chapterService.findAllChaptersByMangaSlug(mangaSlug);
            return new ResponseEntity<List<Chapter>>(chapters, HttpStatus.OK);
        } catch (MangaNotFoundException ex) {
            return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> addChapter(@PathVariable("manga_slug") String mangaSlug, @RequestBody Chapter chapter) {
        if (!chapter.getMangaSlug().equals(mangaSlug)) {
            String message = "The managa slug must match between the URI and chapter body";
            return new ResponseEntity<String>("{\"error_message\":\"" + message + "\"}", HttpStatus.BAD_REQUEST);
        }
        
        try {
            chapter.setMangaSlug(mangaSlug);
            Chapter createdChapter = chapterService.addChapter(chapter);
            return new ResponseEntity<Chapter>(createdChapter, HttpStatus.CREATED);
        } catch (MangaNotFoundException ex) {
            return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        } catch (DuplicateKeyException ex) {
            return new ResponseEntity<>("{\"error_message\":\"" + ex.getRootCause().getMessage() + "\"}", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{chapter_slug}")
    public ResponseEntity<?> getChapterByMangaAndChapterSlug(@PathVariable("manga_slug") String mangaSlug, @PathVariable("chapter_slug") String slug) {
        try {
            Chapter chapter = chapterService.findChapterByMangaAndChapterSlug(mangaSlug, slug);
            return new ResponseEntity<Chapter>(chapter, HttpStatus.OK);
        } catch (MangaNotFoundException | ChapterNotFoundException ex) {
            return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateChatper(@PathVariable("manga_slug") String mangaSlug, @RequestBody Chapter chapter) {
        if (!chapter.getMangaSlug().equals(mangaSlug)) {
            String message = "The managa slug must match between the URI and chapter body";
            return new ResponseEntity<String>("{\"error_message\":\"" + message + "\"}", HttpStatus.BAD_REQUEST);
        }

        try {
            Chapter updatedChapter = chapterService.updateChapter(chapter);
            return new ResponseEntity<Chapter>(updatedChapter, HttpStatus.OK);
        } catch (MangaNotFoundException | ChapterNotFoundException ex) {
            return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        } catch (ChapterUpdateException ex) {
            return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{chapter_slug}")
    public ResponseEntity<String> deleteChapterByMangaAndChapterSlug(@PathVariable("manga_slug") String mangaSlug, @PathVariable("chapter_slug") String slug) {
        try {
            chapterService.deleteChapterByMangaAndChapterSlug(mangaSlug, slug);
            return new ResponseEntity<String>("{\"message\":\"success\"}", HttpStatus.OK);
        } catch (MangaNotFoundException | ChapterNotFoundException ex) {
            return new ResponseEntity<String>("{\"error_message\":\"" + ex.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

}
