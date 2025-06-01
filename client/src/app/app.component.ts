import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { TopBarComponent } from 'src/app/top-bar/top-bar.component';
import { AddMangaModalComponent } from 'src/app/add-manga-modal/add-manga-modal.component';

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
