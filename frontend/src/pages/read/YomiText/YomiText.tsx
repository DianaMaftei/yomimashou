import React from 'react';
import { connect } from 'react-redux';
import RikaiPopUp from './Rikai/Rikai';
import './Rikai/rikai.scss';
import './yomi.scss';
import { highlightMatch, isVisible, search, tryToFindTextAtMouse } from './Rikai/RikaiTextParser';
import KuromojiAnalyzer from 'kuroshiro-analyzer-kuromoji/dist/kuroshiro-analyzer-kuromoji.min';
import Kuroshiro from 'kuroshiro';
import LinearProgress from '@material-ui/core/LinearProgress';
import { filterTextFuriganaByKanjiCategory } from './TextActions/furigana/FuriganaFilterByKanjiCategory';
import SearchType from './Rikai/SearchType';
import TextInfo from '../../../components/textInfo/TextInfo';
import TextActions from './TextActions/TextActions';
import PopupType from './Rikai/PopupType';
import Slide from '@material-ui/core/Slide';
import ActionButton from '../../../components/buttons/actionBtn/ActionButton';
import {
    setAnalyzerAction,
    setFuriganaSentenceAction,
    setFuriganaTextAction,
    setFuriganaTitleAction,
    switchDictionaryAction,
    updateTextSelectInfoAction
} from '../readActions';
import {
    fetchDataAction,
    fetchTranslationAction,
    fetchWordExamplesAction,
    fetchWordListAction,
    setKanjiLevelsAction,
    setPopupInfoAction,
    updateSearchResultAction
} from './Rikai/popUpActions';
import { markTextAsRead } from '../../../service/TextService';
import { isAuthenticated } from '../../../service/AuthService';


const mapStateToProps = (state) => ({
    words: state.yomiText.words,
    names: state.yomiText.names,
    analyzer: state.yomiText.analyzer,
    textSelectInfo: state.yomiText.textSelectInfo,
    searchResult: state.popUp.searchResult,
    previousSearchResult: state.popUp.previousSearchResult,
    showResult: state.popUp.showResult,
    currentDictionary: state.config.popUp.currentDictionary,
    popupInfo: state.popUp.popupInfo,
    kanjiLevels: state.config.kanjiLevels,
    limit: state.popUp.popupInfo.limit,
    number: state.popUp.popupInfo.number,
    showTextActions: state.yomiText.showTextActions
});

const mapDispatchToProps = (dispatch) => ({
    updateTextSelectInfo: textSelectInfo => dispatch(updateTextSelectInfoAction(textSelectInfo)),
    fetchData: (list, url, number, limit) => dispatch(fetchDataAction(url, list.toString(), number, limit)),
    fetchWordExamples: (list, url, page, limit) => dispatch(fetchWordExamplesAction(url, list.toString(), page, limit)),
    updateSearchResult: result => dispatch(updateSearchResultAction(result)),
    setPopupInfo: popupInfo => dispatch(setPopupInfoAction(popupInfo)),
    switchDictionary: () => dispatch(switchDictionaryAction()),
    setFuriganaText: text => dispatch(setFuriganaTextAction(text)),
    setFuriganaTitle: title => dispatch(setFuriganaTitleAction(title)),
    setFuriganaSentence: sentence => dispatch(setFuriganaSentenceAction(sentence)),
    setAnalyzer: analyzer => dispatch(setAnalyzerAction(analyzer)),
    setKanjiLevels: (kanjiLevels) => dispatch(setKanjiLevelsAction(kanjiLevels)),
    fetchWordList: (sentence) => dispatch(fetchWordListAction(sentence)),
    fetchTranslation: (sentence) => dispatch(fetchTranslationAction(sentence))
});

export class YomiText extends React.Component {
    constructor(props) {
        super(props);

        this.props.setAnalyzer(null);

        this.analyzer = new KuromojiAnalyzer({
                                                 dictPath: '/static/kuromoji'
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
            (this.props.analyzer !== nextProps.analyzer) ||
            (this.props.currentDictionary !== nextProps.currentDictionary) ||
            (this.props.words !== nextProps.words) ||
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
        if (this.props.popupInfo.closed) {
            setPopupInfo({
                             ...this.props.popupInfo,
                             closed: false
                         });
            return;
        }

        if (!searchResult.type) {
            return;
        }

        let self = this;

        let number = this.props.previousSearchResult.result !== this.props.searchResult.result ? 0 : this.props.number + 1;

        if (!isVisible()) {
            if (searchResult.type === SearchType.WORD) {
                fetchData(searchResult.result.data.map(item => item.word), searchResult.type, number, this.props.limit);
                this.props.fetchWordExamples(searchResult.result.data[0].word, SearchType.EXAMPLE, 0, 3);
            } else if (searchResult.type === SearchType.KANJI) {
                fetchData(searchResult.result, searchResult.type);
            } else if (searchResult.type === SearchType.NAME) {
                fetchData(searchResult.result.data, searchResult.type);
            } else if (searchResult.type === SearchType.SENTENCE) {
                this.props.setPopupInfo({...this.props.popupInfo, type: PopupType.SENTENCE});
                this.props.fetchWordList(searchResult.result);
                this.props.fetchTranslation(searchResult.result);
                this.kuroshiro.convert(searchResult.result, {
                    to: 'hiragana',
                    mode: 'furigana'
                }).then(function (result) {
                    self.props.setFuriganaSentence(result);
                });
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

    handleFurigana = (event) => {

        let kanjiLevels = this.props.kanjiLevels;
        kanjiLevels[event.target.value] = event.target.checked;
        this.props.setKanjiLevels(kanjiLevels);

        let setFuriganaText = this.props.setFuriganaText;
        let setFuriganaTitle = this.props.setFuriganaTitle;

        let text = this.props.text.content;
        let title = this.props.text.title;

        this.kuroshiro.convert(text, {
            to: 'hiragana',
            mode: 'furigana'
        }).then(function (result) {
            setFuriganaText(filterTextFuriganaByKanjiCategory(result, kanjiLevels));
        });

        this.kuroshiro.convert(title, {
            to: 'hiragana',
            mode: 'furigana'
        }).then(function (result) {
            setFuriganaTitle(filterTextFuriganaByKanjiCategory(result, kanjiLevels));
        });
    };

    render() {
        return (
            <div id="yomi-text" className={this.getClassForDictionary()}>
                <Slide direction="down" in={this.props.showTextActions} mountOnEnter unmountOnExit>
                    <div id="text-actions">
                        <TextActions textContent={this.props.text.content} handleFurigana={this.handleFurigana}
                                     kanjiLevels={this.props.kanjiLevels} textTitle={this.props.text.title}/>
                    </div>
                </Slide>
                <div id="yomi-text-info">
                    <TextInfo text={this.props.text}/>
                </div>
                {(!this.props.words || this.props.words.length === 0) && <LinearProgress id="linear-progress"/>}
                <div>
                    <h3 id="yomi-text-title">{this.props.text.title}</h3>
                    <hr className="MuiDivider-root"/>
                </div>
                <div id="yomi-text-box"
                     onMouseMove={(ev) => this.onMouseMove(ev, this.props.updateSearchResult, this.props.currentDictionary, this.props.updateTextSelectInfo, this.props.words, this.props.names)}
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

export default connect(mapStateToProps, mapDispatchToProps)(YomiText);
