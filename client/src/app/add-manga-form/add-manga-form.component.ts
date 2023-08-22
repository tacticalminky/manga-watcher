import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';

import { BackendApiService } from '../backend-api.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Manga } from '../interfaces';

@Component({
    selector: 'app-add-manga-form',
    templateUrl: './add-manga-form.component.html',
    styleUrls: ['./add-manga-form.component.css']
})
export class AddMangaFormComponent {

    addMangaForm = this.formBuilder.group({
        title: '',
        url: ''
    });

    constructor(
        private apiService: BackendApiService,
        private formBuilder: FormBuilder
    ) { }

    onSubmit(): void {
        const manga: Manga = {
            title: String(this.addMangaForm.value.title),
            slug: String(null),
            url: String(this.addMangaForm.value.url),
            description: null,
            imageUrl: null
        }
        this.addManga(manga);
        this.addMangaForm.reset();
    }

    private addManga(manga: Manga): void {
        this.apiService.addManga(manga).subscribe({
            next: (res: Manga) => {
                this.syncManga(res);
            },
            error: (error: HttpErrorResponse) => {
                alert(error.message);
            }
        });
    }

    private syncManga(manga: Manga): void {
        this.apiService.syncMangaBySlug(manga.slug).subscribe({
            error: (error: HttpErrorResponse) => {
                alert(error.message);
            }
        });
    }
}
