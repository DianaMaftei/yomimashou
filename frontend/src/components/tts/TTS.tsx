import VolumeHighIcon from 'mdi-react/VolumeHighIcon';
import colors from '../../style/colorConstants';
import './tts.scss';


const CHUNK_LENGTH_OF_TEXT = 120;

const speechUtteranceChunker = (utt: SpeechSynthesisUtterance, settings: any) => {
    settings = settings || {};
    let chunkLength = settings && settings.chunkLength || 160;
    let pattRegex = new RegExp('^.{' + Math.floor(chunkLength / 2) + ',' + chunkLength + '}[\.\!\?\,]{1}|^.{1,' + chunkLength + '}$|^.{1,' + chunkLength + '} ');
    let txt = (settings && settings.offset !== undefined ? utt.text.substring(settings.offset) : utt.text);
    let chunkArr = txt.match(pattRegex);

    if(chunkArr[0] !== undefined && chunkArr[0].length > 2) {
        let chunk = chunkArr[0];
        let newUtt = createNewUtterance(chunk);

        for(let x in utt) {
            if(utt.hasOwnProperty(x) && x !== 'text') {
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
};

const play = (utterance: SpeechSynthesisUtterance, isTextLong: boolean) => {
    if(!isTextLong) {
        window.speechSynthesis.speak(utterance);
        return;
    }

    speechUtteranceChunker(utterance, {
        chunkLength: 120
    });
};

const createNewUtterance = (text: string): SpeechSynthesisUtterance => {
    let utterance = new SpeechSynthesisUtterance(text);
    utterance.lang = 'ja-JP';
    utterance.voice = speechSynthesis.getVoices().filter(function (voice) {
        return voice.name === 'Google 日本語';
    })[0];
    return utterance;
};

const TTS = ({text}: TTSProps) => {
    let utterance = createNewUtterance(text);
    let isTextLong = text.length > CHUNK_LENGTH_OF_TEXT;

    return (
        <div id="TTS-btn" onClick={() => play(utterance, isTextLong)}>
            <VolumeHighIcon color={colors.yomiGray500} size="20"/>
        </div>
    );
};

type TTSProps = {
    text: string
}

export default TTS;
