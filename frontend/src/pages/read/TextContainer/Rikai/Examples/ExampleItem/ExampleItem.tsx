import React from 'react';
import TTS from '../../../../../../components/tts/TTS';

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

const ExampleItem = ({exampleClassName, example}: ExampleItemProps) => (
    <div className={"example " + exampleClassName}>
      <p>{example.sentence}</p>
      <TTS text={example.sentence}/>
      <p>{example.meaning}</p>
    </div>
);

type ExampleItemProps = {
    exampleClassName: string
    example: object
}

export default ExampleItem;
