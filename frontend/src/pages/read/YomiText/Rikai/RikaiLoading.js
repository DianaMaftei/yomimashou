import React from 'react';
import spinner from './spinner.svg';

export default (style) => (
    <div id="rikai-window" style={style}>
        <div className="rikai-display rikai-loading">
            <img id="spinner" src={spinner} alt=""/>
        </div>
    </div>
);
