import React from 'react';
import { connect } from 'react-redux';
import ActionButton from '../../../components/buttons/actionBtn/ActionButton';
import { isAuthenticated } from '../../../service/AuthService';
import { markTextAsRead } from '../../../service/TextService';
import { setFuriganaSentenceAction, switchDictionaryAction, updateTextSelectInfoAction } from '../readActions';
import {
    fetchDataAction,
    fetchTranslationAction,
    fetchWordExamplesAction,
    fetchWordListAction,
    setPopupInfoAction,
    updateSearchResultAction, updateShowResultAction
} from './Rikai/popUpActions';
import PopupType from './Rikai/PopupType';
import RikaiPopUp from './Rikai/Rikai';
import './Rikai/rikai.scss';
import { highlightMatch, isVisible, search, tryToFindTextAtMouse } from './Rikai/RikaiTextParser';
import SearchType from './Rikai/SearchType';
import './yomi.scss';


const mapStateToProps = (state) => ({
    textSelectInfo: state.readText.textSelectInfo,
    searchResult: state.popUp.searchResult,
    previousSearchResult: state.popUp.previousSearchResult,
    currentDictionary: state.config.popUp.currentDictionary,
    popupInfo: state.popUp.popupInfo,
    limit: state.popUp.popupInfo.limit,
    number: state.popUp.popupInfo.number
});

const mapDispatchToProps = (dispatch) => ({
    updateTextSelectInfo: textSelectInfo => dispatch(updateTextSelectInfoAction(textSelectInfo)),
    fetchData: (list, url, number, limit) => dispatch(fetchDataAction(url, list.toString(), number, limit)),
    fetchWordExamples: (list, url, page, limit) => dispatch(fetchWordExamplesAction(url, list.toString(), page, limit)),
    updateSearchResult: result => dispatch(updateSearchResultAction(result)),
    updateShowResult: result => dispatch(updateShowResultAction(result)),
    setPopupInfo: popupInfo => dispatch(setPopupInfoAction(popupInfo)),
    switchDictionary: () => dispatch(switchDictionaryAction()),
    setFuriganaSentence: sentence => dispatch(setFuriganaSentenceAction(sentence)),
    fetchWordList: (sentence) => dispatch(fetchWordListAction(sentence)),
    fetchTranslation: (sentence) => dispatch(fetchTranslationAction(sentence))
});

export class TextContainer extends React.Component {
    constructor(props) {
        super(props);
        this.keyDownHandler = this.onKeyDown.bind(this);

        window.speechSynthesis.onvoiceschanged = function (e) {
            window.speechSynthesis.getVoices();
        };
    }

    componentDidMount() {
        document.addEventListener('keydown', this.keyDownHandler, false);

        let element = document.getElementById('yomi-text-container');
        element.innerHTML = (this.props.text && (this.props.text.furigana || this.props.text.content)) || '';
    }

    componentWillUnmount() {
        document.removeEventListener('keydown', this.keyDownHandler, false);
    }

    shouldComponentUpdate(nextProps) {
        return (this.props.text !== nextProps.text) ||
            (this.props.currentDictionary !== nextProps.currentDictionary) ||
            (this.props.analyzedText !== nextProps.analyzedText) ||
            (this.props.kanjiLevel !== nextProps.kanjiLevel) ||
            (this.props.showTextActions !== nextProps.showTextActions);
    }

    componentWillUpdate(nextProps, nextState) {
        if (this.props.text !== nextProps.text) {
            let textElement = document.getElementById('yomi-text-container');
            textElement.innerHTML = (nextProps.text && (nextProps.text.furigana || nextProps.text.content)) || '';

            // let titleElement = document.getElementById("yomi-text-title");
            // titleElement.innerHTML = (nextProps.text && (nextProps.text.furiganaTitle || nextProps.text.title)) || '';
        }
    }

    onMouseClick(ev, searchResult, fetchData, setPopupInfo) {
        if (!searchResult.type) {
            return;
        }

        let self = this;

        if (!isVisible()) {
            if (searchResult.type === SearchType.WORD) {
                const word = self.props.analyzedText.words.find(word => word.kanjiElements.includes(searchResult.result.data.baseForm) ||
                word.readingElements.includes(searchResult.result.data.baseForm));
                self.props.updateShowResult({
                    result: word,
                    type: searchResult.type
                 });
                this.props.fetchWordExamples(searchResult.result.data.baseForm, SearchType.EXAMPLE, 0, 3);
            } else if (searchResult.type === SearchType.KANJI) {
                fetchData(searchResult.result, searchResult.type);
            } else if (searchResult.type === SearchType.NAME) {
                fetchData(searchResult.result.data, searchResult.type);
            } else if (searchResult.type === SearchType.SENTENCE) {
                this.props.setPopupInfo({...this.props.popupInfo, type: PopupType.SENTENCE});
                this.props.fetchWordList(searchResult.result);
                this.props.fetchTranslation(searchResult.result);
                // this.kuroshiro.convert(searchResult.result, {
                //     to: 'hiragana',
                //     mode: 'furigana'
                // }).then(function (result) {
                //     self.props.setFuriganaSentence(result);
                // });
            }

            if (!document.getSelection().getRangeAt(0).getClientRects()[0]) {
                return;
            }

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
                            1: '#f0a0a8',
                            2: '#68b4ee',
                            3: '#6fbca7'
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
        let previousSentence = '';
        for(let i = textAtMouseInfo.prevRangeOfs - 1; i >= 0; i--) {
            if (allText[i] !== '。') {
                previousSentence += allText[i];
            } else {
                break;
            }
        }

        updateSearchResult({
                               type: SearchType.SENTENCE,
                               result: (previousSentence.split('').reverse().join('') + '。').trim()
                           });

        let highlightInfo = {
            selEndList: [{offset: 0, node: textAtMouseInfo.prevRangeNode}],
            matchLen: 1,
            lastRo: textAtMouseInfo.prevRangeOfs,
            prevRangeNode: textAtMouseInfo.prevRangeNode

        };

        highlightMatch(highlightInfo, '#11bc0e');
    }

    getClassForDictionary() {
        return this.props.currentDictionary === 2 ? 'word-dict' : 'kanji-dict';
    }

    onKeyDown(ev) {
        switch(ev.keyCode) {
            case 192:	// ` - switch dictionaries (kanji, names, words, examples)
                this.props.switchDictionary();
                this.props.setPopupInfo({
                                            ...this.props.popupInfo,
                                            type: this.props.popupInfo.type === PopupType.WORD ? PopupType.KANJI : PopupType.WORD
                                        });
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

    render() {
        return (
            <div id="yomi-text" className={this.getClassForDictionary()}>

                <div>
                    <h3 id="yomi-text-title">{this.props.text.title}</h3>
                    <hr className="MuiDivider-root"/>
                </div>
                <div id="yomi-text-box"
                     onMouseMove={(ev) => this.onMouseMove(ev, this.props.updateSearchResult, this.props.currentDictionary, this.props.updateTextSelectInfo, this.props.text.parsedWords, this.props.names)}
                     onClick={(ev) => this.onMouseClick(ev, this.props.searchResult, this.props.fetchData, this.props.setPopupInfo)}
                >
                    <div id="yomi-text-container"/>
                </div>
                {
                    //TODO use mark as read action
                    isAuthenticated() &&
                    <ActionButton onClick={() => markTextAsRead(this.props.id)} label="Mark as Read"/>
                }
                <RikaiPopUp/>
            </div>);
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(TextContainer);
