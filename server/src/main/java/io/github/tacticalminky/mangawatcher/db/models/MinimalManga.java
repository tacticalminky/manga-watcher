package io.github.tacticalminky.mangawatcher.db.models;

/**
 * The minimal manga model for library view
 *
 * @author Andrew Mink
 * @version Oct 1, 2023
 * @since 1.0.0-b.4
 */
public interface MinimalManga {
    String getSlug();

    String getTitle();

    String getUrl();

    String getImageUrl();
}
