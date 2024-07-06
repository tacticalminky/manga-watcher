import { EventEmitter, Injectable } from '@angular/core';
import { Subscription } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AddMangaService {
    private addedMangaEmitter: EventEmitter<void> = new EventEmitter(true);

    constructor() { }

    emit(): void {
        this.addedMangaEmitter.emit();
    }

    subscribe(next: () => void): Subscription {
        return this.addedMangaEmitter.subscribe(next);
    }
}
