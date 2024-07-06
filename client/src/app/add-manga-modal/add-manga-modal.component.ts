import { Component } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';

import { BackendApiService } from '../backend-api.service';
import { Manga, NewManga } from '../manga-models';
import { NgIf } from '@angular/common';

@Component({
    selector: 'app-add-manga-modal',
    standalone: true,
    imports: [NgIf, ReactiveFormsModule],
    templateUrl: './add-manga-modal.component.html',
    styles: []
})
export class AddMangaModalComponent {
    addingManga: boolean = false;

    addMangaForm = this.formBuilder.group({
        title: '',
        url: ''
    });

    constructor(
        private apiService: BackendApiService,
        private formBuilder: FormBuilder
    ) { }

    onSubmit(): void {
        // validate title and url
        if (this.addMangaForm.value.title === '') {
            this.createAlert('Title field must not be empty', 'danger');
            return;
        }

        if (this.addMangaForm.value.url === '') {
            this.createAlert('URL field must not be empty', 'danger');
            return;
        }

        // create manga
        const manga: NewManga = {
            title: String(this.addMangaForm.value.title),
            url: String(this.addMangaForm.value.url)
        };

        this.addManga(manga);
    }

    private createAlert(message: string, type: string): void {
        const wrapper = document.createElement('div');
        wrapper.innerHTML = [
            `<div class="alert alert-${type} alert-dismissible" role="alert">`,
            `   <div>${message}</div>`,
            '   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>',
            '</div>'
        ].join('')

        document.getElementById('liveAlertPlaceholder')!.append(wrapper);
    }

    private addManga(manga: NewManga): void {
        this.addingManga = true;
        this.apiService.addManga(manga).subscribe({
            next: (_: Manga) => {
                this.createAlert('Successfully added manga!', 'success');
                this.addMangaForm.reset();
                this.addingManga = false;
            },
            error: (error: HttpErrorResponse) => {
                this.createAlert(error.error, 'danger');
                this.addingManga = false;
            }
        });
    }
}
