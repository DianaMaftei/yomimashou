import React from "react";
import { connect } from "react-redux";
import axios from "axios";
import RikaiPopUp from "./Rikai/Rikai";
import './Rikai/Rikai.css';
import './yomi.css';
import { highlightMatch, isVisible, search, tryToFindTextAtMouse } from './Rikai/RikaiTextParser';
import KuromojiAnalyzer from "kuroshiro-analyzer-kuromoji/dist/kuroshiro-analyzer-kuromoji.min";
import Kuroshiro from "kuroshiro";
import LinearProgress from '@material-ui/core/LinearProgress';
import {apiUrl} from "../../../AppUrl";
import DownloadIcon from 'mdi-react/DownloadIcon';
import { isAuthenticated, withHeaders } from "../../../auth/auth";
import { filterTextFuriganaByKanjiCategory } from "./furigana/FuriganaFilterByKanjiCategory";
import FuriganaOptions from "./furigana/FuriganaOptions";
import SearchType from "./Rikai/SearchType";

const mapStateToProps = (state) => ({
    words: state.add.words,
    names: state.add.names,
    analyzer: state.add.analyzer,
    textSelectInfo: state.yomiText.textSelectInfo,
    searchResult: state.popUp.searchResult,
    previousSearchResult: state.popUp.previousSearchResult,
    showResult: state.popUp.showResult,
    currentDictionary: state.config.popUp.currentDictionary,
    popupInfo: state.popUp.popupInfo,
    kanjiLevel: state.config.kanjiLevel,
    limit: state.popUp.popupInfo.limit,
    number: state.popUp.popupInfo.number,
});

const mapDispatchToProps = (dispatch) => ({
    updateTextSelectInfo: textSelectInfo => {
        dispatch({
            type: 'UPDATE_TEXT_SELECT_INFO',
            textSelectInfo
        });
    },
    fetchData: (list, url, number, limit) => {
        dispatch({
            type: 'FETCH_DATA',
            payload: axios.get(apiUrl + '/api/dictionary/' + url + '?searchItem=' + list.toString() + `&page=${number}&size=${limit}`)
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
    },
    setFuriganaTitle: title => {
        dispatch({
            type: 'SET_FURIGANA_TITLE',
            title
        });
    },
    setFuriganaSentence: sentence => {
        dispatch({
            type: 'SET_FURIGANA_SENTENCE',
            sentence
        });
    }, setAnalyzer: analyzer => {
        dispatch({
            type: 'SET_ANALYZER',
            analyzer
        });
    }, setKanjiLevel: (event, value) => {
        dispatch({
            type: 'SET_KANJI_LEVEL',
            kanjiLevel: value
        });
    }, fetchWordList: (sentence) => {
        dispatch({
            type: 'FETCH_WORD_LIST',
            payload: axios.post(apiUrl + '/api/sentence/', sentence)
        });
    }, fetchTranslation: (sentence) => {
        dispatch({
            type: 'FETCH_TRANSLATION',
            payload: axios.get('https://api.mymemory.translated.net/get?q=' + sentence +  '&langpair=ja|en')
        })
    }
});

export class YomiText extends React.Component {
    constructor(props) {
        super(props);

        this.props.setAnalyzer(null);

        this.analyzer = new KuromojiAnalyzer({
            dictPath: "/static/kuromoji"
        });

        this.kuroshiro = new Kuroshiro();
        let that = this;
        this.kuroshiro.init(this.analyzer).then(function () {
            that.props.setAnalyzer(that.kuroshiro._analyzer);
        });

        this.keyDownHandler = this.onKeyDown.bind(this);

        window.speechSynthesis.onvoiceschanged = function (e) {
            window.speechSynthesis.getVoices();
        };

        this.isAuthenticated = isAuthenticated();
    }

    componentDidMount() {
        document.addEventListener("keydown", this.keyDownHandler, false);

        let element = document.getElementById("yomi-text-container");
        element.innerHTML = (this.props.text && (this.props.text.furigana || this.props.text.content)) || '';
    }

    componentWillUnmount() {
        document.removeEventListener("keydown", this.keyDownHandler, false);
    }

    shouldComponentUpdate(nextProps) {
        return (this.props.text !== nextProps.text) ||
            (this.props.analyzer !== nextProps.analyzer) ||
            (this.props.currentDictionary !== nextProps.currentDictionary) ||
            (this.props.words !== nextProps.words) ||
            (this.props.kanjiLevel !== nextProps.kanjiLevel);
    }

    componentWillUpdate(nextProps, nextState) {
        if (this.props.text !== nextProps.text) {
            let textElement = document.getElementById("yomi-text-container");
            textElement.innerHTML = (nextProps.text && (nextProps.text.furigana || nextProps.text.content)) || '';

            let titleElement = document.getElementById("yomi-text-title");
            titleElement.innerHTML = (nextProps.text && (nextProps.text.furiganaTitle || nextProps.text.title)) || '';
        }
    }

    onMouseClick(ev, searchResult, fetchData, setPopupInfo) {
        if (!searchResult.type) return;

        let self = this;

        let number = this.props.previousSearchResult.result !== this.props.searchResult.result ?0: this.props.number+1;

        if (!isVisible()) {
            if (searchResult.type === SearchType.WORD) {
                fetchData(searchResult.result.data.map(item => item.word), searchResult.type, number, this.props.limit);
            } else if (searchResult.type === SearchType.KANJI) {
                fetchData(searchResult.result, searchResult.type);
            } else if (searchResult.type === SearchType.NAME) {
                fetchData(searchResult.result.data, searchResult.type);
            } else if (searchResult.type === SearchType.SENTENCE) {
                this.props.fetchWordList(searchResult.result);
                this.props.fetchTranslation(searchResult.result);
                this.kuroshiro.convert(searchResult.result, {
                    to: "hiragana",
                    mode: "furigana"
                }).then(function (result) {
                    self.props.setFuriganaSentence(result);
                });
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
                if (searchResult.isPointerAtFullStop) {
                    this.handleEndOfSentence(textAtMouseInfo, updateSearchResult);
                } else {
                    let entries = searchResult.entries;
                    if (!entries || !entries.result) {
                        return;
                    } else {
                        if (entries.type === SearchType.WORD && (!entries.result.data || entries.result.data.length === 0)) {
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

                        highlightMatch(textAtMouseInfo, highlightColors[currentDictionary]);
                    }
                }

            }
        }
    };

    handleEndOfSentence(textAtMouseInfo, updateSearchResult) {
        window.getSelection().empty();

        let allText = textAtMouseInfo.prevRangeNode.textContent;
        let previousSentence = "";
        for (let i = textAtMouseInfo.prevRangeOfs - 1; i >= 0; i--) {
            if (allText[i] !== "。") {
                previousSentence += allText[i];
            } else {
                break;
            }
        }

        updateSearchResult({
            type: SearchType.SENTENCE,
            result: (previousSentence.split("").reverse().join("") + "。").trim()
        });

        let highlightInfo = {
            selEndList: [{offset: 0, node: textAtMouseInfo.prevRangeNode}],
            matchLen: 1,
            lastRo: textAtMouseInfo.prevRangeOfs,
            prevRangeNode: textAtMouseInfo.prevRangeNode,

        };

        highlightMatch(highlightInfo, "#11bc0e");
    }

    getClassForDictionary() {
        return this.props.currentDictionary === 2 ? "word-dict" : "kanji-dict";
    }

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

    toggleFurigana = () => {
        if (this.props.kanjiLevel === "None") {
            this.props.setFuriganaText(null);
            this.props.setFuriganaTitle(null);

        } else {
            let setFuriganaText = this.props.setFuriganaText;
            let setFuriganaTitle = this.props.setFuriganaTitle;

            let text = this.props.text.content;
            let title = this.props.text.title;

            let self = this;
            this.kuroshiro.convert(text, {
                to: "hiragana",
                mode: "furigana"
            }).then(function (result) {
                setFuriganaText(filterTextFuriganaByKanjiCategory(result, self.props.kanjiLevel));
            });

            this.kuroshiro.convert(title, {
                to: "hiragana",
                mode: "furigana"
            }).then(function (result) {
                setFuriganaTitle(filterTextFuriganaByKanjiCategory(result, self.props.kanjiLevel));
            });
        }
    };

    markAsRead = () => {
        axios.post(apiUrl + '/api/users/textStatus?progressStatus=READ&textId=' + this.props.id, {}, withHeaders());
    };

    render() {
        return (
            <div id="yomi-text" className={this.getClassForDictionary()}>
                {(!this.props.words || this.props.words.length === 0) && <LinearProgress/>}
                <div id="yomi-text-options">
                    <FuriganaOptions analyzer={this.props.analyzer} toggleFurigana={this.toggleFurigana} kanjiLevel={this.props.kanjiLevel} setLevel={this.props.setKanjiLevel}/>
                    {
                        this.isAuthenticated &&
                        <button className="btn btn-light" id="mark-read"
                                onClick={this.markAsRead}>
                            Mark as Read
                        </button>
                    }
                    <form id="dld-tts" method="POST"
                          action={'https://talkify.net/api/speech/v1/download?key=' + process.env.REACT_APP_TALKIFY_KEY}>
                        <input type="hidden" name="text" value={this.props.text.content}/>
                        <button type="submit" className="btn btn-light" id="dld-tts-btn">
                            <DownloadIcon size={24}/>
                        </button>
                    </form>
                </div>
                <div id="yomi-text-box"
                     onMouseMove={(ev) => this.onMouseMove(ev, this.props.updateSearchResult, this.props.currentDictionary, this.props.updateTextSelectInfo, this.props.words, this.props.names)}
                     onClick={(ev) => this.onMouseClick(ev, this.props.searchResult, this.props.fetchData, this.props.setPopupInfo)}
                >
                    <h3 id="yomi-text-title">{this.props.text.title}</h3>
                    <br/>
                    <div id="yomi-text-container"/>
                </div>
                <RikaiPopUp/>
            </div>);
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(YomiText);