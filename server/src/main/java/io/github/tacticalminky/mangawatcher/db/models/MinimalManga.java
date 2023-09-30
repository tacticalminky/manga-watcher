package io.github.tacticalminky.mangawatcher.db.models;

import org.springframework.data.mongodb.core.index.Indexed;

/**
 * The minimal database model for Manga database model.
 * Only used when adding manga.
 *
 * @author Andrew Mink
 * @version Sept 30, 2023
 * @since 1.0.0-b.4
 */
public class MinimalManga extends AbstractModel {
    @Indexed(unique = true)
    private String title;

    public MinimalManga(String title, String url) {
        super(url);

        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
