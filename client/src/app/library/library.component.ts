import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';

import { MinimalManga } from '../manga-models';
import { BackendApiService } from '../backend-api.service';
import { MangaCardComponent } from './manga-card/manga-card.component';

@Component({
    selector: 'app-library',
    standalone: true,
    imports: [
        CommonModule,
        MangaCardComponent
    ],
    templateUrl: './library.component.html',
    styles: [],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class LibraryComponent implements OnInit {
    mangaList?: MinimalManga[];

    constructor(private apiService: BackendApiService) { }

    ngOnInit(): void {
        this.getManga();
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
