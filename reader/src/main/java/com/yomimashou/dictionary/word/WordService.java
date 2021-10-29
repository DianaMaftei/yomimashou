package com.yomimashou.dictionary.word;

import com.yomimashou.appscommon.model.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WordService {

  private final WordRepository wordRepository;

  @Autowired
  public WordService(final WordRepository wordRepository) {
    this.wordRepository = wordRepository;
  }

  public Page<Word> getByReadingElemOrKanjiElem(final String[] searchItems, final Pageable pageable) {
    return wordRepository
        .findDistinctByKanjiElementsInOrReadingElementsIn(searchItems, searchItems, pageable);
  }

  public Page<Word> getByStartingKanji(final String searchItem, final Pageable pageable) {
    return wordRepository.findDistinctByKanjiElementsLikeOrderByPriority(
        searchItem + "%", pageable);
  }


  public Page<Word> getByEndingKanji(final String searchItem, final Pageable pageable) {
    return wordRepository.findDistinctByKanjiElementsLikeOrderByPriority(
        "%" + searchItem, pageable);
  }

  public Page<Word> getByContainingKanji(final String searchItem, final Pageable pageable) {
    return wordRepository.findDistinctByKanjiElementsLikeOrderByPriority(
        "%" + searchItem + "%", pageable);
  }
}
