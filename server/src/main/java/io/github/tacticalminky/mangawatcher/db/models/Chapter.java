package io.github.tacticalminky.mangawatcher.db.models;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * Database Model for Chapters
 *
 * @author Andrew Mink
 * @version Sept 23, 2023
 * @since 1.0
 */
public class Chapter implements Serializable {
    @MongoId
    private String slug;

    private float number;

    private String name;

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
     * @param slug      the chapter's slug
     * @param number    the chapter's number
     * @param url       the chapter's url
     */
    public Chapter(String slug, float number, String url) {
        super();
        this.slug = slug;
        this.number = number;
        this.url = url;
    }

    /**
     * Full class constructor
     *
     * @param slug      the chapter's slug
     * @param number    the chapter's number
     * @param url       the chapter's url
     * @param isRead    <code>true</code> if the manga has been read;
     *                  <code>false</code> otherwise
     */
    public Chapter(String slug, float number, String name, String url, boolean isRead) {
        super();
        this.slug = slug;
        this.number = number;
        this.name = name;
        this.url = url;
        this.isRead = isRead;
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

}
