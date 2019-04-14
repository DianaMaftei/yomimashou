import React from "react";
import TTS from "../../../../../`common/TTS/TTS";

export default (kanjiList, kanaList, grammar, longDef, showExamples, wordClassName) => {
    let kanji = kanjiList.map((item, index) => {
        return <span className="w-kanji" key={item + index}>{item}</span>
    });

    let kana = kanaList.map((item, index) => {
        return <span className="w-kana" key={item + index}>{item}</span>
    });

    return (
        <div id="word-with-kanji-and-long-def" className={"w-word " + wordClassName}>
            <div key={kanjiList}>
                <span>{kanji}</span>
                <span className="example-btn" onClick={showExamples}>Ex</span>
            </div>
            <span key={kanaList + grammar}>{kana}</span>
            <TTS text={kanaList.join()}/>
            <span key={grammar} className="w-grammar">{grammar}</span>
            <p className="w-def longDef">{longDef}</p>
        </div>
    );
}
