import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

import { SyncService } from '../sync.service';

@Component({
    selector: 'app-top-bar',
    standalone: true,
    imports: [RouterLink],
    templateUrl: './top-bar.component.html',
    styles: []
})
export class TopBarComponent {

    constructor(private syncService: SyncService) {}

    onSyncAllClick(): void {
        this.syncService.syncAllManga();
    }
}
