package com.example.mangawatcher.exceptions;

public class ChapterUpdateException extends RuntimeException {
    public ChapterUpdateException(String mangaSlug, String chapterSlug, String message) {
        super("Invalid update to chapter with a manga slug of " + mangaSlug + " and a chapter slug of " + chapterSlug + ": " + message);
    }
}
