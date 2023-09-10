package io.github.tacticalminky.mangawatcher.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mongodb.MongoWriteException;
import io.github.tacticalminky.mangawatcher.exceptions.MangaNotFoundException;
import io.github.tacticalminky.mangawatcher.exceptions.MangaWriteException;
import io.github.tacticalminky.mangawatcher.db.models.Manga;
import io.github.tacticalminky.mangawatcher.db.repos.MangaRepo;

/**
 * 
 * @author Andrew Mink
 * @version Aug 24, 2023
 * @since 1.0
 */
@Service
public class MangaService {
    @Autowired
    private MangaRepo mangaRepo;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private SyncService syncService;

    public List<Manga> getAllManga() {
        return mangaRepo.findAll();
    }

    public Manga addManga(Manga manga) throws MongoWriteException {
        String slug = manga.getTitle().toLowerCase();
        slug = slug.replace(' ', '-');
        manga.setSlug(slug);

        try {
            findMangaBySlug(manga.getSlug());
            String message = "Manga already exists, can't create new";
            throw new MangaWriteException(manga.getSlug(), message);
        } catch (MangaNotFoundException ex) {
            Manga createdManga = mangaRepo.save(manga);
            syncService.syncManga(manga);
            return createdManga;
        }
    }

    public Manga findMangaBySlug(String slug) throws MangaNotFoundException {
        return mangaRepo.findMangaBySlug(slug)
            .orElseThrow(() -> new MangaNotFoundException(slug));
    }

    public Manga updateManga(Manga manga) throws MangaNotFoundException, MangaWriteException {
        Manga oldManga = findMangaBySlug(manga.getSlug());
        
        if (!manga.getUrl().equals(oldManga.getUrl())) {
            String message = "Can't change the manga's url";
            throw new MangaWriteException(manga.getSlug(), message);
        }

        return mangaRepo.save(manga);
    }

    public void deleteMangaBySlug(String slug) throws MangaNotFoundException {
        mangaRepo.deleteMangaBySlug(slug)
            .orElseThrow(() -> new MangaNotFoundException(slug));
        chapterService.deleteAllChaptersByMangaSlug(slug);
    }

}
