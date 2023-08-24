import { Component } from '@angular/core';

import { SyncService } from '../sync.service';

@Component({
    selector: 'app-top-bar',
    templateUrl: './top-bar.component.html',
    styleUrls: ['./top-bar.component.css']
})
export class TopBarComponent {
    
    constructor(private syncService: SyncService) {}

    onSyncAllClick(): void {
        this.syncService.syncAllManga();
    }
}
