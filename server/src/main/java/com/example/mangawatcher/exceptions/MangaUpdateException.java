package com.example.mangawatcher.exceptions;

public class MangaUpdateException extends RuntimeException {
    public MangaUpdateException(String slug, String message) {
        super("Invalid update to manga with slug " + slug + ": " + message);
    }
}
