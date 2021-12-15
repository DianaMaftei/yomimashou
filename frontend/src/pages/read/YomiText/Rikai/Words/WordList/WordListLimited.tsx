import React from "react";
import WordItem from "../WordItem/WordItem";

export default (results, showExamples, showMoreResults) => (
    <div id="word-list-limited">
        {results.map((result, index) => {
            return <WordItem wordClassName={(index % 2 === 0) ? 'definition-light' : 'definition-dark'}
                             key={result.kana + result.kanji + result.grammar + result.def}
                             kanji={result.kanji}
                             kana={result.kana} grammar={result.grammar} longDef={result.longDef}
                             shortDef={result.showShortDef} showExamples={showExamples}/>
        })}

        <div className="more-btn">
            <span id="more-btn" onClick={showMoreResults}>&#9660; More</span>
        </div>
    </div>
);
