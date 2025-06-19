import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import { Router } from '@angular/router';

import { Manga } from 'src/app/manga-models';
import { BackendApiService } from 'src/app/services/backend-api.service';

@Component({
    selector: 'app-edit-manga-modal',
    standalone: true,
    imports: [
        NgIf,
        ReactiveFormsModule
    ],
    templateUrl: './edit-manga-modal.component.html'
})
export class EditMangaModalComponent implements OnInit {
    @Input({ required: true }) manga!: Manga;

    @Output() updated = new EventEmitter();

    updatingManga: boolean = false;
    deletingManga: boolean = false;

    updateMangaForm = this.formBuilder.group({
        title: '',
        url: '',
        imageUrl: '',
        description: '',
        isMonitored: false,
        isDescriptionLocked: false,
        isImageUrlLocked: false,
    });

    constructor(
        private router: Router,
        private formBuilder: FormBuilder,
        private apiService: BackendApiService
    ) { }

    ngOnInit(): void {
        this.resetForm();
    }

    resetForm(): void {
        this.updateMangaForm.setValue({
            title: this.manga.title,
            url: this.manga.url,
            imageUrl: this.manga.imageUrl,
            description: this.manga.description,
            isMonitored: this.manga.isMonitored,
            isDescriptionLocked: this.manga.isDescriptionLocked,
            isImageUrlLocked: this.manga.isImageUrlLocked
        });
    }

    onSubmit(): void {
        // validate inputs
        if (this.updateMangaForm.value.title === '') {
            this.createAlert('Title field must not be empty', 'danger');
            return;
        }

        if (this.updateMangaForm.value.url === '') {
            this.createAlert('URL field must not be empty', 'danger');
            return;
        }

        // update manga
        const updatedManga: Manga = {
            slug: this.manga.slug,
            title: String(this.updateMangaForm.value.title),
            url: String(this.updateMangaForm.value.url),
            imageUrl: String(this.updateMangaForm.value.imageUrl),
            description: String(this.updateMangaForm.value.description),
            isMonitored: Boolean(this.updateMangaForm.value.isMonitored),
            isDescriptionLocked: Boolean(this.updateMangaForm.value.isDescriptionLocked),
            isImageUrlLocked: Boolean(this.updateMangaForm.value.isImageUrlLocked),

            chapters: this.manga.chapters
        }

        this.updateManga(updatedManga);
    }

    private createAlert(message: string, type: string): void {
        const wrapper = document.createElement('div');
        wrapper.innerHTML = [
            `<div class="alert alert-${type} alert-dismissible" role="alert">`,
            `   <div>${message}</div>`,
            '   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>',
            '</div>'
        ].join('');

        document.getElementById('liveAlertPlaceholder')!.append(wrapper);
    }

    private updateManga(manga: Manga): void {
        this.updatingManga = true;
        this.apiService.updateManga(manga).subscribe({
            next: (_: Manga) => {
                this.createAlert('Successfully updated manga!', 'success');
                this.updatingManga = false;

                this.updated.emit()
            },
            error: (error: HttpErrorResponse) => {
                this.createAlert(error.error, 'danger');
                this.updatingManga = false;
            }
        })
    }

    deleteManga(): void {
        this.deletingManga = true;
        this.apiService.deleteManga(this.manga.slug).subscribe({
            next: () => {
                this.createAlert('Successfully deleted manga!', 'success');
                this.deletingManga = false;

                document.getElementById('confirmDeleteModalClose')?.click();

                this.router.navigate(['/']);
            },
            error: (error: HttpErrorResponse) => {
                this.createAlert(error.error, 'danger');
                this.deletingManga = false;
            }
        });
    }
}
