import { Component, OnInit } from '@angular/core';
import { NgIf, NgFor } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

import { Manga, Chapter } from '../manga-models';
import { BackendApiService } from '../backend-api.service';

@Component({
    selector: 'app-manga-view',
    standalone: true,
    imports: [NgIf, NgFor],
    templateUrl: './manga-view.component.html',
    styles: []
})
export class MangaViewComponent implements OnInit {
    manga!: Manga;

    markRead: boolean = false;
    syncing: boolean = false;
    countToUpdate: number = 0;

    constructor(
        private apiService: BackendApiService,
        private route: ActivatedRoute,
        private router: Router
    ) { }

    ngOnInit(): void {
        const routeParams = this.route.snapshot.paramMap;
        const mangaSlug = String(routeParams.get('mangaSlug'));

        this.getManga(mangaSlug);
    }

    private getManga(slug: string): void {
        this.apiService.getMangaBySlug(slug).subscribe({
            next: (res: Manga) => {
                this.manga = res;

                this.markRead = this.manga.chapters.find(chapter => {
                    return !chapter.isRead;
                }) !== undefined;
            },
            error: (error: HttpErrorResponse) => {
                alert(error.message)
                this.router.navigate(['/404']);
            }
        });
    }

    onSyncMangaClick(): void {
        this.syncing = true;
        this.apiService.syncMangaBySlug(this.manga.slug).subscribe({
            next: () => {
                this.syncing = false;
            },
            error: (error: HttpErrorResponse) => {
                alert(error.message);
            }
        });
    }

    onChapterClick(chapter: Chapter): void {
        if (!chapter.isRead) {
            chapter.isRead = true;
            this.apiService.updateChapter(this.manga.slug, chapter).subscribe({
                error: (error: HttpErrorResponse) => {
                    alert(error.message);
                }
            });
        }
    }

    onMarkAllReadClick(read: boolean): void {
        this.manga.chapters?.forEach(chapter => {
            if (chapter.isRead !== read) {
                this.countToUpdate += 1;
                chapter.isRead = read;
                this.apiService.updateChapter(this.manga.slug, chapter).subscribe({
                    next: (res: Chapter) => {
                        this.countToUpdate -= 1;
                    }
                });
            }
        });
        this.markRead = !read;
    }
}
