package io.github.tacticalminky.mangawatcher.exceptions;

public class MangaWriteException extends RuntimeException {
    public MangaWriteException(String slug, String message) {
        super("Invalid write to manga with slug " + slug + ": " + message);
    }
}
