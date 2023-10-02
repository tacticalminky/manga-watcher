// Interface for new manga
export interface NewManga {
    title: string;
    url: string;
}

// Interface for manga in the library view
export interface MinimalManga extends NewManga {
    slug: string;
    imageUrl: string | null;
}

// Interface for manga in the detail and editing views
export interface Manga extends MinimalManga {
    description: string;
    isMonitored: boolean;
    isDescriptionLocked: boolean;
    isImageUrlLocked: boolean;

    chapters: Chapter[];
}

// Interface for chapters
export interface Chapter {
    slug: string;
    number: number;
    url: string;
    isRead: boolean;
}
