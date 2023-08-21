package com.example.mangawatcher.exceptions;

public class MangaNotFoundException extends RuntimeException {
    public MangaNotFoundException(String slug) {
        super("Manga with slug " + slug + " was not found");
    }
}
