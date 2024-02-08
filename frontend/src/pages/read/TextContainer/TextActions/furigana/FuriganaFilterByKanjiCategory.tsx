import { JLPTlevels } from './KanjiByCategory';


export const filterTextFuriganaByKanjiCategory = (text, kanjiLevels) => {
    const matchRubyTags = /(?=<ruby>)(.*?)(?:<\/ruby>)/g;
    const matchRubyKanji = /(?<=<ruby>)(.*?)(?=<rp>)/g;

    const allRubyTags = text.match(matchRubyTags);

    let interimText = text;

    if(!allRubyTags) return;

    const kanjiListByCategory = Object.keys(kanjiLevels).map(key => {
        if (kanjiLevels[key] === true) {
            return JLPTlevels[key]
        }
        return "";
    }).join("");

    allRubyTags.forEach(tag => {
        const matchedKanji = tag.match(matchRubyKanji)[0];

        let containingKanji = 0;

        [...matchedKanji].forEach(kanji => {
            if (!kanjiListByCategory.includes(kanji)) {
                containingKanji++;
            }
        });

        if (containingKanji === matchedKanji.length) {
            interimText = interimText.replace(tag, matchedKanji);
        }
    });

    return interimText;
};
