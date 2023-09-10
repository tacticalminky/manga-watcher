package io.github.tacticalminky.mangawatcher.db.repos;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import io.github.tacticalminky.mangawatcher.db.models.Manga;

/**
 * 
 * @author Andrew Mink
 * @version Aug 19, 2023
 * @since 1.0
 */
public interface MangaRepo extends MongoRepository<Manga, String> {

    @Query("{slug: '?0'}")
    Optional<Manga> findMangaBySlug(String slug);

    @Query(value = "{slug: '?0'}", delete = true)
    Optional<Manga> deleteMangaBySlug(String slug);

}