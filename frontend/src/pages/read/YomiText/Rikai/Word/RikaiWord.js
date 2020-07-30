import React from 'react';
import MaterialIcon from "material-icons-react";
import colors from "../../../../../style/colorConstants"
import AddToDeckButton from "../../../../`common/buttons/addToDeckBtn/AddToDeckButton";

export default (hidePopup, style, result, wordExamples, showMore) => {
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

    const getSentence = (sentence, word) => {
        const keyWords = [...word.kanji, ...word.kana];
        let newSentence = sentence;
        keyWords.forEach(word=> {
            newSentence = newSentence.replace(word, '<span style="color:placeholderColor; font-weight: 500">placeholder</span>');
            newSentence = newSentence.replace('placeholderColor', colors.yomiLightRed);
            newSentence = newSentence.replace('placeholder', word);
        })
        return newSentence;
    }

    return (
        <div id="rikai-window" style={style} className="elevation-lg" onClick={hidePopup}>
            <div className="rikai-display container">
                <span className="rikai-word-tts">
                    <MaterialIcon icon="volume_up" color={colors.yomiGray500} size="tiny"/>
                </span>
                <div className="rikai-word-kanji">{result.kanji.join(", ")}</div>
                <AddToDeckButton size="small"/>

                <div className="rikai-word-kana">{result.kana.join(", ")}</div>

                <div className="rikai-word-grammar">{[...new Set(result.grammar)].join(", ")}</div>

                <div className="rikai-word-meaning">{getMeaning(result.meanings)}</div>

                <div className="rikai-word-examples">
                    {
                        wordExamples.map(example => <div key={example}>
                            <div>
                                <MaterialIcon icon="volume_up" color={colors.yomiGray500} size="tiny"/>
                                <span className="example-sentence" dangerouslySetInnerHTML={{__html: getSentence(example.sentence, result)}}></span>

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