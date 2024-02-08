package com.yomimashou.dictionary.word;

import com.yomimashou.appscommon.model.Word;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WordServiceTest {

    @InjectMocks
    private WordService wordService;

    @Mock
    private WordRepository wordRepository;

    @Test
    void getByStartingKanjiSearchesByKanjiOrReadingElementsStartingWithSearchItem() {
        final List<Word> words = Collections.singletonList(Word.builder().kanjiElements(Collections.singleton("猫舌")).build());
        when(wordRepository.findDistinctByKanjiElementsStartsWithOrderByPriority("猫", Pageable.unpaged()))
                .thenReturn(new PageImpl<>(words));

        Page<Word> wordsPage = wordService.getByStartingKanji("猫", Pageable.unpaged());

        verify(wordRepository).findDistinctByKanjiElementsStartsWithOrderByPriority("猫", Pageable.unpaged());
        assertThat(wordsPage.getTotalElements()).isEqualTo(words.size());
    }

    @Test
    void getByEndingKanjiSearchesByKanjiOrReadingElementsEndingWithSearchItem() {
        final List<Word> words = Collections.singletonList(Word.builder().kanjiElements(Collections.singleton("金曜日")).build());
        when(wordRepository.findDistinctByKanjiElementsEndsWithOrderByPriority("日", Pageable.unpaged()))
                .thenReturn(new PageImpl<>(words));

        Page<Word> wordsPage = wordService.getByEndingKanji("日", Pageable.unpaged());

        verify(wordRepository).findDistinctByKanjiElementsEndsWithOrderByPriority("日", Pageable.unpaged());
        assertThat(wordsPage.getTotalElements()).isEqualTo(words.size());
    }

    @Test
    void getByContainingKanjiSearchesByKanjiOrReadingElementsContainingSearchItem() {
        final List<Word> words = Collections.singletonList(Word.builder().kanjiElements(Collections.singleton("国家独占")).build());
        when(wordRepository.findDistinctByKanjiElementsContainsOrderByPriority("家", Pageable.unpaged()))
                .thenReturn(new PageImpl<>(words));

        Page<Word> wordsPage = wordService.getByContainingKanji("家", Pageable.unpaged());

        verify(wordRepository).findDistinctByKanjiElementsContainsOrderByPriority("家", Pageable.unpaged());
        assertThat(wordsPage.getTotalElements()).isEqualTo(words.size());
    }
}
