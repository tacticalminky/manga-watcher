import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

import { MinimalManga } from 'src/app/manga-models';

@Component({
    selector: 'app-manga-card',
    standalone: true,
    imports: [
        CommonModule,
        RouterLink
    ],
    templateUrl: './manga-card.component.html',
    styles: []
})
export class MangaCardComponent {
    @Input({ required: true }) manga!: MinimalManga;
}
