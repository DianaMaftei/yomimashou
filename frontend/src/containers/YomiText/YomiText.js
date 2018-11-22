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
import Trumbowyg from 'react-trumbowyg'

const mapStateToProps = (state) => ({
    words: state.add.words,
    names: state.add.names,
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
    }
});

export class YomiText extends React.Component {
    constructor(props) {
        super(props);

        // window.removeEventListener('keydown', this.onKeyDown.bind(this), true);
        // window.addEventListener('keydown', this.onKeyDown.bind(this), true);

        this.analyzer = new KuromojiAnalyzer({
            dictPath: "/kuromojiDict"
        });

        this.kuroshiro = new Kuroshiro();
        this.kuroshiro.init(this.analyzer);
    }

    componentDidMount() {
        window.addEventListener("keydown", this.onKeyDown.bind(this));
    }

    shouldComponentUpdate(nextProps) {
        return this.props.text !== nextProps.text;
    }

    onMouseClick(searchResult, fetchData, setPopupInfo) {
        if (!searchResult.type) return;

        if (!isVisible()) {
            if (searchResult.type === "words") {
                fetchData(searchResult.result.data.map(item => item.word), searchResult.type);
            } else if (searchResult.type === "kanji") {
                fetchData(searchResult.result, searchResult.type);
            } else if (searchResult.type === "names") {
                fetchData(searchResult.result.data, searchResult.type);
            }

            if(!document.getSelection().getRangeAt(0).getClientRects()[0]) return;

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
                }

                if (!this.props.searchResult.result) {
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
        let btnsDef = {
            buttonName: {
                fn: () => alert("boo"),
                ico: 'insertImage'
            }
        };

        return <div id="yomi-text">
            <div id="yomi-text-container"
                 onMouseDown={ev => this.mouseDown = true}
                 onMouseUp={ev => this.mouseDown = false}
                 onClick={(ev) => this.onMouseClick(this.props.searchResult, this.props.fetchData, this.props.setPopupInfo)}
                 onMouseMove={(ev) => this.onMouseMove(ev, this.props.updateSearchResult, this.props.currentDictionary, this.props.updateTextSelectInfo, this.props.words, this.props.names)}>

                <div id="yomi-text-box">
                    <button id="toggle-furigana" onClick={this.showFurigana.bind(this)}> ルビ</button>
                    <Trumbowyg
                        id='react-trumbowyg'
                        data={this.props.text.furigana || this.props.text.content || '<p><br></p>'}
                        btnsDef={btnsDef}
                        buttons={[['buttonName']]}
                        disabled={true}
                    />
                </div>


            </div>

            <RikaiPopUp/>
        </div>;
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(YomiText);