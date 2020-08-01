import React from 'react';
import spinner from './spinner.svg';

export default (style) => (
    <div id="rikai-window" style={style}>
        <div className="rikai-top">
            <span className="rikai-title">Loading...</span>
        </div>
        <div className="rikai-display">
            <img id="spinner" src={spinner} alt=""/>
        </div>
    </div>
);