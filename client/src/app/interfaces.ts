export interface Manga {
    title: string;
    slug: string;
    url: string;
    description?: string | null;
    imageUrl?: string | null;
    isMonitored?: boolean | null;
    isDescriptionLocked?: boolean | null;
    isImageUrlLocked?: boolean | null;
}

export interface Chapter {
    id: string;
    mangaSlug: string;
    slug: string;
    number: number;
    name: string | null;
    url: string;
    isRead: boolean | null;
}
