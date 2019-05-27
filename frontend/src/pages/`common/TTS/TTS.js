import React from "react";
import PauseCircleOutline from 'mdi-react/PauseCircleOutlineIcon';
import PlayCircleOutline from 'mdi-react/PlayCircleOutlineIcon';
import Replay from 'mdi-react/ReplayIcon';
import Stop from 'mdi-react/StopIcon';
import StepForward from 'mdi-react/StepForwardIcon';
import "./tts.css";

const CHUNK_LENGTH_OF_TEXT = 120;

function speechUtteranceChunker(utt, settings) {
    settings = settings || {};
    let chunkLength = settings && settings.chunkLength || 160;
    let pattRegex = new RegExp('^.{' + Math.floor(chunkLength / 2) + ',' + chunkLength + '}[\.\!\?\,]{1}|^.{1,' + chunkLength + '}$|^.{1,' + chunkLength + '} ');
    let txt = (settings && settings.offset !== undefined ? utt.text.substring(settings.offset) : utt.text);
    let chunkArr = txt.match(pattRegex);

    if (chunkArr[0] !== undefined && chunkArr[0].length > 2) {
        let chunk = chunkArr[0];
        let newUtt = createNewUtterance(chunk);

        for (let x in utt) {
            if (utt.hasOwnProperty(x) && x !== 'text') {
                newUtt[x] = utt[x];
            }
        }
        newUtt.onend = function () {
            settings.offset = settings.offset || 0;
            settings.offset += chunk.length - 1;
            speechUtteranceChunker(utt, settings);
        };
        console.log(newUtt); //IMPORTANT!! Do not remove
        setTimeout(function () {
            speechSynthesis.speak(newUtt);
        }, 0);
    }
}

function play(utterance, isTextLong) {
    if (!isTextLong) {
        window.speechSynthesis.speak(utterance);
        return;
    }

    speechUtteranceChunker(utterance, {
        chunkLength: 120
    });
}

function pause() {
    window.speechSynthesis.pause();
}

function resume() {
    window.speechSynthesis.resume();
}

function replay(utterance, text, isTextLong) {
    window.speechSynthesis.cancel();
    utterance = createNewUtterance(text);
    play(utterance, isTextLong);
}

function cancel() {
    window.speechSynthesis.cancel();
}

function createNewUtterance(text) {
    let utterance = new SpeechSynthesisUtterance(text);
    utterance.lang = "ja-JP";
    utterance.voice = speechSynthesis.getVoices().filter(function(voice) { return voice.name == 'Google 日本語'; })[0];
    return utterance;
}

export default ({text}) => {
    let utterance = createNewUtterance(text);
    let isTextLong = text.length > CHUNK_LENGTH_OF_TEXT;

    return (
        <div id="TTS-btns">
            <button id="play" className="btn" onClick={() => play(utterance, isTextLong)}><PlayCircleOutline
                size={16}/></button>
            <button id="pause" className="btn" onClick={pause}><PauseCircleOutline size={16}/></button>
            <button id="resume" className="btn" onClick={resume}><StepForward size={16}/></button>
            <button id="cancel" className="btn" onClick={cancel}><Stop size={16}/></button>
            <button id="replay" className="btn" onClick={() => replay(utterance, text, isTextLong)}><Replay
                size={16}/></button>

        </div>
);
}
