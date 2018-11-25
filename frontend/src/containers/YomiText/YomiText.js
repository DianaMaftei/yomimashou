import React from "react";
import { connect } from "react-redux";
import axios from "axios";
import RikaiPopUp from "../Rikai/Rikai";
import '../Rikai/Rikai.css';
import './yomi.css';
import { highlightMatch, isVisible, search, tryToFindTextAtMouse } from "../../util/rikai/RikaiTextParser";
import apiUrl from "../../AppUrl";
import KuromojiAnalyzer from "kuroshiro-analyzer-kuromoji/dist/kuroshiro-analyzer-kuromoji.min";
import Kuroshiro from "kuroshiro";

const mapStateToProps = (state) => ({
    words: state.add.words,
    names: state.add.names,
    analyzer: state.add.analyzer,
    textSelectInfo: state.yomiText.textSelectInfo,
    searchResult: state.popUp.searchResult,
    showResult: state.popUp.showResult,
    limit: state.config.popUp.limit,
    currentDictionary: state.config.popUp.currentDictionary,
    popupInfo: state.popUp.popupInfo
});

const mapDispatchToProps = (dispatch) => ({
    setText: (text) => {
        dispatch({
            type: 'SET_HIGHLIGHTED_TEXT',
            text
        });
    },
    updateTextSelectInfo: textSelectInfo => {
        dispatch({
            type: 'UPDATE_TEXT_SELECT_INFO',
            textSelectInfo
        });
    },
    fetchData: (list, url) => {
        dispatch({
            type: 'FETCH_DATA',
            payload: axios.get(apiUrl + '/api/dictionary/' + url + '?searchItem=' + list.toString())
        });
    },
    updateSearchResult: result => {
        dispatch({
            type: 'UPDATE_SEARCH_RESULT',
            result
        });
    },
    setPopupInfo: popupInfo => {
        dispatch({
            type: 'SET_POPUP_INFO',
            popupInfo: popupInfo
        });
    },
    switchDictionary: () => {
        dispatch({
            type: 'SWITCH_DICTIONARY'
        });
    },
    setFuriganaText: text => {
        dispatch({
            type: 'SET_FURIGANA_TEXT',
            text
        });
    }, setAnalyzer: analyzer => {
        dispatch({
            type: 'SET_ANALYZER',
            analyzer
        });
    }
});

export class YomiText extends React.Component {
    constructor(props) {
        super(props);

        this.props.setAnalyzer(null);

        // window.removeEventListener('keydown', this.onKeyDown.bind(this), true);
        // window.addEventListener('keydown', this.onKeyDown.bind(this), true);

        this.analyzer = new KuromojiAnalyzer({
            dictPath: "/kuromojiDict"
        });

        this.kuroshiro = new Kuroshiro();
        let that = this;
        this.kuroshiro.init(this.analyzer).then(function () {
            that.props.setAnalyzer(that.kuroshiro._analyzer);
        })
    }

    componentDidMount() {
        window.addEventListener("keydown", this.onKeyDown.bind(this));

        let element = document.getElementById("yomi-text-container");
        element.innerHTML = (this.props.text && (this.props.text.furigana || this.props.text.content)) || '';
    }

    shouldComponentUpdate(nextProps) {
        return (this.props.text !== nextProps.text) || (this.props.analyzer !== nextProps.analyzer);
    }

    componentWillUpdate(nextProps, nextState) {
        if (this.props.text !== nextProps.text) {
            let element = document.getElementById("yomi-text-container");
            element.innerHTML = (nextProps.text && (nextProps.text.furigana || nextProps.text.content)) || '';
        }
    }

    onMouseClick(ev, searchResult, fetchData, setPopupInfo) {
        if (!searchResult.type) return;

        if (!isVisible()) {
            if (searchResult.type === "words") {
                fetchData(searchResult.result.data.map(item => item.word), searchResult.type);
            } else if (searchResult.type === "kanji") {
                fetchData(searchResult.result, searchResult.type);
            } else if (searchResult.type === "names") {
                fetchData(searchResult.result.data, searchResult.type);
            }

            if (!document.getSelection().getRangeAt(0).getClientRects()[0]) return;

            setPopupInfo({
                visibility: true, position: {
                    x: document.getSelection().getRangeAt(0).getClientRects()[0].x - 10,
                    y: document.getSelection().getRangeAt(0).getClientRects()[0].y - document.body.scrollTop + 25
                }
            });
        }
    };

    onMouseMove(ev, updateSearchResult, currentDictionary, updateTextSelectInfo, wordList, nameList) {
        if (!isVisible() && !this.mouseDown) {
            let textAtMouseInfo = tryToFindTextAtMouse(ev);

            let searchResult = search(textAtMouseInfo, currentDictionary, wordList, nameList);

            if (searchResult) {
                let entries = searchResult.entries;

                if (!entries || !entries.result) {
                    return;
                } else {
                    if (entries.type === "words" && (!entries.result.data || entries.result.data.length === 0)) {
                        return;
                    }
                }

                if (!this.props.searchResult || this.props.searchResult.result) {
                    updateSearchResult(entries);
                } else if (JSON.stringify(this.props.searchResult.result) !== JSON.stringify(entries.result)) {
                    updateSearchResult(entries);
                }

                if (textAtMouseInfo && entries) {
                    textAtMouseInfo.entries = JSON.stringify(entries.result.data);
                    textAtMouseInfo.matchLen = entries.result.matchLen || entries.matchLen;
                    textAtMouseInfo.uofs = (textAtMouseInfo.prevRangeOfs + textAtMouseInfo.uofs);
                    textAtMouseInfo.prevSelView = textAtMouseInfo.prevRangeNode.ownerDocument.defaultView;
                    textAtMouseInfo.lastRo = searchResult.lastRo;
                    textAtMouseInfo.selEndList = searchResult.selEndList;

                    updateTextSelectInfo(textAtMouseInfo);

                    let highlightColors = {
                        1: "#f0a0a8",
                        2: "#68b4ee",
                        3: "#6fbca7"
                    };

                    let highlightedText = highlightMatch(textAtMouseInfo, highlightColors[currentDictionary]);

                    // this.props.setText(highlightedText);
                }
            }
        }
    };

    onKeyDown(ev) {
        switch (ev.keyCode) {
            case 83:	// s - switch dictionaries (kanji, names, words, examples)
                this.props.switchDictionary();
                //TODO remove hightlight? or do a new search with the new dict option
                break;
            case 81:	// q - hide popup
                this.props.setPopupInfo({
                    visibility: false
                });
                break;
            default:
                return;
        }

        // don't eat shift
        ev.preventDefault();
    };

    showFurigana() {
        if (this.props.text.furigana) {
            this.props.setFuriganaText(null);

        } else {
            let setFuriganaText = this.props.setFuriganaText;
            let text = this.props.text.content;

            this.kuroshiro.convert(text, {
                to: "hiragana",
                mode: "furigana"
            }).then(function (result) {
                setFuriganaText(result);
            })
        }
    }

    render() {
        return <div id="yomi-text">
            <div id="yomi-text-options">
                <button className="btn btn-light" id="toggle-furigana"
                        disabled={!this.props.analyzer}
                        onClick={this.showFurigana.bind(this)}>
                    ルビ
                </button>
            </div>
            <div id="yomi-text-container"
                 onMouseDown={ev => this.mouseDown = true}
                 onMouseUp={ev => this.mouseDown = false}
                 onClick={(ev) => this.onMouseClick(ev, this.props.searchResult, this.props.fetchData, this.props.setPopupInfo)}
                 onMouseMove={(ev) => this.onMouseMove(ev, this.props.updateSearchResult, this.props.currentDictionary, this.props.updateTextSelectInfo, this.props.words, this.props.names)}>
            </div>

            <RikaiPopUp/>
        </div>;
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(YomiText);