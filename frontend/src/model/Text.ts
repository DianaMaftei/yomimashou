export type Text = {
    id: number;
    title: string;
    content: string;
    excerpt: string;
    imageFileName: string;
    tags: string[];
    kanjiCountByLevel: Map<string, number>;
    characterCount: number
};
