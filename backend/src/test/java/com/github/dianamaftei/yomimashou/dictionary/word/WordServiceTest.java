package com.github.dianamaftei.yomimashou.dictionary.word;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class WordServiceTest {

  @InjectMocks
  private WordService wordService;

  @Mock
  private WordRepository wordRepository;

  @Test
  void getByEqualsKanjiSearchesByFixedMatchForKanjiOrReadingElements() {
    String[] searchItems = {"searchItem"};
    List<Word> words = Collections.emptyList();

    when(wordRepository.findDistinctByKanjiElementsInOrReadingElementsIn(searchItems, searchItems,
        Pageable.unpaged()))
        .thenReturn(new PageImpl<>(words));
    Page<Word> wordList = wordService.getByReadingElemOrKanjiElem(searchItems, Pageable.unpaged());

    verify(wordRepository, times(1))
        .findDistinctByKanjiElementsInOrReadingElementsIn(searchItems, searchItems,
            Pageable.unpaged());
  }

  @Test
  void getByStartingKanjiSearchesByKanjiOrReadingElementsStartingWithSearchItem() {
    List<Word> words = Collections.emptyList();

    when(wordRepository.findDistinctByKanjiElementsLikeOrderByPriority("猫%", Pageable.unpaged()))
        .thenReturn(new PageImpl<>(words));
    Page<Word> wordList = wordService.getByStartingKanji("猫", Pageable.unpaged());

    verify(wordRepository, times(1))
        .findDistinctByKanjiElementsLikeOrderByPriority("猫%", Pageable.unpaged());
  }

  @Test
  void getByEndingKanjiSearchesByKanjiOrReadingElementsEndingWithSearchItem() {
    List<Word> words = Collections.emptyList();

    when(wordRepository.findDistinctByKanjiElementsLikeOrderByPriority("%猫", Pageable.unpaged()))
        .thenReturn(new PageImpl<>(words));
    Page<Word> wordList = wordService.getByEndingKanji("猫", Pageable.unpaged());

    verify(wordRepository, times(1))
        .findDistinctByKanjiElementsLikeOrderByPriority("%猫", Pageable.unpaged());
  }

  @Test
  void getByContainingKanjiSearchesByKanjiOrReadingElementsContainingSearchItem() {
    List<Word> words = Collections.emptyList();

    when(wordRepository.findDistinctByKanjiElementsLikeOrderByPriority("%猫%", Pageable.unpaged()))
        .thenReturn(new PageImpl<>(words));
    Page<Word> wordList = wordService.getByContainingKanji("猫", Pageable.unpaged());

    verify(wordRepository, times(1))
        .findDistinctByKanjiElementsLikeOrderByPriority("%猫%", Pageable.unpaged());
  }
}
