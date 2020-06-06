package com.github.dianamaftei.yomimashou.dictionary.word;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.dianamaftei.appscommon.model.Word;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(MockitoExtension.class)
class WordServiceTest {

  @InjectMocks
  private WordService wordService;

  @Mock
  private WordRepository wordRepository;

  @Test
  void getByEqualsKanjiSearchesByFixedMatchForKanjiOrReadingElements() {
    final String[] searchItems = {"searchItem"};
    final List<Word> words = Collections.emptyList();

    when(wordRepository.findDistinctByKanjiElementsInOrReadingElementsIn(searchItems, searchItems,
        Pageable.unpaged()))
        .thenReturn(new PageImpl<>(words));

    final Page<Word> wordList = wordService.getByReadingElemOrKanjiElem(searchItems, Pageable.unpaged());

    verify(wordRepository, times(1))
        .findDistinctByKanjiElementsInOrReadingElementsIn(searchItems, searchItems,
            Pageable.unpaged());
  }

  @Test
  void getByStartingKanjiSearchesByKanjiOrReadingElementsStartingWithSearchItem() {
    final List<Word> words = Collections.emptyList();

    when(wordRepository.findDistinctByKanjiElementsLikeOrderByPriority("猫%", Pageable.unpaged()))
        .thenReturn(new PageImpl<>(words));
    final Page<Word> wordList = wordService.getByStartingKanji("猫", Pageable.unpaged());

    verify(wordRepository, times(1))
        .findDistinctByKanjiElementsLikeOrderByPriority("猫%", Pageable.unpaged());
  }

  @Test
  void getByEndingKanjiSearchesByKanjiOrReadingElementsEndingWithSearchItem() {
    final List<Word> words = Collections.emptyList();

    when(wordRepository.findDistinctByKanjiElementsLikeOrderByPriority("%猫", Pageable.unpaged()))
        .thenReturn(new PageImpl<>(words));
    final Page<Word> wordList = wordService.getByEndingKanji("猫", Pageable.unpaged());

    verify(wordRepository, times(1))
        .findDistinctByKanjiElementsLikeOrderByPriority("%猫", Pageable.unpaged());
  }

  @Test
  void getByContainingKanjiSearchesByKanjiOrReadingElementsContainingSearchItem() {
    final List<Word> words = Collections.emptyList();

    when(wordRepository.findDistinctByKanjiElementsLikeOrderByPriority("%猫%", Pageable.unpaged()))
        .thenReturn(new PageImpl<>(words));
    final Page<Word> wordList = wordService.getByContainingKanji("猫", Pageable.unpaged());

    verify(wordRepository, times(1))
        .findDistinctByKanjiElementsLikeOrderByPriority("%猫%", Pageable.unpaged());
  }
}
