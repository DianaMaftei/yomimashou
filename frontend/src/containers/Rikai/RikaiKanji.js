import React from 'react';
import Kanji from '../../components/rikai/Kanji/Kanji';

export default (hidePopup, style, result) => (
    <div id="rikai-window" style={style}>
        <div className="rikai-top">
            <span className="rikai-title" id="title-kanji">Kanji Dictionary</span>
            <span className="closeBtn" onClick={hidePopup}>&#x2716;</span>
        </div>
        <div className="rikai-display">
            <Kanji result={result.result}/>
        </div>
    </div>
);