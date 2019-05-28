import React from "react";
import KanjiStrokeDiagram from "./KanjiStrokeDiagram";
import TTS from "../../../../`common/TTS/TTS";
import KanjiDrawPad from "./KanjiDrawPad/KanjiDrawPad"
import SimpleModal from "../../../../`common/modal/SimpleModal";
import RtkInfo from "./RtkInfo";

export default ({ result, showWordExamples }) => {
    return (
        <div className="kanji-box">
            <div className="kanji-info">
                <KanjiStrokeDiagram character={result.character}/>
                <div className="k-main-data">
                    {result.frequency && <span className="k-main-data-line">Frequency: {result.frequency}</span>}
                    {result.grade && <span className="k-main-data-line">Grade: {result.grade}</span>}
                    {result.strokes && <span className="k-main-data-line">Strokes: {result.strokes}</span>}
                    {result.oldJLPT && <span className="k-main-data-line">Old JLPT: {result.oldJLPT}</span>}
                    {result.newJLPT && <span className="k-main-data-line">New JLPT: {result.newJLPT}</span>}
                </div>
                <SimpleModal label={'Practice'}>
                    <KanjiDrawPad character={result.character}/>
                </SimpleModal>
            </div>
            <h2 className="k-eigo">{result.eigo}</h2>
            <h3>Kun: <span className="k-yomi">{result.kunReading || '–'}</span></h3>
            {result.kunReading && <TTS text={result.kunReading}/>}
            <h3>On: <span className="k-yomi">{result.onReading || '–'}</span></h3>
            {result.onReading && <TTS text={result.onReading}/>}
            <br/>
            <RtkInfo keyword={result.keyword} components={result.components} story1={result.story1}
                     story2={result.story2}/>
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

