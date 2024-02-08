import React from 'react';
import WordList from './WordList/WordList';


export default (hidePopup, style, result, last, showExamples, showMore) => (
    <div id="rikai-window" style={style}>
        <div className="rikai-top">
            <span className="rikai-title" id="title-words">Word Dictionary</span>
            <span className="closeBtn" onClick={hidePopup}>&#x2716;</span>
        </div>
        <div className="rikai-display">
            <WordList resultList={result.result} showMore={showMore} last={last}
                      showExamples={showExamples}/>
        </div>
    </div>
);
