import React from "react";
import { connect } from "react-redux";
import axios from "axios";
import RikaiPopUp from "../Rikai/Rikai";
import '../Rikai/Rikai.css';
import { highlightMatch, search, tryToFindTextAtMouse } from "../../util/rikai/RikaiTextParser";
import apiUrl from "../../AppUrl";

const mapStateToProps = (state) => ({
    text: state.viewYomi.text,
    textSelectInfo: state.yomiText.textSelectInfo,
    searchResult: state.popUp.searchResult,
    showResult: state.popUp.showResult,
    limit: state.config.popUp.limit,
    currentDictionary: state.config.popUp.currentDictionary,
    popupInfo: state.popUp.popupInfo
});

const mapDispatchToProps = (dispatch) => ({
    updateTextSelectInfo: textSelectInfo => {
        dispatch({
            type: 'UPDATE_TEXT_SELECT_INFO',
            textSelectInfo
        });
    },
    fetchData: (list, url) => {
        dispatch({
            type: 'FETCH_DATA',
            payload: axios.get(apiUrl + '/api/' + url + '?searchItem=' + list.toString())
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
    }
});

export class YomiText extends React.Component {
    constructor(props) {
        super(props);
        window.addEventListener('keydown', this.onKeyDown.bind(this), true);
    }

    shouldComponentUpdate(nextProps, nextState) {
        return this.props.text !== nextProps.text;
    }

    onMouseClick(searchResult, fetchData, setPopupInfo) {
        if (searchResult.type === "words") {
            fetchData(searchResult.result.data.map(item => item.word), searchResult.type);
        } else if (searchResult.type === "kanji") {
            fetchData(searchResult.result, searchResult.type);
        }

        setPopupInfo({
            visibility: true, position: {
                x: document.getSelection().getRangeAt(0).getClientRects()[0].left,
                y: document.getSelection().getRangeAt(0).getClientRects()[0].bottom
            }
        });
    };

    onMouseMove(ev, updateSearchResult, currentDictionary, updateTextSelectInfo) {
        let textSelectInfo = tryToFindTextAtMouse(ev);

        let searchResult = search(textSelectInfo, currentDictionary, updateSearchResult);

        if (searchResult) {
            let entries = searchResult.entries;

            if (entries) {
                updateSearchResult(entries);
            }

            if (textSelectInfo && entries) {
                textSelectInfo.uofsNext = entries.matchLen;
                textSelectInfo.uofs = (textSelectInfo.prevRangeOfs + textSelectInfo.uofs - textSelectInfo.prevRangeOfs);
                textSelectInfo.prevSelView = textSelectInfo.prevRangeNode.ownerDocument.defaultView;
                textSelectInfo.lastRo = searchResult.lastRo;
                textSelectInfo.lastSelEnd = searchResult.lastSelEnd;

                updateTextSelectInfo(textSelectInfo);

                highlightMatch(textSelectInfo.lastRo, entries.result.matchLen, textSelectInfo.lastSelEnd, textSelectInfo);
            }
        }
    };

    onKeyDown(ev) {
        switch (ev.keyCode) {
            case 83:	// s - switch dictionaries (kanji, names, words, examples)
                this.props.switchDictionary();
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

    render() {
        return <div id="yomi-text">
            <div id="yomi-text-container"
                 onClick={(ev) => this.onMouseClick(this.props.searchResult, this.props.fetchData, this.props.setPopupInfo)}
                 onMouseMove={(ev) => this.onMouseMove(ev, this.props.updateSearchResult, this.props.currentDictionary, this.props.updateTextSelectInfo)}>
                {this.props.text}

            </div>

            <RikaiPopUp/>
        </div>;
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(YomiText);