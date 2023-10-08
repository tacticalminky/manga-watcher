import { Routes } from "@angular/router";

export const routes: Routes = [
    {
        path: '',
        loadComponent: () => import('./library/library.component').then(mod => mod.LibraryComponent)
    },
    {
        path: 'manga/:mangaSlug',
        loadComponent: () => import('./manga-details/manga-details.component').then(mod => mod.MangaDetailsComponent)
    },
    {
        path: '**',
        loadComponent: () => import('./page-not-found/page-not-found.component').then(mod => mod.PageNotFoundComponent)
    }
];
