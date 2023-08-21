package com.example.mangawatcher.db_models;

import java.io.Serializable;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
// import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * Database Model for Chapters
 * 
 * @author Andrew Mink
 * @version Aug 19, 2023
 * @since 1.0
 */
@Document("chapters")
@CompoundIndex(name = "chapter_id", def = "{'manga_slug': 1, 'number': 1}", unique = true)
public class Chapter implements Serializable {

    // @MongoId
    // private String id;

    @Indexed
    @Field("manga_slug")
    private String mangaSlug;

    @Indexed
    private String slug;

    private float number;

    private String name = "";

    private String url;

    @Field("is_read")
    private boolean isRead = false;

    /**
     * Blank class constructor
     */
    public Chapter() {
        super();
    }

    /**
     * Minimum class constructor
     * 
     * @param mangaSlug the manga's slug
     * @param slug      the chapter's slug
     * @param number    the chapter's number
     * @param url       the chapter's url
     */
    public Chapter(String mangaSlug, String slug, float number, String url) {
        super();
        this.mangaSlug = mangaSlug;
        this.slug = slug;
        this.number = number;
        this.url = url;
    }

    /**
     * Full class constructor
     * 
     * @param mangaSlug the manga's slug
     * @param slug      the chapter's slug
     * @param number    the chapter's number
     * @param url       the chapter's url
     * @param isRead    <code>true</code> if the manga has been read;
     *                  <code>false</code> otherwise
     */
    public Chapter(String mangaSlug, float number, String name, String url, boolean isRead) {
        super();
        this.mangaSlug = mangaSlug;
        this.number = number;
        this.name = name;
        this.url = url;
        this.isRead = isRead;
    }

    public String getMangaSlug() {
        return mangaSlug;
    }

    public void setMangaSlug(String mangaSlug) {
        this.mangaSlug = mangaSlug;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public float getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        return String.format(
                "Chapter{manga_slug=%s, number=%f, name=%s, url=%s, is_read=%s}",
                mangaSlug, number, name, url, isRead ? "true" : "false");
    }

}
