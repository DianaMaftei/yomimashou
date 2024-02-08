import React from 'react';
import NameList from './NameList';


export default (hidePopup, style, result, limit) => (
    <div id="rikai-window" style={style}>
        <div className="rikai-top">
            <span className="rikai-title">Names Dictionary</span>
            <span className="closeBtn" onClick={hidePopup}>&#x2716;</span>
        </div>
        <div className="rikai-display">
            <NameList resultList={result.result} limit={limit}/>
        </div>
    </div>
);
