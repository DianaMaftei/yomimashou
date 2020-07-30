import React from "react";
import KanjiStrokeDiagram from "./KanjiStrokeDiagram";
import CardItemOrigin from "../AddToDeck/CardItemOrigin";

export default ({result, showWordExamples}) => {

  const getKana = (kunReading, onReading) => {
    let kana;
    if (kunReading && kunReading.length > 0) {
      if (onReading && onReading.length > 0) {
        kana = kunReading + ", " + onReading;
      } else {
        kana = kunReading;
      }
    } else {
      kana = onReading;
    }

    return kana;
  };

  // let item = {
  //   kanji: result.character,
  //   kana: getKana(result.kunReading, result.onReading),
  //   explanation: result.eigo,
  //   cardItemOrigin: CardItemOrigin.KANJI
  // };

  return (
      <div className="kanji-box">
        <div className="kanji-info">
          <KanjiStrokeDiagram character={result.character}/>
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
          <div className="k-main-data">
              <p>Kun: <span className="k-yomi">{result.kunReading || '–'}</span></p>
              <p>On: <span className="k-yomi">{result.onReading || '–'}</span></p>
              <p className="k-eigo">{result.eigo}</p>
          </div>

        <div className="view-mnemonics">View mnemonics</div>
        <h6 className="examples-label">Example words:</h6>
        <div className="example-words-search">
            <div onClick={() => showWordExamples(result.character,
                    "byStartingKanji")}>{result.character}<span
                    className="asterisk">*</span></div>
          <div onClick={() => showWordExamples(result.character,
              "byContainingKanji")}><span
              className="asterisk">*</span>{result.character}<span
              className="asterisk">*</span></div>
          <div onClick={() => showWordExamples(result.character,
              "byEndingKanji")}><span
              className="asterisk">*</span>{result.character}</div>
        </div>
      </div>
  )
};

