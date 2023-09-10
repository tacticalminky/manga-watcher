package io.github.tacticalminky.mangawatcher.exceptions;

public class ChapterWriteException extends RuntimeException {
    public ChapterWriteException(String mangaSlug, String chapterSlug, String message) {
        super("Invalid write to chapter with a manga slug of " + mangaSlug + " and a chapter slug of " + chapterSlug + ": " + message);
    }
}
