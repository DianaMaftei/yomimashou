import React from "react";
import TTS from "../../../../../`common/TTS/TTS";
import CardItemOrigin from "../../AddToDeck/CardItemOrigin";
import Kuroshiro from "kuroshiro";

// const getCardItem = (example) => {
//   let sentenceContainsKanji = Kuroshiro.Util.hasKanji(example.sentence);
//
//   let item = {
//     kanji: sentenceContainsKanji ? example.sentence : '',
//     kana: sentenceContainsKanji ? '' : example.sentence,
//     explanation: example.meaning,
//     cardItemOrigin: CardItemOrigin.SENTENCE_EXAMPLE
//   };
//
//   return item;
// };

export default ({exampleClassName, example}) => (
    <div className={"example " + exampleClassName}>
      <p>{example.sentence}</p>
      <TTS text={example.sentence}/>
      <p>{example.meaning}</p>
    </div>
);