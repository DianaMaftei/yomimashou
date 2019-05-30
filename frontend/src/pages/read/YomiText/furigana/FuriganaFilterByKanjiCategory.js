import { JLPTlevels } from "./KanjiByCategory";

export const filterTextFuriganaByKanjiCategory = (text, category) => {
    const matchRubyTags = /(?=<ruby>)(.*?)(?:<\/ruby>)/g;
    const matchRubyKanji = /(?<=<ruby>)(.*?)(?=<rp>)/g;

    const allRubyTags = text.match(matchRubyTags);
    let interimText = text;

    allRubyTags.forEach(tag => {
        const matchedKanji = tag.match(matchRubyKanji)[0];
        const kanjiListByCategory = JLPTlevels[category];
        let containingKanji = 0;

        [...matchedKanji].forEach(kanji => {
            if (kanjiListByCategory.includes(kanji)) {
                containingKanji++;
            }
        });

        if (containingKanji === matchedKanji.length) {
            interimText = interimText.replace(tag, matchedKanji);
        }
    });

    return interimText;
};