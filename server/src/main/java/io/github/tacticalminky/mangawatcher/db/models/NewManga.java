package io.github.tacticalminky.mangawatcher.db.models;

import org.springframework.data.mongodb.core.index.Indexed;

/**
 * The database model for Manga database model.
 * Only used when adding manga.
 *
 * @author Andrew Mink
 * @version Oct 1, 2023
 * @since 1.0.0-b.4
 */
public class NewManga extends AbstractModel {
    @Indexed(unique = true)
    private String title;

    public NewManga() {
    }

    public NewManga(String title, String url) {
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
