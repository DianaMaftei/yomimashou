import React from 'react';
import TTS from "../../../../`common/TTS/TTS";
import STT from "../../../../`common/STT/STT";

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

export default (hidePopup, style, sentence) => {
    return (
        <div id="rikai-window" style={style}>
            <div className="rikai-top">
                <span className="rikai-title" id="title-examples">Sentence</span>
                <span className="closeBtn" onClick={hidePopup}>&#x2716;</span>
            </div>
            <div className="rikai-display">
                {!sentence.furigana && <div id="sentence">{sentence.text}</div>}
                {sentence.furigana && <div id="sentence" dangerouslySetInnerHTML={{__html: htmlDecode(sentence.furigana)}}/>}
                <TTS text={sentence.text}/>
                <br/>
                <STT />
                <br/>
                <div>{sentence.translation && escapeHtmlSpecialCharacters(sentence.translation)}</div>
                <br/>
                <div>{buildSentenceInfo(sentence.tokens)}</div>
                <hr/>
            </div>
        </div>
    );
}