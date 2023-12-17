package io.github.tacticalminky.mangawatcher.db.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import io.github.tacticalminky.mangawatcher.db.models.*;

/**
 * The repository for interacting with the manga collection in the database
 *
 * @author Andrew Mink
 * @version Oct 8, 2023
 * @since 1.0.0-b.4
 */
public interface MangaRepo extends MongoRepository<Manga, String> {
    final String minimalMangaFields = "{ slug: 1, title: 1, url: 1, imageUrl: 1 }";

    /**
     * @return a list of manga in the form: { slug, title, url, imageUrl }
     */
    @Query(value = "{ slug: { $regex: ?0 } }", fields = minimalMangaFields, sort = "{ title: 1 }")
    List<MinimalManga> findAllAsMinimalMangaByRegex(String regex);

    @Query("{ slug: ?0 }")
    Optional<Manga> findMangaBySlug(String slug);

    @DeleteQuery("{ slug: ?0 }")
    Optional<Manga> deleteMangaBySlug(String slug);

    @Query("{ slug: ?0 }")
    @Update("{ $addToSet: { chapters: { $each: [?1], $sort: { number: -1 }}}}")
    void addChapterBySlug(String slug, Chapter chapter);

    @Query("{ slug: ?0 }")
    @Update("{ $push: { chapters: { $each: ?1, $sort: { number: -1 }}}}")
    void addChaptersBySlug(String slug, List<Chapter> chapters);

    @Query("{ slug: ?0, 'chapters.slug': ?1 }")
    @Update("{ $set: { 'chapters.$.isRead': ?2 }}")
    void updateChapterIsReadBySlug(String slug, String chapterSlug, boolean isRead);
}
