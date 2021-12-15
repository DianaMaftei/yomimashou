import React from 'react';
import colors from "../../../../../style/colorConstants"
import AddToDeckButton from "../../../../../components/buttons/addToDeckBtn/AddToDeckButton";
import TTS from "../../../../../components/tts/TTS";
import PopupType from "../PopupType";
import RikaiDict from "../RikaiDictionary";
import SearchType from "../SearchType";

const getMeaning = (meanings) => {
    let meaningString = [...new Set(meanings.flat())].join(", ")
    if (meaningString.length > 80) {
        let substring = meaningString.substr(0, 80);
        let lastIndexOfComma = substring.lastIndexOf(",");
        substring = substring.substr(0, lastIndexOfComma);
        return <span>{substring} <span className="carrot-down">&#9660;</span></span>
    }
    return meaningString;
}

const getHighlightedSentence = (sentence, word) => {
    const keyWords = [...word.kanji, ...word.kana];
    let newSentence = sentence;
    keyWords.forEach(word=> {
        newSentence = newSentence.replace(word, '<span style="color:placeholderColor; font-weight: 500">placeholder</span>');
        newSentence = newSentence.replace('placeholderColor', colors.yomiLightRed);
        newSentence = newSentence.replace('placeholder', word);
    })
    return newSentence;
}

const handleKanjiClick = (changePopup,fetchKanji, kanji) => {
    fetchKanji([...kanji], SearchType.KANJI);
    changePopup(PopupType.KANJI)
}

const RikaiWord = ({style, result, wordExamples, changePopup, fetchKanji}: RikaiWordProps) => {

    return (
        <div id="rikai-window" style={style} className="elevation-lg">
            <div className="rikai-display container">
                <span className="rikai-word-tts">
                    <TTS text={result.kanji.length > 0 ? result.kanji[0] : result.kana[0]}/>
                </span>
                <div className="rikai-word-kanji">
                    {
                        result.kanji.join(", ").split("").map((currentChar, index) => {
                            if (RikaiDict.isKanji(currentChar)) {
                                return <span key={currentChar + index} style={{color:colors.yomiLightRed}} onClick={() => handleKanjiClick(changePopup, fetchKanji, currentChar)}>{currentChar}</span>
                            } else {
                                return currentChar;
                            }
                        })
                    }
                </div>
                <span onClick={() => changePopup(PopupType.ADD)}>
                    <AddToDeckButton/>
                </span>
                <div className="rikai-word-kana">{result.kana.join(", ")}</div>

                <div className="rikai-word-grammar">{[...new Set(result.grammar)].join(", ")}</div>

                <div className="rikai-word-meaning">{getMeaning(result.meanings)}</div>

                <div className="rikai-word-examples">
                    {wordExamples &&
                        wordExamples.map((example, index) =>
                            <div key={example + index}>
                                <div>
                                    <TTS text={example.sentence}/>
                                    <span className="example-sentence" dangerouslySetInnerHTML={{__html:
                                            getHighlightedSentence(example.sentence, result)}}/>
                                </div>
                                <div>{example.meaning}</div>
                            </div>
                        )
                    }
                </div>
                {/*<div className="rikai-word-more-ex">more examples</div>*/}

            </div>
        </div>
    );
}

type RikaiWordProps = {
    style: object
    result: object
    wordExamples: Array<string>
    changePopup: any
    fetchKanji: any
}

export default RikaiWord;
