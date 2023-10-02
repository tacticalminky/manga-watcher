import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { RouterLink } from '@angular/router';

import { MinimalManga } from '../manga-models';
import { BackendApiService } from '../backend-api.service';

@Component({
    selector: 'app-library',
    standalone: true,
    imports: [CommonModule, RouterLink],
    templateUrl: './library.component.html',
    styleUrls: ['./library.component.css']
})
export class LibraryComponent implements OnInit {
    mangaList: MinimalManga[] | undefined;

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
