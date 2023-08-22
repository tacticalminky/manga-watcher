import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddMangaFormComponent } from './add-manga-form.component';

describe('AddMangaFormComponent', () => {
    let component: AddMangaFormComponent;
    let fixture: ComponentFixture<AddMangaFormComponent>;

    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [AddMangaFormComponent]
        });
        fixture = TestBed.createComponent(AddMangaFormComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
