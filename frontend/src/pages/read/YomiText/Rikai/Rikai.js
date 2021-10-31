import React from "react";
import { connect } from "react-redux";
import axios from "axios";
import RikaiLoading from './RikaiLoading';
import RikaiKanji from './Kanji/RikaiKanji';
import RikaiSentence from './Sentence/RikaiSentence';
import './rikai.scss';
import { apiUrl } from "../../../../AppUrl";
import SearchType from "./SearchType";
import RikaiWord from "./Word/RikaiWord";
import RtkInfo from "./Kanji/RtkInfo";
import KanjiDrawPad from "./Kanji/KanjiDrawPad/KanjiDrawPad";
import AddToDeck from "./AddToDeck/AddToDeck";
import OutsideClickHandler from 'react-outside-click-handler';
import PopupType from "./PopupType";

const mapStateToProps = (state) => ({
  ...state.popUp,
  last: state.popUp.popupInfo.last,
  limit: state.popUp.popupInfo.limit,
  number: state.popUp.popupInfo.number,
  searchResult: state.popUp.searchResult,
  previousSearchResult: state.popUp.previousSearchResult,
  wordExamples: state.popUp.wordExamples
});

const mapDispatchToProps = (dispatch) => ({
  setPopupInfo: popupInfo => {
    dispatch({
      type: 'SET_POPUP_INFO',
      popupInfo: popupInfo
    });
  }, fetchData: (list, url, number, limit) => {
    dispatch({
      type: 'FETCH_DATA',
      payload: axios.get(
          apiUrl + '/api/dictionary/' + url + '?searchItem=' + list.toString()
          + `&page=${number}&size=${limit}`)
    });
  }, updateSearchResult: result => {
    dispatch({
      type: 'UPDATE_SEARCH_RESULT',
      result
    });
  }, updateShowResult: result => {
    dispatch({
      type: 'UPDATE_SHOW_RESULT',
      result
    });
  }
});

const getPopupStyle = (popupInfo) => {

  let x, y;

  let popup = window.document.getElementById('rikai-window');

  if (!popupInfo || !popup) {
    return;
  }

  let styles = {};

  // calculate position
  if (popupInfo) {
    styles.top = '-1000px';
    styles.left = '0px';

    // make the popup visible and gets its height and width
    let pW = popup.offsetWidth;

    // below the selection
    if (popupInfo.position) {
      x = popupInfo.position.x;
      y = popupInfo.position.y;
    }

    y += document.documentElement.scrollTop || document.body.scrollTop;

    //go left if necessary
    if ((x + pW) > (window.innerWidth - 130)) {
      x -= Math.abs(window.innerWidth - x - 130 - pW);
    }
  } else {
    x += window.scrollX;
    y += window.scrollY;
  }

  // styles.left = x + 'px';
  styles.left = 20 + 'px';
  styles.top = y + 'px';
  styles.display = popupInfo.visibility ? 'block' : 'none';

  return styles;
};

const sortResultsByRelevanceAndAddConjugation = (searchedElements,
    fetchedList) => {
  if (!searchedElements) {
    return;
  }

  let sortedList = [];
  let words = searchedElements.map(item => item.word);
  let conjugations = searchedElements.map(item => item.grammarPoint);

  let fetchedListCopy = [...fetchedList];

  for (let i = 0; i < words.length; i++) {
    for (let j = 0; j < fetchedListCopy.length; j++) {
      if (fetchedListCopy[j].kanjiElements.indexOf(words[i]) > -1
          || fetchedListCopy[j].readingElements.indexOf(words[i]) > -1) {

        let item = {...fetchedListCopy[j]};
        item.grammarPoint = conjugations[i];
        sortedList.push(item);
        fetchedListCopy.splice(j, 1);
        j--;
      }
    }
  }

  return sortedList
};

const getResultFromKanjiEntry = (entry) => {
  let kanji = {};
  kanji.frequency = entry.frequency;
  kanji.grade = entry.grade;
  kanji.strokes = entry.strokeCount;
  kanji.eigo = entry.meaning ? entry.meaning.replace(/\|/g, ", ") : null;
  kanji.character = entry.character;
  kanji.kunReading = entry.kunReading ? entry.kunReading.replace(/\|/g, ", ")
      : null;
  kanji.onReading = entry.onReading ? entry.onReading.replace(/\|/g, ", ")
      : null;
  kanji.oldJLPT = entry.references ? entry.references.jlptOldLevel : null;
  kanji.newJLPT = entry.references ? entry.references.jlptNewLevel : null;
  kanji.keyword = entry.keyword;
  kanji.components = entry.components;
  kanji.story1 = entry.story1;
  kanji.story2 = entry.story2;
  return kanji;
};

const getResultFromWordEntry = (searchTerms, results, searchType) => {
  let result = [];

  let resultList = searchType === SearchType.WORDS_BY_KANJI ?
      results: sortResultsByRelevanceAndAddConjugation(searchTerms,
      results);

  if (!resultList) {
    return;
  }

  for (let i = 0; i < resultList.length; i++) {
    let word = {};
    word.kanji = resultList[i].kanjiElements;
    word.kana = resultList[i].readingElements;
    word.meanings = resultList[i].meanings.map(
        meaning => meaning.glosses.replace(/\|/g, ", "));
    word.grammar = resultList[i].meanings.filter(meaning => meaning.partOfSpeech .length > 0).map(
        meaning => meaning.partOfSpeech.split("|")).flat();
    result.push(word);
  }
  return result;
};

const sortResultsByRelevance = (searchTerms, results) => {
  let resultList = [];

  searchTerms.forEach(searchTerm => {
    results.forEach(result => {
      if (result.kanji === searchTerm || result.reading === searchTerm) {
        resultList.push(result);
      }
    });
  });

  return resultList;
};

const getResultFromNameEntry = (searchTerms, results) => {
  return sortResultsByRelevance(searchTerms, results);
};

const getResultFromEntry = (searchResult, fetchedResult) => {
  if (!fetchedResult.result) {
    return;
  }

  if (fetchedResult.type === SearchType.WORD) {
    return {
      type: SearchType.WORD,
      result: getResultFromWordEntry(searchResult.data, fetchedResult.result)
    };
  }

  if (fetchedResult.type === SearchType.WORDS_BY_KANJI) {
    let words = fetchedResult.result.map(entry => {
      return {word: entry.kanjiElements}
    });
    return {
      type: SearchType.WORD,
      result: getResultFromWordEntry(words, fetchedResult.result.slice(), SearchType.WORDS_BY_KANJI)
    };
  }

  if (fetchedResult.type === SearchType.KANJI) {
    return {
      type: SearchType.KANJI,
      result: getResultFromKanjiEntry(fetchedResult.result)
    };
  }

  if (fetchedResult.type === SearchType.NAME) {
    return {
      type: SearchType.NAME,
      result: getResultFromNameEntry(searchResult.data,
          fetchedResult.result.slice())
    };
  }
};

export const getResult = (searchResult, showResult) => {
  if (!searchResult || !searchResult.result) {
    return;
  }

  if (searchResult.type === SearchType.SENTENCE) {
    let sentenceTokens = showResult && showResult.result;
    return {
      type: SearchType.SENTENCE,
      text: searchResult.result,
      tokens: sentenceTokens,
      translation: showResult.translation && showResult.translation.data
          && showResult.translation.data.responseData.translatedText,
      furigana: showResult.furigana
    }
  }

  if (showResult.type === SearchType.EXAMPLE) {
    return showResult;
  }

  return getResultFromEntry(searchResult.result, showResult);
};

export class Rikai extends React.Component {

  shouldComponentUpdate(nextProps) {
    return (this.props.showResult !== nextProps.showResult)
        || (this.props.wordExamples !== nextProps.wordExamples)
        || (this.props.popupInfo.visibility !== nextProps.popupInfo.visibility)
        || (this.props.popupInfo.disableOutsideClickHandler !== nextProps.popupInfo.disableOutsideClickHandler)
        || (this.props.popupInfo.type !== nextProps.popupInfo.type);
  }

  showMore = () => {
    let number = this.props.previousSearchResult.result
    !== this.props.searchResult.result ? 0 : this.props.number + 1;

    this.props.fetchData(
        this.props.searchResult.result.data.map(item => item.word),
        this.props.searchResult.type, number, this.props.limit);
  };

  hidePopup = (event) => {
    this.props.setPopupInfo({...this.props.popupInfo, visibility: false, closed: true, type: PopupType.WORD});
  };

  changePopupType(popupType) {
    this.props.setPopupInfo({...this.props.popupInfo, type: popupType});
  }

  fetchKanji(kanji) {
    this.props.updateSearchResult({
          matchLen: 1,
          result: [...kanji],
          type: SearchType.KANJI
        });
    this.props.fetchData(kanji, SearchType.KANJI);
  }

  showPreviousWord() {
    this.props.updateSearchResult(this.props.previousSearchResult);
    this.props.updateShowResult(this.props.previousShowResult);
  }

  toggleOutsideClickHandler() {
    this.props.setPopupInfo({...this.props.popupInfo, disableOutsideClickHandler: !this.props.popupInfo.disableOutsideClickHandler});
  }

  render() {
      if(!this.props.popupInfo.visibility) {
      return <div/>
    }

    let style = getPopupStyle(this.props.popupInfo);
    let result = getResult(this.props.searchResult, this.props.showResult);

    if (!result) {
      return RikaiLoading(style);
    }

    let popUpComponent;

    switch (this.props.popupInfo.type) {
      case (PopupType.WORD):
        popUpComponent = <RikaiWord style={style} result={result.result[0]} wordExamples={this.props.wordExamples}
                                    changePopup={this.changePopupType.bind(this)} fetchKanji ={this.fetchKanji.bind(this)}/>;
        break;
      case (PopupType.KANJI) :
        popUpComponent = <RikaiKanji style={style} result={result.result} showWordExamples={this.props.updateSearchResult}
                                     changePopup={this.changePopupType.bind(this)} showPreviousWord={this.showPreviousWord.bind(this)}/>
        break;
      case(PopupType.RTK) :
        popUpComponent = <RtkInfo character={result.result.character} keyword={result.result.keyword}
                                  components={result.result.components}
                                  story1={result.result.story1} story2={result.result.story2}
                                  style={style} changePopup={this.changePopupType.bind(this)}/>
        break;
      case(PopupType.DRAW) :
        popUpComponent = <KanjiDrawPad character={result.result.character} open={true} style={style} changePopup={this.changePopupType.bind(this)}/>
        break;
      case(PopupType.ADD) :
        let item = result.type === SearchType.KANJI ? {
          kanji: result.result.character,
          kana: [result.result.kunReading, result.result.onReading],
          meanings: result.result.eigo,
          type: SearchType.KANJI
        } : {...result.result[0], type: result.type};
        popUpComponent = <AddToDeck style={style} item={item} changePopup={this.changePopupType.bind(this)} toggleOutsideClickHandler={this.toggleOutsideClickHandler.bind(this)}/>
        break;
      case(PopupType.SENTENCE) :
        popUpComponent = <RikaiSentence style={style} sentence={result}/>
        break;
    }

    return (
        <OutsideClickHandler onOutsideClick={this.hidePopup} disabled={this.props.popupInfo.disableOutsideClickHandler}>
          {popUpComponent}
        </OutsideClickHandler>
    )

  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Rikai);
