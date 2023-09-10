package io.github.tacticalminky.mangawatcher.exceptions;

public class ChapterNotFoundException extends RuntimeException {
    public ChapterNotFoundException(String mangaSlug, String chapterSlug) {
        super("Chapter with a manga slug of " + mangaSlug + " and a chapter slug of " + chapterSlug + " was not found");
    }
}
