import React from "react";
import Raphael from "raphael";
import "dmak";
import MaterialIcon from 'material-icons-react';
import apiUrl from "../../../AppUrl";

export default ({ result, showWordExamples }) => {
    window.Raphael = Raphael;

    let dmak = new window.Dmak(result.character, {'element': "kanji-draw", "stroke": {attr:{active: "#943a51"}}, step: 0.01, "uri": apiUrl + '/api/dictionary/kanji/svg/'});

    return (
        <div className="kanji-box">
            <div className="kanji-info">
                <div className="k-kanji">
                    <div id="kanji-draw"/>
                    <div id="sample-btn">
                        <button className="btn" id="p" onClick={() => dmak.eraseLastStrokes(1)}><MaterialIcon icon="skip_previous" size='tiny'/></button>
                        <button className="btn" id="s" onClick={() => dmak.pause()}><MaterialIcon icon="pause_circle_outline" size='tiny'/></button>
                        <button className="btn" id="g" onClick={() => dmak.render()}><MaterialIcon icon="play_circle_outline" size='tiny'/></button>
                        <button className="btn" id="r" onClick={() => dmak.erase()}><MaterialIcon icon="replay" size='tiny'/></button>
                        <button className="btn" id="n" onClick={() => dmak.renderNextStrokes(1)}><MaterialIcon icon="skip_next" size='tiny'/></button>
                    </div>
                </div>
                <div className="k-main-data">
                    {result.frequency && <span className="k-main-data-line">Frequency: {result.frequency}</span>}
                    {result.grade && <span className="k-main-data-line">Grade: {result.grade}</span>}
                    {result.strokes && <span className="k-main-data-line">Strokes: {result.strokes}</span>}
                    {result.oldJLPT && <span className="k-main-data-line">Old JLPT: {result.oldJLPT}</span>}
                    {result.newJLPT && <span className="k-main-data-line">New JLPT: {result.newJLPT}</span>}
                </div>
            </div>
            <h2 className="k-eigo">{result.eigo}</h2>
            <h3>Kun: <span className="k-yomi">{result.kunReading}</span></h3>
            <h3>On: <span className="k-yomi">{result.onReading}</span></h3>
            <br/>

            <h4>See example words:</h4>
            <br/>
            <div className="example-words-search">
                <span onClick={() => showWordExamples(result.character, "byStartingKanji")}>{result.character}<span
                    className="asterisk">*</span></span>
                <span onClick={() => showWordExamples(result.character, "byContainingKanji")}><span
                    className="asterisk">*</span>{result.character}<span className="asterisk">*</span></span>
                <span onClick={() => showWordExamples(result.character, "byEndingKanji")}><span
                    className="asterisk">*</span>{result.character}</span>
            </div>
        </div>
    )
};

