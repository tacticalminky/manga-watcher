package io.github.tacticalminky.mangawatcher.db.models;

import java.io.Serializable;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.util.Assert;

/**
 * An abstract model for the Manga and Chapter database models.
 *
 * @author Andrew Mink
 * @version Oct 1, 2023
 * @since 1.0.0-b.4
 */
public class AbstractModel implements Serializable {
    @MongoId
    private String slug;

    @Indexed(unique = true)
    private String url;

    /**
     * Default class constructor
     */
    public AbstractModel() {
    }

    /**
     * Full class constructor
     *
     * @param url the url for the manga or chapter
     */
    public AbstractModel(String url) {
        Assert.hasText(url, "The url must be provided with text");

        this.url = url;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        Assert.isNull(this.slug, "You can not change the slug once it is set");

        this.slug = slug;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || this.slug == null || !(this.getClass().equals(obj.getClass()))) {
            return false;
        }

        AbstractModel other = (AbstractModel) obj;

        return this.slug.equals(other.slug);
    }

    @Override
    public int hashCode() {
        return (slug != null) ? slug.hashCode() : 0;
    }
}
