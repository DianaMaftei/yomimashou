import { User } from './User';


export type Text = {
    id: number;
    title: string;
    content: string;
    excerpt: string;
    imageFileName: string;
    tags: string[];
    kanjiCountByLevel: Map<string, number>;
    characterCount: number;
    creationDate: Date;
    createdBy: User;
    parsedWords: Map<string, string>;
    parsedKanji: string;
};
