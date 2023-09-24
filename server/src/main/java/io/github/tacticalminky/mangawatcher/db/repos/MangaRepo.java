package io.github.tacticalminky.mangawatcher.db.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import io.github.tacticalminky.mangawatcher.db.models.Chapter;
import io.github.tacticalminky.mangawatcher.db.models.Manga;

/**
 *
 * @author Andrew Mink
 * @version Aug 19, 2023
 * @since 1.0
 */
public interface MangaRepo extends MongoRepository<Manga, String> {
    final String commonMangaFields = "{ slug: 1, title: 1, url: 1, description: 1, image_rl: 1, is_monitored: 1, is_description_locked: 1, is_image_url_locked: 1 }";
    final String commonChapterFields = "{ chapters.slug: 1,  chapters.name: 1, chapters.url: 1, is_read: 1 }";

    @Query(value = "{}", fields = commonMangaFields, sort = "{ title: 1 }")
    List<Manga> findAllMangas();

    @Query(value = "{ slug: ?0 }")
    Optional<Manga> findMangaBySlug(String slug);

    @Query(value = "{ slug: ?0 }", fields = commonMangaFields)
    Optional<Manga> findMinimalMangaBySlug(String slug);

    @Query(value = "{ slug: ?0 }", delete = true)
    Optional<Manga> deleteMangaBySlug(String slug);

    @Query(value = "{ slug: ?0 }", fields = commonChapterFields, sort = "{ chapter.number: -1 }")
    List<Chapter> findAllChaptersByMangaSlug(String slug);

    @Query(value = "{ slug: ?0, chapters.slug: ?1 }", fields = commonChapterFields)
    Optional<Chapter> findChapterByMangaAndChapterSlug(String slug, String chapterSlug);

    @Query(value = "{ slug: ?0 }", delete = true)
    List<Chapter> deleteAllChaptersByMangaSlug(String slug);

    @Query(value = "{slug: ?0, chapters.slug: ?1 }", delete = true)
    Optional<Chapter> deleteChapterByMangaAndChapterSlug(String slug, String chapterSlug);

}
