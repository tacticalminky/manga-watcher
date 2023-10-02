import { Component } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';

import { BackendApiService } from '../backend-api.service';
import { NewManga } from '../manga-models';

@Component({
    selector: 'app-add-manga-form',
    standalone: true,
    imports: [ReactiveFormsModule],
    templateUrl: './add-manga-form.component.html',
    styles: []
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
        const manga: NewManga = {
            title: String(this.addMangaForm.value.title),
            url: String(this.addMangaForm.value.url)
        };
        this.addManga(manga);
        this.addMangaForm.reset();
    }

    private addManga(manga: NewManga): void {
        this.apiService.addManga(manga).subscribe({
            error: (error: HttpErrorResponse) => {
                alert(error.message);
            }
        });
    }
}
