import React from 'react';
import AddToDeckButton from '../../../../../components/buttons/addToDeckBtn/AddToDeckButton';
import BackButton from '../../../../../components/buttons/backBtn/BackButton';
import PopupType from '../PopupType';
import Kanji from './Kanji';


const handleBack = (showPreviousWord, changePopup) => {
    showPreviousWord();
    changePopup(PopupType.WORD);
}

const RikaiKanji = ({style, result, showWordExamples, changePopup, showPreviousWord}: RikaiKanjiProps) => (
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

type RikaiKanjiProps = {
    style: object
    result: object
    showWordExamples: any
    changePopup: any
    showPreviousWord: any
}

export default RikaiKanji;
