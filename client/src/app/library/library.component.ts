import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { RouterLink } from '@angular/router';

import { Manga } from '../interfaces';
import { BackendApiService } from '../backend-api.service';

@Component({
    selector: 'app-library',
    standalone: true,
    imports: [CommonModule, RouterLink],
    templateUrl: './library.component.html',
    styleUrls: ['./library.component.css']
})
export class LibraryComponent implements OnInit {
    mangas: Manga[] | undefined;

    constructor(private apiService: BackendApiService) { }

    ngOnInit(): void {
        this.getManga();
    }

    private getManga(): void {
        this.apiService.getAllManga().subscribe({
            next: (res: Manga[]) => {
                this.mangas = res;
                this.mangas.sort((m1, m2) => {
                    if (m1.title > m2.title) return 1;
                    if (m1.title < m2.title) return -1;
                    return 0;
                });
            },
            error: (error: HttpErrorResponse) => {
                console.error(error);
                alert(error.message);
            }
        })
    }
}
