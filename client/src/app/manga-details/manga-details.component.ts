import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

import { Manga, Chapter } from '../interfaces';
import { BackendApiService } from '../backend-api.service';

@Component({
    selector: 'app-manga-details',
    templateUrl: './manga-details.component.html',
    styleUrls: ['./manga-details.component.css']
})
export class MangaDetailsComponent implements OnInit {
    manga: Manga | undefined;
    chapters: Chapter[] | undefined;

    constructor(
        private apiService: BackendApiService,
        private route: ActivatedRoute,
        private router: Router,
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
            },
            error: (error: HttpErrorResponse) => {
                alert(error.message);
            }
        });
    }
}
