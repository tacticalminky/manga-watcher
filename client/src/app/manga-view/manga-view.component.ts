import { Component, OnInit } from '@angular/core';
import { NgIf, NgFor } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { Manga, Chapter } from '../manga-models';
import { BackendApiService } from '../backend-api.service';

@Component({
    selector: 'app-manga-view',
    standalone: true,
    imports: [NgIf, NgFor, ReactiveFormsModule ],
    templateUrl: './manga-view.component.html',
    styles: []
})
export class MangaViewComponent implements OnInit {
    manga!: Manga;

    markRead: boolean = false;
    syncing: boolean = false;
    selecting: boolean = false;
    markSelectedRead: boolean = false;
    countToUpdate: number = 0;

    selectingForm: FormGroup = this.formBuilder.group({});

    constructor(
        private apiService: BackendApiService,
        private formBuilder: FormBuilder,
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

                this.manga.chapters.forEach(chapter => {
                    this.selectingForm.addControl(chapter.slug, new FormControl(null));
                });
            },
            error: (error: HttpErrorResponse) => {
                alert(error.message)
                this.router.navigate(['/404']);
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

    toggleSelectChapters(): void {
        this.selecting = !this.selecting;
        this.selectingForm.reset();
        this.markSelectedRead = false;
    }

    updateMarkSelectRead(isRead: boolean): void {
        if (!this.markSelectedRead && !isRead) {
            this.markSelectedRead = true;
        }
    }

    onMarkSelectedSubmit(): void {
        const selected_chapters: Chapter[] = this.manga.chapters.filter(chapter => this.selectingForm.get(chapter.slug)?.value === true);

        selected_chapters.forEach(chapter => {
            if (chapter.isRead !== this.markSelectedRead) {
                this.markChapter(chapter, this.markSelectedRead);
            }
        });

        this.toggleSelectChapters();
    }

    onSyncMangaClick(): void {
        this.syncing = true;
        this.apiService.syncMangaBySlug(this.manga.slug).subscribe({
            next: () => {
                this.syncing = false;
                this.getManga(this.manga.slug);
            },
            error: (error: HttpErrorResponse) => {
                alert(error.message);
            }
        });
    }

    onMarkAllReadClick(read: boolean): void {
        this.manga.chapters.forEach(chapter => {
            if (chapter.isRead !== read) {
                this.markChapter(chapter, read);
            }
        });
        this.markRead = !read;
    }

    private markChapter(chapter: Chapter, isRead: boolean): void {
        this.countToUpdate += 1;
        chapter.isRead = isRead;
        this.apiService.updateChapter(this.manga.slug, chapter).subscribe({
            next: () => {
                this.countToUpdate -= 1;

                if (this.countToUpdate === 0) {
                    this.getManga(this.manga.slug);
                }
            },
            error: (error: HttpErrorResponse) => {
                alert(error.message);
            }
        });
    }
}
