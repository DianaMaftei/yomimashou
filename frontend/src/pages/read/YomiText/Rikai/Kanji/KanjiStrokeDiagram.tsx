import 'dmak';
import Pause from 'mdi-react/PauseIcon';
import Play from 'mdi-react/PlayIcon';
import Replay from 'mdi-react/ReplayIcon';
import SkipNext from 'mdi-react/SkipNextIcon';
import SkipPrevious from 'mdi-react/SkipPreviousIcon';
import Raphael from 'raphael';
import React from 'react';
import { readApiUrl } from '../../../../../AppUrl';


window.Raphael = Raphael;

const KanjiStrokeDiagram = ({character, doOnClick}: KanjiStrokeDiagramProps) => {
    let dmak = new window.Dmak(character, {
        'element': "kanji-draw",
        "stroke": {attr: {active: "#943a51"}},
        step: 0.01,
        "uri": readApiUrl + '/api/dictionary/kanji/svg/'
    });

    return (
        <div className="k-kanji">
            <div className="practice-label">Tap to practice</div>
            <div id="kanji-draw" onClick={() => doOnClick()}/>
            <div id="sample-btn">
                <button id="p" onClick={() => dmak.eraseLastStrokes(1)}><SkipPrevious size="20"/></button>
                <button id="s" onClick={() => dmak.pause()}><Pause size="20"/></button>
                <button id="g" onClick={() => dmak.render()}><Play size="20"/></button>
                <button id="r" onClick={() => dmak.erase()}><Replay size="20"/></button>
                <button id="n" onClick={() => dmak.renderNextStrokes(1)}><SkipNext size="20"/></button>
            </div>
        </div>
    );
}


type KanjiStrokeDiagramProps = {
    character: string
    doOnClick: any
}

export default KanjiStrokeDiagram;
