package io.github.tacticalminky.mangawatcher.db.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * Database Model for Manga
 *
 * @author Andrew Mink
 * @version Aug 24, 2023
 * @since 1.0
 */
@Document("manga")
public class Manga implements Serializable {
    // TODO: add Chapter collection to list

    @MongoId
    private String slug;

    @Indexed(unique = true)
    private String title;

    @Indexed(unique = true)
    private String url;

    private String description;

    @Field("image_url")
    private String imageUrl;

    @Field("is_monitored")
    private boolean isMonitored = true;

    @Field("is_description_locked")
    private boolean isDescriptionLocked = false;

    @Field("is_image_url_locked")
    private boolean isImageUrlLocked = false;

    private List<Chapter> chapters;

    /**
     * Blank class constructor
     */
    public Manga() {
        super();
    }

    /**
     * Minimum class constructor
     *
     * @param title the title of the manga
     * @param url   the url for the manga
     */
    public Manga(String title, String url) {
        super();
        this.title = title;
        this.url = url;

        this.chapters = new ArrayList<>();
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
        super();
        this.title = title;
        this.url = url;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isMonitored = isMonitored;

        this.chapters = new ArrayList<>();
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
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

    // TODO: getters and setters for chapters
    public List<Chapter> getChapters() {
        return chapters;
    }

    public void addChapter(Chapter chapter) {
        chapters.add(chapter);
    }

    public void updateChapter(Chapter chapter) {
        // TODO: implement
    }

}
