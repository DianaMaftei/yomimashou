import React from "react";
import KuromojiAnalyzer from "kuroshiro-analyzer-kuromoji/dist/kuroshiro-analyzer-kuromoji.min";
import Kuroshiro from "kuroshiro";
import ActionButton from "../buttons/actionBtn/ActionButton";

let recognition;
let recognizing = false;
let final_transcript = '';
let kuroshiro;
let analyzer;
let hiraganaSentence;

const stripTextOfPunctuation = (sentence) => {
    return sentence.split("").map(character => Kuroshiro.Util.isHiragana(character) ? character : "").join("");
}

function init(text) {
    kuroshiro = new Kuroshiro();
    analyzer = new KuromojiAnalyzer({
        dictPath: "/static/kuromoji"
    });
    kuroshiro.init(analyzer);

    setTimeout(function () {
        kuroshiro.convert(text, {to: "hiragana", mode: "normal"}).then(function (result) {
            hiraganaSentence = stripTextOfPunctuation(result);
        });
    }, 4000);

    recognition = new window.webkitSpeechRecognition();
    recognition.lang = 'ja-JP';

    recognition.onresult = function (event) {
        var interim_transcript = '';
        for (var i = event.resultIndex; i < event.results.length; ++i) {
            if (event.results[i].isFinal) {
                final_transcript += event.results[i][0].transcript;
            } else {
                interim_transcript += event.results[i][0].transcript;
            }
        }

        console.log(interim_transcript);

        kuroshiro.convert(final_transcript, {to: "hiragana", mode: "normal"}).then(function (result) {
            console.log("hiraganaSentence", hiraganaSentence);
            console.log("result", stripTextOfPunctuation(result));
            console.log("SAME:", hiraganaSentence === stripTextOfPunctuation(result));
        });
    };
}

function start() {
    if (recognizing) {
        recognition.stop();
        recognizing = false;
        final_transcript = '';
        return;
    }
    recognition.start();
    recognizing = true;
}

const STT = ({text}) => {
    init(text);

    return (
        <div id="STT">
            <ActionButton onClick={start} label="Practice Speaking"/>
            {hiraganaSentence}
        </div>
    );
}

export default STT;
