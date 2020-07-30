import React from 'react';
import Kanji from "./Kanji";
import BackButton from "../../../../`common/buttons/backBtn/BackButton";
import AddToDeckButton from "../../../../`common/buttons/addToDeckBtn/AddToDeckButton";

export default (hidePopup, style, result, showWordExamples) => (
    <div id="rikai-window" style={style}  onClick={hidePopup} class="elevation-lg">
        <div className="rikai-display">
            <div className="rikai-kanji-top">
                <BackButton/>
                <AddToDeckButton size="28px"/>
            </div>
            <Kanji result={result.result} showWordExamples={showWordExamples}/>
        </div>
    </div>
);