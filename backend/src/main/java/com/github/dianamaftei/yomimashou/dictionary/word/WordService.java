package com.github.dianamaftei.yomimashou.dictionary.word;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WordService {

  private final WordRepository wordRepository;

  @Autowired
  public WordService(WordRepository wordRepository) {
    this.wordRepository = wordRepository;
  }

  public Page<Word> getByReadingElemOrKanjiElem(String[] searchItems, Pageable pageable) {
    return wordRepository
        .findDistinctByKanjiElementsInOrReadingElementsIn(searchItems, searchItems, pageable);
  }

  public Page<Word> getByStartingKanji(String searchItem, Pageable pageable) {
    return wordRepository.findDistinctByKanjiElementsLikeOrderByPriority(
        searchItem + "%", pageable);
  }


  public Page<Word> getByEndingKanji(String searchItem, Pageable pageable) {
    return wordRepository.findDistinctByKanjiElementsLikeOrderByPriority(
        "%" + searchItem, pageable);
  }

  public Page<Word> getByContainingKanji(String searchItem, Pageable pageable) {
    return wordRepository.findDistinctByKanjiElementsLikeOrderByPriority(
        "%" + searchItem + "%", pageable);
  }
}
