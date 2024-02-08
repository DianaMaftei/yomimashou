import React from 'react';
import PopupType from '../PopupType';
import KanjiStrokeDiagram from './KanjiStrokeDiagram';


const Kanji = ({result, showWordExamples, changePopup}: KanjiProps) => (
    <div className="kanji-box">
        <div className="kanji-info">
            <KanjiStrokeDiagram character={result.character} doOnClick={() => changePopup(PopupType.DRAW)}/>

            <div className="k-main-data">
                <p className="k-eigo">{result.eigo}</p>
                <p>Kun: <span className="k-yomi">{result.kunReading || '–'}</span></p>
                <p>On: <span className="k-yomi">{result.onReading || '–'}</span></p>
            </div>
            <div className="k-dictionary-data">
                {result.strokes && <span
                    className="k-main-data-line">Strokes: <span>{result.strokes}</span></span>}
                {result.grade && <span
                    className="k-main-data-line">Grade: <span>{result.grade}</span></span>}
                {result.oldJLPT && <span
                    className="k-main-data-line">Old JLPT: <span>{result.oldJLPT}</span></span>}
                {result.newJLPT && <span
                    className="k-main-data-line">New JLPT: <span>{result.newJLPT}</span></span>}
                {result.frequency && <span
                    className="k-main-data-line">Frequency: <span>{result.frequency}</span></span>}
            </div>
        </div>
        <div className="view-mnemonics" onClick={() => changePopup(PopupType.RTK)}>View mnemonics</div>
        <h6 className="examples-label">Example words:</h6>
        <div className="example-words-search">
            <div onClick={() => showWordExamples(
                result.character,
                'byStartingKanji'
            )}>{result.character}<span
                className="asterisk">*</span></div>
            <div onClick={() => showWordExamples(
                result.character,
                'byContainingKanji'
            )}><span
                className="asterisk">*</span>{result.character}<span
                className="asterisk">*</span></div>
            <div onClick={() => showWordExamples(
                result.character,
                'byEndingKanji'
            )}><span
                className="asterisk">*</span>{result.character}</div>
        </div>
    </div>
);


type KanjiProps = {
    result: object
    showWordExamples: any
    changePopup: any
}

export default Kanji;

