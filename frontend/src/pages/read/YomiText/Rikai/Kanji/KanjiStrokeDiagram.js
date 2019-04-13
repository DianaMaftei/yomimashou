import React from "react";
import apiUrl from "../../../../../AppUrl";
import Raphael from "raphael";
import "dmak";
import SkipPrevious from '@material-ui/icons/SkipPrevious';
import SkipNext from '@material-ui/icons/SkipNext';
import PauseCircleOutline from '@material-ui/icons/PauseCircleOutline';
import PlayCircleOutline from '@material-ui/icons/PlayCircleOutline';
import Replay from '@material-ui/icons/Replay';

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
            <div id="kanji-draw"/>
            <div id="sample-btn">
                <button className="btn" id="p" onClick={() => dmak.eraseLastStrokes(1)}><SkipPrevious fontSize="default"/>
                </button>
                <button className="btn" id="s" onClick={() => dmak.pause()}><PauseCircleOutline fontSize="small"/></button>
                <button className="btn" id="g" onClick={() => dmak.render()}><PlayCircleOutline fontSize="small"/></button>
                <button className="btn" id="r" onClick={() => dmak.erase()}><Replay fontSize="small"/></button>
                <button className="btn" id="n" onClick={() => dmak.renderNextStrokes(1)}><SkipNext fontSize="small"/>
                </button>
            </div>
        </div>
    );
}