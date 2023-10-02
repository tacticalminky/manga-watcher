// TODO: update

// Interface for new manga
export interface NewManga {
    title: string;
    url: string;
}

// Interface for manga in the library view
export interface MinimalManga extends NewManga {
    slug: string;
    imageUrl: string;
}

// Interface for manga in the detail and editing views
export interface FullManga extends MinimalManga {
    description: string;
    isMonitored: boolean;
    isDescriptionLocked: boolean;
    isImageUrlLocked: boolean;

    chapters: Chapter[];
}

// Interface for chapters
// export interface Chapter {
//     slug: string;
//     number: number;
//     url: string;
//     isRead: boolean;
// }

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
