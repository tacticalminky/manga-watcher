package io.github.tacticalminky.mangawatcher.db.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.Assert;

/**
 * The full database model for Manga
 *
 * @author Andrew Mink
 * @version Oct 1, 2023
 * @since 1.0.0-b.4
 */
@Document("manga")
public class Manga extends NewManga {
    private String description, imageUrl;

    private boolean isMonitored = true;
    private boolean isDescriptionLocked = false;
    private boolean isImageUrlLocked = false;

    private Set<Chapter> chapters = new HashSet<>();

    public Manga() {
    }

    /**
     * Minimal class constructor
     *
     * @param title the title of the manga
     * @param url   the url for the manga
     */
    public Manga(String title, String url) {
        super(title, url);
    }

    /**
     * Full class constructor
     *
     * @param title       the manga's title
     * @param url         the manga's url
     * @param description the description of the manga's plot
     * @param imageUrl    the url for the manga's image
     * @param isMonitored the monitored status of the manga
     */
    public Manga(String title, String url, String description, String imageUrl, boolean isMonitored) {
        super(title, url);

        this.description = description;
        this.imageUrl = imageUrl;
        this.isMonitored = isMonitored;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isMonitored() {
        return isMonitored;
    }

    public void setIsMonitored(boolean isMonitored) {
        this.isMonitored = isMonitored;
    }

    public boolean isDescriptionLocked() {
        return isDescriptionLocked;
    }

    public void getIsDescriptionLocked(boolean isDescriptionLocked) {
        this.isDescriptionLocked = isDescriptionLocked;
    }

    public boolean isImageUrlLocked() {
        return isImageUrlLocked;
    }

    public void getIsImageUrlLocked(boolean isImageUrlLocked) {
        this.isImageUrlLocked = isImageUrlLocked;
    }

    public List<Chapter> getChapters() {
        return Collections.unmodifiableList(new ArrayList<Chapter>(chapters));
    }

    public Optional<Chapter> getChapter(String slug) {
        for (Chapter chapter : chapters) {
            if (chapter.getSlug().equals(slug)) {
                return Optional.of(chapter);
            }
        }

        return Optional.empty();
    }

    // returns true if successfully added a new chapter
    public boolean addChapter(Chapter chapter) {
        Assert.notNull(chapter, "Chapter must not be null");

        return chapters.add(chapter);
    }

    // returns true if successfully updated the chapter
    public boolean updateChapter(Chapter chapter) {
        if (!chapters.remove(chapter))
            return false;

        return addChapter(chapter);
    }

}
