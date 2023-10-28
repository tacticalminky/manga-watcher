package io.github.tacticalminky.mangawatcher.db.models;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.IndexDirection;

/**
 * Database Model for Chapters
 *
 * @author Andrew Mink
 * @version Oct 1, 2023
 * @since 1.0.0-b.4
 */
public class Chapter extends AbstractModel {
    @Indexed(direction = IndexDirection.DESCENDING)
    private float number;

    private boolean isRead = false;

    /**
     * Default class constructor
     */
    public Chapter() {
    }

    /**
     * Minimal class constructor
     *
     * @param slug   the chapter's slug
     * @param number the chapter's number
     * @param url    the chapter's url
     */
    public Chapter(String slug, float number, String url) {
        super(url);
        setSlug(slug);

        this.number = number;
    }

    /**
     * Full class constructor
     *
     * @param slug   the chapter's slug
     * @param number the chapter's number
     * @param url    the chapter's url
     * @param isRead <code>true</code> if the manga has been read;
     *               <code>false</code> otherwise
     */
    public Chapter(String slug, float number, String url, boolean isRead) {
        super(url);
        setSlug(slug);

        this.number = number;
        this.isRead = isRead;
    }

    public float getNumber() {
        return number;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

}
