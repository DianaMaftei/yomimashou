import React from "react";
import { connect } from "react-redux";
import { fetchData, updateSearchResult, updateShowResult } from "../actions/index";
import { isVisible, search, tryToFindTextAtMouse } from "../util/rikai/RikaiTextParser";
import RikaiPopUp from "../components/rikai/Rikai-pop-up";

const mapStateToProps = (state) => ({
    text: state.yomi.text,
    searchResult: state.popUp.searchResult,
    showResult: state.popUp.showResult,
    limit: state.config.popUp.limit
});

const mapDispatchToProps = (dispatch) => ({
    updateSearchResult: result => {
        dispatch(updateSearchResult(result));
    },
    updateShowResult: result => {
        dispatch(updateShowResult(result));
    },
    fetchData: (list, url) => {
        dispatch(fetchData(list, url));
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

        this.showWait = false;

        // Hack because ro was coming out always 0 for some reason.
        this.lastRo = 0;

        this.onMouseMove = this.onMouseMove.bind(this);
        this.onMouseClick = this.onMouseClick.bind(this);
        this.onKeyDown = this.onKeyDown.bind(this);
        this.onKeyUp = this.onKeyUp.bind(this);
        this.onTouch = this.onTouch.bind(this);

        this.hidePopup = this.hidePopup.bind(this);
        this.showPopup = this.showPopup.bind(this);
        this.showExamples = this.showExamples.bind(this);
        this.getResultFromEntry = this.getResultFromEntry.bind(this);
        this.sortByRelevanceAndAddConjugation = this.sortByRelevanceAndAddConjugation.bind(this);
        this.fetchData = this.fetchData.bind(this);

        this.enable();
    }

    shouldComponentUpdate(nextProps) {
        return this.props.showResult !== nextProps.showResult;
    }

    componentDidUpdate(prevProps) {
        if (prevProps.showResult !== this.props.showResult) {
            this.showPopup(window.rikaiR.prevTarget);
        }
    }

    finishSearching(result) {
        this.props.finishSearching();
        this.props.updateSearchResult(result);
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
                this.fetchData(this.props.searchResult);
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
                this.fetchData(this.props.searchResult);
                break;
            case 77:	// m - next character
                ev.currentTarget.rikaiR.uofsNext = 1;

            // eslint-disable-next-line
            case 78:	// n - next word
                for (i = 50; i > 0; --i) {
                    ev.currentTarget.rikaiR.uofs += ev.currentTarget.rikaiR.uofsNext;
                    if (search(ev.currentTarget.rikaiR, this.defaultDict, this.props.updateSearchResult) >= 0) break;
                }
                this.fetchData(this.props.searchResult);
                break;
            default:
                return;
        }

        this.keysDown[ev.keyCode] = 1;

        // don't eat shift
        ev.preventDefault();
    }

    fetchData(searchResult) {
        if (searchResult.type === "words") {
            this.props.fetchData(searchResult.result.data.map(item => item.word), searchResult.type);
        } else if (searchResult.type === "kanji") {
            this.props.fetchData(searchResult.result, searchResult.type);
        }
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
            window.addEventListener('touchstart', this.onTouch, true);
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
                self.props.updateShowResult({ type: 'examples', result: resultList });
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

    onTouch(ev) {
        this.showWait = true;
        let self = this;

        setTimeout(function () {
            self.showWait = false;
            self.onMouseClick(ev);
        }, 200);
    }

    onMouseClick(ev) {
        if(this.showWait) {
            return;
        }

        if (ev.target.id === 'yomi-text') {
            this.fetchData(this.props.searchResult);
            this.showPopup(window.rikaiR.prevTarget);
            return;
        }

        if (ev.target.id !== 'more-btn') {
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

    sortByRelevanceAndAddConjugation(searchedElements, fetchedList) {
        let sortedList = [];
        let words = searchedElements.map(item => item.word);
        let conjugations = searchedElements.map(item => item.grammarPoint);

        for (let i = 0; i < words.length; i++) {
            for (let j = 0; j < fetchedList.length; j++) {
                if (fetchedList[j].kanjiElements.indexOf(words[i]) > -1 || fetchedList[j].readingElements.indexOf(words[i]) > -1) {
                    fetchedList[j].grammarPoint = conjugations[i];
                    sortedList.push(fetchedList[j]);
                    fetchedList.splice(j, 1);
                    j--;
                }
            }
        }

        return sortedList
    }

    getResultFromEntry(searchResult, fetchedResult) {
        if (fetchedResult.result === null) return null;
        if (fetchedResult.type === "words") {
            let result = [];
            let resultList = this.sortByRelevanceAndAddConjugation(searchResult.data, fetchedResult.result.slice());
            for (let i = 0; i < resultList.length; i++) {
                let word = {};
                word.kanji = resultList[i].kanjiElements !== "" ? resultList[i].kanjiElements.split("|") : null
                word.kana = resultList[i].readingElements.split("|");
                word.longDef = resultList[i].meanings.map(meaning => ((meaning.partOfSpeech ? "(" + meaning.partOfSpeech + ") " : "") + meaning.glosses.replace(/\|/g, ", "))).join("; ");
                word.showShortDef = word.longDef.length > 140 ? word.longDef.substring(0, 140) : null;
                word.grammar = resultList[i].grammarPoint !== "" ? resultList[i].grammarPoint : null;
                result.push(word);
            }

            return { type: 'words', result: result };

        } else if (fetchedResult.type === "kanji") {
            let kanji = {};
            kanji.frequency = fetchedResult.result.frequency;
            kanji.grade = fetchedResult.result.grade;
            kanji.strokes = fetchedResult.result.strokeCount;
            kanji.eigo = fetchedResult.result.meaning.replace(/\|/g, ", ");
            kanji.kanji = fetchedResult.result.kanji;
            kanji.kunReading = fetchedResult.result.kunReading.replace(/\|/g, ", ");
            kanji.onReading = fetchedResult.result.onReading.replace(/\|/g, ", ");

            return { type: 'kanji', result: kanji };
        }
    }

    render() {

        let result = null;
        if (this.props.searchResult.result) {
            result = this.getResultFromEntry(this.props.searchResult.result, this.props.showResult);
        }

        if (this.props.showResult.type === "examples") {
            result = this.props.showResult;
        }

        return (
            <div>
                <div id="yomi-text">{this.props.text}</div>
                <RikaiPopUp hidePopup={this.hidePopup} result={result} limit={this.props.limit}
                            showExamples={this.showExamples}/>
            </div>
        );
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(YomiText);
