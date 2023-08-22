import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

import { Manga } from '../interfaces';
import { BackendApiService } from '../backend-api.service';

@Component({
    selector: 'app-library',
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
                console.log(res);
                this.mangas = res;
            },
            error: (error: HttpErrorResponse) => {
                console.error(error);
                alert(error.message);
            }
        })
    }
}
