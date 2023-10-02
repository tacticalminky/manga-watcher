package io.github.tacticalminky.mangawatcher.db.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import io.github.tacticalminky.mangawatcher.db.models.*;

/**
 * The repository for interacting with the manga collection in the database
 *
 * @author Andrew Mink
 * @version Oct 1, 2023
 * @since 1.0.0-b.4
 */
public interface MangaRepo extends MongoRepository<Manga, String> {
    final String minimalMangaFields = "{ slug: 1, title: 1, url: 1, imageUrl: 1 }";

    /**
     * @return a list of manga in the form: { slug, title, url, imageUrl }
     */
    @Query(value = "{ slug: { $regex: ?0 } }", fields = minimalMangaFields, sort = "{ title: 1 }")
    List<MinimalManga> findAllAsMinimalMangaByRegex(String regex);

    @Query(value = "{ slug: ?0 }")
    Optional<Manga> findMangaBySlug(String slug);

    @Query(value = "{ slug: ?0 }", delete = true)
    Optional<Manga> deleteMangaBySlug(String slug);

}
