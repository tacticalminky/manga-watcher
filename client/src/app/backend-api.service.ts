import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Chapter, Manga, MinimalManga, NewManga } from './manga-models';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class BackendApiService {
    private apiServerUrl = environment.apiServerUrl;

    constructor(private http: HttpClient) { }

    /**     Manga API Calls         */
    public getAllManga(): Observable<MinimalManga[]> {
        return this.http.get<MinimalManga[]>(`${this.apiServerUrl}/api/manga`);
    }

    public addManga(manga: NewManga): Observable<Manga> {
        return this.http.post<Manga>(`${this.apiServerUrl}/api/manga`, manga);
    }

    public getMangaBySlug(slug: string): Observable<Manga> {
        return this.http.get<Manga>(`${this.apiServerUrl}/api/manga/${slug}`);
    }

    public updateManga(manga: Manga): Observable<Manga> {
        return this.http.put<Manga>(`${this.apiServerUrl}/api/manga`, manga);
    }

    public deleteManga(slug: string): Observable<JSON> {
        return this.http.delete<JSON>(`${this.apiServerUrl}/api/manga/${slug}`);
    }

    /**     Sync API Calls          */
    public syncAllManga(): Observable<JSON> {
        return this.http.get<JSON>(`${this.apiServerUrl}/sync`);
    }

    public syncMangaBySlug(slug: string): Observable<JSON> {
        return this.http.get<JSON>(`${this.apiServerUrl}/sync/${slug}`);
    }

    /**     Chapter API Calls       */
    public updateChapter(mangaSlug: string, chapter: Chapter): Observable<Chapter> {
        return this.http.put<Chapter>(`${this.apiServerUrl}/api/manga/${mangaSlug}/chapters`, chapter);
    }
}
