import React from "react";
import {apiUrl} from "../../../../../AppUrl";
import Raphael from "raphael";
import "dmak";
import SkipPrevious from 'mdi-react/SkipPreviousIcon';
import SkipNext from 'mdi-react/SkipNextIcon';
import PauseCircleOutline from 'mdi-react/PauseCircleOutlineIcon';
import PlayCircleOutline from 'mdi-react/PlayCircleOutlineIcon';
import Replay from 'mdi-react/ReplayIcon';

window.Raphael = Raphael;

export default ({character}) => {
    let dmak = new window.Dmak(character, {
        'element': "kanji-draw",
        "stroke": {attr: {active: "#943a51"}},
        step: 0.01,
        "uri": apiUrl + '/api/dictionary/kanji/svg/'
    });

    return (
        <div className="k-kanji">
            <div className="practice-label">Tap to practice</div>
            <div id="kanji-draw"/>
            <div id="sample-btn">
                <button id="p" onClick={() => dmak.eraseLastStrokes(1)}><SkipPrevious size={16}/>
                </button>
                <button id="s" onClick={() => dmak.pause()}><PauseCircleOutline size={16}/></button>
                <button id="g" onClick={() => dmak.render()}><PlayCircleOutline size={16}/></button>
                <button id="r" onClick={() => dmak.erase()}><Replay size={16}/></button>
                <button id="n" onClick={() => dmak.renderNextStrokes(1)}><SkipNext size={16}/>
                </button>
            </div>
        </div>
    );
}