import React from 'react';
import ExampleList from './ExampleList/index';


const RikaiExamples = ({hidePopup, style, result}: RikaiExamplesProps) => (
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

type RikaiExamplesProps = {
    hidePopup: any
    style: any
    result: any
}

export default RikaiExamples;
