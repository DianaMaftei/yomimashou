import React from 'react';
import FuriganaOptions from "./furigana/FuriganaOptions";
import DownloadIcon from "mdi-react/DownloadIcon";
import "./textActions.css";

const TextActions = ({analyzer, kanjiLevel, setKanjiLevel, textContent, toggleFurigana}) => {
    return (
        <div id="text-actions">
            <FuriganaOptions analyzer={analyzer} toggleFurigana={toggleFurigana}
                             kanjiLevel={kanjiLevel} setLevel={setKanjiLevel}/>
            <form id="dld-tts" method="POST"
                  action={'https://talkify.net/api/speech/v1/download?key=' + process.env.REACT_APP_TALKIFY_KEY}>
                <input type="hidden" name="text" value={textContent}/>
                <button type="submit" className="btn btn-light" id="dld-tts-btn">
                    <DownloadIcon size={24}/>
                </button>
            </form>
        </div>
    );
};

export default TextActions;
