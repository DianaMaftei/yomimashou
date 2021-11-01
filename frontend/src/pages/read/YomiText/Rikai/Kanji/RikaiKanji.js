import React from 'react';
import Kanji from "./Kanji";
import BackButton from "../../../../../components/buttons/backBtn/BackButton";
import AddToDeckButton from "../../../../../components/buttons/addToDeckBtn/AddToDeckButton";
import PopupType from "../PopupType";

const handleBack = (showPreviousWord, changePopup) => {
    showPreviousWord();
    changePopup(PopupType.WORD);
}

export default ({style, result, showWordExamples, changePopup, showPreviousWord}) => (
    <div id="rikai-window" style={style} className="elevation-lg">
        <div className="rikai-display">
            <div className="rikai-kanji-top">
                <BackButton onClick={() => handleBack(showPreviousWord, changePopup)}/>
                <span >
                    <AddToDeckButton onClick={() => changePopup(PopupType.ADD)}/>
                </span>
            </div>
            <Kanji result={result} showWordExamples={showWordExamples} changePopup={changePopup}/>
        </div>
    </div>
);
