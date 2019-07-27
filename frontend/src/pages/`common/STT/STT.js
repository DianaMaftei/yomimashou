import React from "react";
import "./STT.css";
import KuromojiAnalyzer from "kuroshiro-analyzer-kuromoji/dist/kuroshiro-analyzer-kuromoji.min";
import Kuroshiro from "kuroshiro";

let recognition;
let recognizing = false;
let final_transcript ='';
let kuroshiro;
let analyzer;

function init() {
    kuroshiro = new Kuroshiro();
    analyzer = new KuromojiAnalyzer({
        dictPath: "/static/kuromoji"
    });
    kuroshiro.init(analyzer);

    recognition = new window.webkitSpeechRecognition();
    recognition.lang = 'ja-JP';

    recognition.onresult = function(event) {
        var interim_transcript = '';
        for (var i = event.resultIndex; i < event.results.length; ++i) {
            if (event.results[i].isFinal) {
                final_transcript += event.results[i][0].transcript;
            } else {
                interim_transcript += event.results[i][0].transcript;
            }
        }
        console.log(final_transcript);
        console.log(interim_transcript);
    };
}

function startButton(event) {
    if(recognizing) {
        recognition.stop();
        recognizing = false;
        final_transcript = '';
        return;
    }
    recognition.start();
    recognizing = true;
}

export default ({text}) => {
    init();

    return (
        <div id="STT">
            <button id="start_button" onClick={startButton}>START</button>
        </div>
    );
}
