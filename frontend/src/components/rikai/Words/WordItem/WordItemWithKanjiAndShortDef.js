import React from "react";

export default (kanjiList, kanaList, grammar, shortDef, showExamples, wordClassName, showLongDef) => {
    let kanji = kanjiList.map((item, index) => {
        return <span className="w-kanji" key={item + index}>{item}</span>
    });

    let kana = kanaList.map((item, index) => {
        return <span className="w-kana" key={item + index}>{item}</span>
    });

    return (
        <div id="word-with-kanji-and-short-def" className={"w-word " + wordClassName}>
            <div key={kanjiList}>
                <span>{kanji}</span>
                <span className="example-btn" onClick={showExamples}>Ex</span>
            </div>
            <span key={kanaList + grammar}>{kana}</span>
            <span key={grammar} className="w-grammar">{grammar}</span>
            <p className="w-def shortDef">
                {shortDef}
                <span id="ellipsis" onClick={showLongDef}> ...</span>
            </p>
        </div>
    );
}