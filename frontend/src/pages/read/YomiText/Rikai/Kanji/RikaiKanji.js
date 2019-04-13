import React from 'react';
import Kanji from "./Kanji";

export default (hidePopup, style, result, showWordExamples) => (
    <div id="rikai-window" style={style}>
        <div className="rikai-top">
            <span className="rikai-title" id="title-kanji">Kanji Dictionary</span>
            <span className="closeBtn" onClick={hidePopup}>&#x2716;</span>
        </div>
        <div className="rikai-display">
            <Kanji result={result.result} showWordExamples={showWordExamples}/>
        </div>
    </div>
);