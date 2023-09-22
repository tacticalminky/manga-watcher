import { Component, OnInit } from '@angular/core';
import { NgIf, NgFor } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

import { Manga, Chapter } from '../interfaces';
import { BackendApiService } from '../backend-api.service';
import { SyncService } from '../sync.service';

@Component({
    selector: 'app-manga-details',
    standalone: true,
    imports: [NgIf, NgFor],
    templateUrl: './manga-details.component.html',
    styleUrls: ['./manga-details.component.css']
})
export class MangaDetailsComponent implements OnInit {
    manga: Manga | undefined;
    chapters: Chapter[] | undefined;
    markRead: boolean = false;
    countToUpdate: number = 0;

    constructor(
        private apiService: BackendApiService,
        private syncService: SyncService,
        private route: ActivatedRoute,
        private router: Router
    ) { }

    ngOnInit(): void {
        const routeParams = this.route.snapshot.paramMap;
        const mangaSlug = String(routeParams.get('mangaSlug'));

        this.getManga(mangaSlug);
        this.getChapters(mangaSlug);
    }

    private getManga(slug: string): void {
        this.apiService.getMangaBySlug(slug).subscribe({
            next: (res: Manga) => {
                this.manga = res;
            },
            error: (error: HttpErrorResponse) => {
                alert(error.message)
                this.router.navigate(['/404']);
            }
        });
    }

    private getChapters(mangaSlug: string): void {
        this.apiService.getChaptersByMangaSlug(mangaSlug).subscribe({
            next: (res: Chapter[]) => {
                this.chapters = res;
                this.markRead = res.find(chapter => {
                    return !chapter.isRead;
                }) !== undefined;
            },
            error: (error: HttpErrorResponse) => {
                alert(error.message);
            }
        });
    }

    onSyncMangaClick(): void {
        this.syncService.syncManga(this.manga!);
    }

    onChapterClick(chapter: Chapter): void {
        if (!chapter.isRead) {
            chapter.isRead = true;
            this.apiService.updateChapter(chapter.mangaSlug, chapter).subscribe({
                error: (error: HttpErrorResponse) => {
                    alert(error.message);
                }
            });
        }
    }

    onMarkAllReadClick(read: boolean): void {
        this.chapters?.forEach(chapter => {
            if (chapter.isRead !== read) {
                this.countToUpdate += 1;
                chapter.isRead = read;
                this.apiService.updateChapter(chapter.mangaSlug, chapter).subscribe({
                    next: (res: Chapter) => {
                        this.countToUpdate -= 1;

                    }
                });
            }
        });
        this.markRead = !read;
    }
}
