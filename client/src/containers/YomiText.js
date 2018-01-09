import React from "react";
import { connect } from "react-redux";
import { popUpSetVisibility, updateSearchResult, updateShowResult, updateHighlightTerm } from "../actions/index";
import { tryToFindTextAtMouse, isVisible, search } from "../util/rikai/RikaiTextParser";
import RikaiPopUp from "../components/rikai/Rikai-pop-up";

const mapStateToProps = (state) => ({
    text: state.yomi.text,
    searchResult: state.popUp.searchResult,
    showResult: state.popUp.showResult,
    limit: state.config.popUp.limit
});

const mapDispatchToProps = (dispatch) => ({
    updateHighlightTerm: result => {
        dispatch(updateHighlightTerm(result));
    },
    updateSearchResult: result => {
        dispatch(updateSearchResult(result));
    },
    updateShowResult: result => {
        dispatch(updateShowResult(result));
    },
    togglePopupVisibility: visibility => {
        dispatch(popUpSetVisibility(visibility));
    }
});

class YomiText extends React.Component {
    constructor(props) {
        super(props);

        this.lastPos = { x: null, y: null };
        this.defaultDict = 2;
        this.keysDown = [];
        this.nextDict = 3;

        this.config = {};
        this.config.highlight = 'true';
        this.config.popupDelay = 0;
        this.config.disablekeys = 'false';
        this.config.limit = 5;

        this.searchResult = {
            type: "words"
        };

        // Hack because ro was coming out always 0 for some reason.
        this.lastRo = 0;

        this.onMouseMove = this.onMouseMove.bind(this);
        this.onMouseClick = this.onMouseClick.bind(this);
        this.onKeyDown = this.onKeyDown.bind(this);
        this.onKeyUp = this.onKeyUp.bind(this);

        this.hidePopup = this.hidePopup.bind(this);
        this.showPopup = this.showPopup.bind(this);
        this.showExamples = this.showExamples.bind(this);

        this.enable();
    }

    onKeyDown(ev) {
        if ((ev.shiftKey) && (ev.keyCode !== 16)) return;
        if (this.keysDown[ev.keyCode]) return;
        if (!isVisible()) return;

        if (window.rikaiR.config.disablekeys === 'true' && (ev.keyCode !== 16)) return;

        let i;

        switch (ev.keyCode) {
            case 83:	// s - switch dictionaries (kanji, names, words)
                search(ev.currentTarget.rikaiR, this.nextDict, this.props.updateSearchResult);
                this.props.updateShowResult(this.props.searchResult);
                this.showPopup(window.rikaiR.prevTarget);
                break;
            case 81:	// q - hide popup
                this.hidePopup();
                break;
            case 66:	// b - previous character
                let ofs = ev.currentTarget.rikaiR.uofs;
                for (i = 50; i > 0; --i) {
                    ev.currentTarget.rikaiR.uofs = --ofs;
                    if (search(ev.currentTarget.rikaiR, this.defaultDict, this.props.updateSearchResult) >= 0) {
                        if (ofs >= ev.currentTarget.rikaiR.uofs) break;	// ! change later
                    }
                }
                this.props.updateShowResult(this.props.searchResult);
                this.showPopup(window.rikaiR.prevTarget);
                break;
            case 77:	// m - next character
                ev.currentTarget.rikaiR.uofsNext = 1;

            // eslint-disable-next-line
            case 78:	// n - next word
                for (i = 50; i > 0; --i) {
                    ev.currentTarget.rikaiR.uofs += ev.currentTarget.rikaiR.uofsNext;
                    if (search(ev.currentTarget.rikaiR, this.defaultDict, this.props.updateSearchResult) >= 0) break;
                }
                this.props.updateShowResult(this.props.searchResult);
                this.showPopup(window.rikaiR.prevTarget);
                break;
            default:
                return;
        }

        this.keysDown[ev.keyCode] = 1;

        // don't eat shift
        ev.preventDefault();
    }

    onKeyUp(ev) {
        if (this.keysDown[ev.keyCode]) this.keysDown[ev.keyCode] = 0;
    }

    //Add the listeners and configure app
    enable() {
        if (window.rikaiR === null || window.rikaiR === undefined) {
            window.rikaiR = {};

            window.rikaiR.config = this.config;

            window.addEventListener('mousemove', this.onMouseMove, false);
            window.addEventListener('click', this.onMouseClick, false);
            window.addEventListener('keydown', this.onKeyDown, true);
            window.addEventListener('keyup', this.onKeyUp, true);
        }
    }

    //Removes the listeners and deletes the element
    disable() {
        if (window.rikaiR !== null) {
            let e;
            window.removeEventListener('mousemove', this.onMouseMove, false);
            window.removeEventListener('click', this.onMouseClick, false);
            window.removeEventListener('keydown', this.onKeyDown, true);
            window.removeEventListener('keyup', this.onKeyUp, true);

            e = document.getElementById('rikai-window');
            if (e) e.parentNode.removeChild(e);

            this.clearHi();
            delete window.rikaiR;
        }
    }

    showExamples(word) {
        let term1 = word.kanji !== undefined ? word.kanji : word.kana;
        let term2 = word.kanji !== undefined ? word.kana : '';
        let url = `http://nihongo.monash.edu/cgi-bin/wwwjdic?1ZEU${term1}=1=${term2}`;

        let axios = require('axios');

        let self = this;

        axios.get(url)
            .then(function (response) {
                let data = response.data.substring(response.data.indexOf('<pre>') + 5, response.data.indexOf('</pre>'));
                let resultList = data.split('\n').filter(entry => entry.substring(0, 2) === 'A:').map(entry => entry.substring(2, entry.indexOf('#')).trim());
                self.props.updateShowResult({ type: 'examples', resultList: resultList });
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    onMouseMove(ev) {
        this.lastPos.x = ev.clientX;
        this.lastPos.y = ev.clientY;
        this.lastTarget = ev.target;

        if (ev.target.id === 'yomi-text') {
            tryToFindTextAtMouse(ev, this.defaultDict, this.props.updateSearchResult);
        }
    }

    onMouseClick(ev) {
        if (ev.target.id === 'yomi-text') {
            this.props.updateShowResult(this.props.searchResult);
            this.showPopup(window.rikaiR.prevTarget);
        } else if (ev.target.id !== 'more-btn') {
            let popupFound = false;
            let element = ev.target;
            while (element.parentElement !== null) {
                if (ev.target.id === 'rikai-window' || element.parentElement.id === 'rikai-window') {
                    popupFound = true;
                    break;
                }
                element = element.parentElement;
            }
            if (!popupFound) {
                this.hidePopup();
            }
        }
    }

    hidePopup() {
        let popup = document.getElementById('rikai-window');
        if (popup) {
            popup.style.display = 'none';
        }
    }

    showPopup(elem) {
        let topdoc = window.document;

        let x, y;

        let popup = topdoc.getElementById('rikai-window');

        // calculate position
        if (elem) {
            popup.style.top = '-1000px';
            popup.style.left = '0px';

            // make the popup visible and gets its height and width
            popup.style.display = 'block';
            let pW = popup.offsetWidth;

            // below the selection
            x = window.rikaiR.elemPosition.left;
            y = window.rikaiR.elemPosition.bottom;

            y += document.documentElement.scrollTop || document.body.scrollTop;

            //go left if necessary
            if ((x + pW) > (window.innerWidth - 20)) {
                x -= Math.abs(window.innerWidth - x - 20 - pW);
            }
        } else {
            x += window.scrollX;
            y += window.scrollY;
        }

        popup.style.left = x + 'px';
        popup.style.top = y + 'px';
        popup.style.display = 'block';
    }

    render() {
        return (
            <div>
                <div id="yomi-text">{this.props.text}</div>
                <RikaiPopUp hidePopup={this.hidePopup} result={this.props.showResult} limit={this.props.limit}
                            showExamples={this.showExamples}/>
            </div>
        );
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(YomiText);
