import React from 'react';
import TTS from "../../../../../components/tts/TTS";
import STT from "../../../../../components/stt/STT";
import Kuroshiro from "kuroshiro";
import "./rikaiSentence.scss";

function escapeHtmlSpecialCharacters(string) {
    let elem = document.createElement('textarea');
    elem.innerHTML = string;
    return elem.value;
}

function hasContent(string) {
    return string !== undefined && string !== null && string.length > 0;
}

function buildPartsOfSpeechInfo(token) {
    let pos1 = token.partOfSpeechLevel1;
    let pos2 = token.partOfSpeechLevel2;
    let pos3 = token.partOfSpeechLevel3;
    let pos4 = token.partOfSpeechLevel4;

    return pos1 + (hasContent(pos2) ? " - " + pos2 : '') + (hasContent(pos3) ? " - " + pos3 : '') + (hasContent(pos4) ? " - " + pos4 : '')
}

function buildSentenceInfo(sentenceTokens) {
    let words = [];
    sentenceTokens.forEach(function (token, index) {
        words.push(<div key={"word" + token.surface + index} style={{fontSize: '20px'}}>
            {isWord(token) &&
            <p className="token"
               id={"word" + token.surface + index}>{token.surface + " - " + buildPartsOfSpeechInfo(token)}</p>}
        </div>);
    });

    return words;
}

function htmlDecode(input) {
    let doc = new DOMParser().parseFromString(input, "text/html");
    return doc.body.innerHTML;
}

function isWord(token) {
    if (token == null) return;
    return !(token.partOfSpeechLevel2 === "comma" ||
        token.partOfSpeechLevel2 === "period" ||
        token.partOfSpeechLevel2 === "open parenthesis" ||
        token.partOfSpeechLevel2 === "closed parenthesis" ||
        token.partOfSpeechLevel2 === "sign" ||
        token.partOfSpeechLevel2 === "white space"
    );
}

export default ({style, sentence}) => {
    let sentenceContainsKanji = Kuroshiro.Util.hasKanji(sentence.text);

    return (
        <div id="rikai-window" style={style}>
            <div className="rikai-display">
                <div>
                    <span id="sentence-tts-btn"><TTS text={sentence.text}/></span>
                    {!sentence.furigana && <div id="sentence">{sentence.text}</div>}
                    {sentence.furigana && <div id="sentence" dangerouslySetInnerHTML={{__html: htmlDecode(sentence.furigana)}}/>}
                </div>
                <br/>
                <div>{sentence.translation && escapeHtmlSpecialCharacters(sentence.translation)}</div>
                <br/>
                <STT text={sentence.text}/>
                <br/>
                {/*<div>{buildSentenceInfo(sentence.tokens)}</div>*/}
            </div>
        </div>
    );
}
