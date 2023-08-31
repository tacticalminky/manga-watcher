import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Manga, Chapter } from './interfaces';

@Injectable({
    providedIn: 'root'
})
export class BackendApiService {
    // private apiServerUrl = 'http://localhost:8091';
    private apiServerUrl = `${window.location.origin}/api`;

    constructor(private http: HttpClient) { }

    /**     Manga API Calls         */
    public getAllManga(): Observable<Manga[]> {
        return this.http.get<Manga[]>(`${this.apiServerUrl}/manga`);
    }

    public addManga(manga: Manga): Observable<Manga> {
        return this.http.post<Manga>(`${this.apiServerUrl}/manga`, manga);
    }

    public getMangaBySlug(slug: string): Observable<Manga> {
        return this.http.get<Manga>(`${this.apiServerUrl}/manga/${slug}`);
    }

    public updateManga(manga: Manga): Observable<Manga> {
        return this.http.put<Manga>(`${this.apiServerUrl}/manga`, manga);
    }

    // public deleteMangaBySlug(slug: string): Observable<JSON> {
    //     return this.http.delete<JSON>(`${this.apiServerUrl}/manga/${slug}`);
    // }

    /**     Sync API Calls          */
    public syncAllManga(): Observable<JSON> {
        return this.http.get<JSON>(`${this.apiServerUrl}/sync`);
    }
    
    public syncMangaBySlug(slug: string): Observable<JSON> {
        return this.http.get<JSON>(`${this.apiServerUrl}/sync/${slug}`);
    }

    /**     Chapter API Calls       */
    public getChaptersByMangaSlug(mangaSlug: string): Observable<Chapter[]> {
        return this.http.get<Chapter[]>(`${this.apiServerUrl}/manga/${mangaSlug}/chapters`)
    }

    // public addChapter(mangaSlug: string, chapter: Chapter): Observable<Chapter> {
    //     return this.http.post<Chapter>(`${this.apiServerUrl}/manga/${mangaSlug}/chapters`, chapter);
    // }

    // public getChapterByMangaAndChapterSlug(mangaSlug: string, chapterSlug: string): Observable<Chapter> {
    //     return this.http.get<Chapter>(`${this.apiServerUrl}/manga/${mangaSlug}/chapters${chapterSlug}`)
    // }

    public updateChapter(mangaSlug: string, chapter: Chapter): Observable<Chapter> {
        return this.http.put<Chapter>(`${this.apiServerUrl}/manga/${mangaSlug}/chapters`, chapter);
    }

    // public deleteChapterByMangaAndChapterSlug(mangaSlug: string, chapterSlug: string): Observable<JSON> {
    //     return this.http.delete<JSON>(`${this.apiServerUrl}/manga/${mangaSlug}/chapters${chapterSlug}`);
    // }
}
