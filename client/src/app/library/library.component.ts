import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';

import { MinimalManga } from 'src/app/manga-models';
import { MangaCardComponent } from 'src/app/library/manga-card/manga-card.component';
import { BackendApiService } from 'src/app/services/backend-api.service';
import { UpdateLibraryService } from 'src/app/services/update-library.service';

@Component({
    selector: 'app-library',
    standalone: true,
    imports: [
        CommonModule,
        MangaCardComponent
    ],
    templateUrl: './library.component.html',
    styles: []
})
export class LibraryComponent implements OnInit {
    mangaList: MinimalManga[] = [];

    constructor(
        private apiService: BackendApiService,
        private updateLibraryService: UpdateLibraryService
    ) { }

    ngOnInit(): void {
        this.getManga();

        this.updateLibraryService.subscribe(() => this.getManga());
    }

    private getManga(): void {
        this.apiService.getAllManga().subscribe({
            next: (res: MinimalManga[]) => {
                this.mangaList = res;
            },
            error: (error: HttpErrorResponse) => {
                console.error(error);
                alert(error.message);
            }
        });
    }
}
