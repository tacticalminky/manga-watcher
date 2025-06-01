import { EventEmitter, Injectable } from '@angular/core';
import { Subscription } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class UpdateLibraryService {
    private updateLibraryEmitter: EventEmitter<void> = new EventEmitter(true);

    constructor() { }

    emit(): void {
        this.updateLibraryEmitter.emit();
    }

    subscribe(next: () => void): Subscription {
        return this.updateLibraryEmitter.subscribe(next);
    }
}
