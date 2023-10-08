import { Component } from '@angular/core';

import { RouterOutlet } from '@angular/router';

import { TopBarComponent } from './top-bar/top-bar.component';
import { AddMangaModalComponent } from './add-manga-modal/add-manga-modal.component';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [
        RouterOutlet,
        TopBarComponent,
        AddMangaModalComponent
    ],
    templateUrl: './app.component.html',
    styles: []
})
export class AppComponent {
}
