import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LibraryComponent } from './library/library.component';
import { MangaDetailsComponent } from './manga-details/manga-details.component';
import { AddMangaFormComponent } from './add-manga-form/add-manga-form.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';


const routes: Routes = [
    { path: '', component: LibraryComponent },
    { path: 'add-manga', component: AddMangaFormComponent },
    { path: 'manga/:mangaSlug', component: MangaDetailsComponent },
    { path: '**', component: PageNotFoundComponent }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
