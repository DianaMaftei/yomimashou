import React from 'react';
import FuriganaOptions from "./furigana/FuriganaOptions";
import "./textActions.css";
import Button from "@material-ui/core/Button";
import axios from "axios";

const dldMp3 =(textContent, textTitle) => {
    axios.post('https://talkify.net/api/speech/v1/?key=' + process.env.REACT_APP_TALKIFY_KEY, {
        Text : textContent,
        Format: "mp3",
        rate: -2,
        pitch: 2
    }, {
        responseType: 'blob'
    }).then(response => {
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', textTitle + '.mp3');
        document.body.appendChild(link);
        link.click();
    }).catch(err => {
        console.log(err);
    });
}

const TextActions = ({handleFurigana, textContent, textTitle, kanjiLevels}) => {
    return (
        <div>
            <div className="text-actions-buttons">
                <Button variant="outlined" component="span" onClick={() => {}}>
                    Add to Favorites
                </Button>
                <Button variant="outlined" onClick={() => dldMp3(textContent, textTitle)} type="button" id="dld-tts-btn" >
                    Download MP3
                </Button>
            </div>
            <FuriganaOptions handleFurigana={handleFurigana} kanjiLevels={kanjiLevels}/>
        </div>
    );
};

export default TextActions;
