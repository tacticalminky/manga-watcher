import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

import { BackendApiService } from '../backend-api.service';
import { HttpErrorResponse } from '@angular/common/http';
import { NgIf } from '@angular/common';

@Component({
    selector: 'app-top-bar',
    standalone: true,
    imports: [
        RouterLink,
        NgIf
    ],
    templateUrl: './top-bar.component.html',
    styles: []
})
export class TopBarComponent {
    syncing: boolean = false;

    constructor(private apiService: BackendApiService) {}

    onSyncAllClick(): void {
        this.syncing = true;
        this.apiService.syncAllManga().subscribe({
            next: () => {
                this.syncing = false;
            },
            error: (error: HttpErrorResponse) => {
                alert(error.message);
            }
        });
    }
}
