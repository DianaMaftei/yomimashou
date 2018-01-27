import React from "react";

const Kanji = (props) => {
    return (
        <div className="kanji-box">
            <div>
                <div className="k-kanji">{props.result.kanji}</div>
                <div className="k-main-data">
                    <span className="k-main-data-line">Frequency: {props.result.frequency}</span>
                    <span className="k-main-data-line">Grade: {props.result.grade}</span>
                    <span className="k-main-data-line">Strokes: {props.result.strokes}</span>
                </div>
            </div>
            <h2 className="k-eigo">{props.result.eigo}</h2>
            <h3>Kun: <span className="k-yomi">{props.result.kunReading}</span></h3>
            <h3>On: <span className="k-yomi">{props.result.onReading}</span></h3>
        </div>
    );
};

export default Kanji;
