package com.example.mangawatcher.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mangawatcher.db_models.Manga;
import com.example.mangawatcher.db_repos.MangaRepo;
import com.example.mangawatcher.exceptions.MangaNotFoundException;
import com.example.mangawatcher.exceptions.MangaUpdateException;
import com.mongodb.MongoWriteException;

/**
 * 
 * @author Andrew Mink
 * @version Aug 19, 2023
 * @since 1.0
 */
@Service
public class MangaService {
    @Autowired
    private MangaRepo mangaRepo;

    public List<Manga> findAllManga() {
        return mangaRepo.findAll();
    }

    public Manga addManga(Manga manga) throws MongoWriteException {
        String slug = manga.getTitle().toLowerCase();
        slug = slug.replace(' ', '-');
        manga.setSlug(slug);
        
        return mangaRepo.save(manga);
    }

    public Manga findMangaBySlug(String slug) throws MangaNotFoundException {
        return mangaRepo.findMangaBySlug(slug)
            .orElseThrow(() -> new MangaNotFoundException(slug));
    }

    public Manga updateManga(Manga manga) throws MangaNotFoundException, MangaUpdateException {
        Manga oldManga = findMangaBySlug(manga.getSlug());
        
        if (manga.getUrl() != oldManga.getUrl()) {
            String message = "Can't change the manga's url";
            throw new MangaUpdateException(manga.getSlug(), message);
        }

        return mangaRepo.save(manga);
    }

    public void deleteMangaBySlug(String slug) throws MangaNotFoundException {
        mangaRepo.deleteMangaBySlug(slug)
            .orElseThrow(() -> new MangaNotFoundException(slug));
    }

}
