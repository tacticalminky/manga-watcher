import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';

import { Manga } from 'src/app/manga-models';
import { BackendApiService } from 'src/app/services/backend-api.service';

@Component({
    selector: 'app-edit-manga-modal',
    standalone: true,
    imports: [ReactiveFormsModule],
    templateUrl: './edit-manga-modal.component.html'
})
export class EditMangaModalComponent {
    // TODO: add manga input

    updatingManga: boolean = false;

    updateMangaForm = this.formBuilder.group({});

    constructor(
        private formBuilder: FormBuilder,
        private apiService: BackendApiService
    ) { }

    onSubmit(): void {
        // validate inputs

        // update manga
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

    private updateManga(manga: Manga): void {
        this.updatingManga = true;
        this.apiService.updateManga(manga).subscribe({
            next: (_: Manga) => {
                this.createAlert('Successfully updated manga!', 'success');
                this.updateMangaForm.reset();
                this.updatingManga = false;
            },
            error: (error: HttpErrorResponse) => {
                this.createAlert(error.error, 'danger');
                this.updatingManga = false;
            }
        })
    }
}
