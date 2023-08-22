import { Injectable } from '@angular/core';

import { Manga } from './interfaces';
import { BackendApiService } from './backend-api.service';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class SyncService {
    private proccessing: boolean = false;

    constructor(private apiService: BackendApiService) { }

    public syncAllManga(): void {
        if (!this.proccessing) {
            this.proccessing = true;
            this.apiService.syncAllManga().subscribe({
                next: (res: JSON) => {
                    this.proccessing = false;
                },
                error: (error: HttpErrorResponse) => {}
            });
        }
    }

    public syncManga(manga: Manga): void {
        if (!this.proccessing) {
            this.proccessing = true;
            this.apiService.syncMangaBySlug(manga.slug).subscribe({
                next: (res: JSON) => {
                    this.proccessing = false;
                },
                error: (error: HttpErrorResponse) => {}
            });
        }
    }

}
