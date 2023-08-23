import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

import { Manga } from './interfaces';
import { BackendApiService } from './backend-api.service';

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
