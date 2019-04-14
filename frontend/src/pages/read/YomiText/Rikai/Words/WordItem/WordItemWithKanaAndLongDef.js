import React from "react";
import TTS from "../../../../../`common/TTS/TTS";

export default (kanaList, grammar, longDef, showExamples, wordClassName) => {
    let kana = kanaList.map((item, index) => {
        return <span className="w-kana" key={item + index}>{item}</span>
    });

    return (
        <div id="word-with-kana-and-long-def" className={"w-word " + wordClassName}>
            <span key={kanaList + grammar}>{kana}</span>
            <TTS text={kanaList.join()}/>
            <span className="example-btn" onClick={showExamples}>Ex</span>
            <span key={grammar} className="w-grammar">{grammar}</span>
            <p className="w-def longDef">{longDef}</p>
        </div>
    );
}
