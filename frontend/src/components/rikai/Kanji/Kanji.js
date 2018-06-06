import React from "react";

export default ({ result }) => (
    <div className="kanji-box">
        <div>
            <div className="k-kanji">{result.kanji}</div>
            <div className="k-main-data">
                <span className="k-main-data-line">Frequency: {result.frequency}</span>
                <span className="k-main-data-line">Grade: {result.grade}</span>
                <span className="k-main-data-line">Strokes: {result.strokes}</span>
            </div>
        </div>
        <h2 className="k-eigo">{result.eigo}</h2>
        <h3>Kun: <span className="k-yomi">{result.kunReading}</span></h3>
        <h3>On: <span className="k-yomi">{result.onReading}</span></h3>
    </div>
);

