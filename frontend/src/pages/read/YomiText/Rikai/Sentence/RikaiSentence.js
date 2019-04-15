import React from 'react';
import TTS from "../../../../`common/TTS/TTS";

export default (hidePopup, style, result) => {

    return (
        <div id="rikai-window" style={style}>
            <div className="rikai-top">
                <span className="rikai-title" id="title-examples">Sentence</span>
                <span className="closeBtn" onClick={hidePopup}>&#x2716;</span>
            </div>
            <div className="rikai-display">
                <span>{result}</span>
                <TTS text={result}/>
            </div>
        </div>
    );
}