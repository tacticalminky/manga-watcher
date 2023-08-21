package com.example.mangawatcher.db_repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.mangawatcher.db_models.Chapter;

/**
 * 
 * @author Andrew Mink
 * @version Aug 19, 2023
 * @since 1.0
 */
public interface ChapterRepo extends MongoRepository<Chapter, String> {

    @Query("{manga_slug: '?0'}")
    List<Chapter> findChaptersByMangaSlug(String mangaSlug);

    @Query("{manga_slug: '?0', slug: '?1'}")
    Optional<Chapter> findChapterByMangaAndChapterSlug(String mangaSlug, String slug);

    @Query(value = "{manga_slug: '?0', slug: '?1'}", delete = true)
    Optional<Chapter> deleteChapterByMangaAndChapterSlug(String mangaSlug, String slug);

}
