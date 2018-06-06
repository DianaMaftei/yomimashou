import React from 'react';
import ExampleList from '../../components/rikai/Examples/ExampleList/index';

export default (hidePopup, style, result) => (
    <div id="rikai-window" style={style}>
        <div className="rikai-top">
            <span className="rikai-title" id="title-examples">Examples</span>
            <span className="closeBtn" onClick={hidePopup}>&#x2716;</span>
        </div>
        <div className="rikai-display">
            <ExampleList resultList={result.result}/>
        </div>
    </div>
);