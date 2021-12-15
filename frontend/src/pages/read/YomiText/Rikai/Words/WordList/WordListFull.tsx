import React from "react";
import WordItem from "../WordItem/WordItem";

export default (results, showExamples) => (
    <div id="word-list-full">
        {results.map((result, index) => {
            return <WordItem wordClassName={(index % 2 === 0) ? 'definition-light' : 'definition-dark'}
                      key={result.kana + result.kanji + result.grammar + result.def}
                      kanji={result.kanji}
                      kana={result.kana} grammar={result.grammar} longDef={result.longDef}
                      shortDef={result.showShortDef} showExamples={showExamples}/>
        })}
    </div>
);
