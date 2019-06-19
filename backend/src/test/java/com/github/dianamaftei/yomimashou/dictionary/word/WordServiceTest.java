package com.github.dianamaftei.yomimashou.dictionary.word;

import static com.github.dianamaftei.yomimashou.dictionary.word.QWord.word;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WordServiceTest {

  private WordService wordService;

  @Captor
  private ArgumentCaptor<Predicate> predicateArgumentCaptor;

  @Mock
  private JPAQuery jpaQuery;

  @Mock
  private JPAQueryFactory jpaQueryFactory;

  @BeforeEach
  void setUp() throws Exception {
    wordService = new WordService(jpaQueryFactory);
    when(jpaQueryFactory.query()).thenReturn(jpaQuery);
    when(jpaQuery.from(word)).thenReturn(jpaQuery);
    when(jpaQuery.where(any(Predicate.class))).thenReturn(jpaQuery);
    when(jpaQuery.distinct()).thenReturn(jpaQuery);
    when(jpaQuery.leftJoin(word.meanings)).thenReturn(jpaQuery);
    when(jpaQuery.fetchJoin()).thenReturn(jpaQuery);
  }

  @Test
  void get() {
    String[] searchItems = {"searchItem"};
    List<Word> words = Collections.emptyList();
    when(jpaQuery.fetch()).thenReturn(words);
    List<Word> wordList = wordService.get(searchItems);
    verify(jpaQueryFactory).query();
    verify(jpaQuery).where(predicateArgumentCaptor.capture());
    String conditions = predicateArgumentCaptor.getAllValues().toString();
    assertEquals(
        "[word.readingElements = searchItem || " + "word.readingElements like searchItem|% || "
            + "word.readingElements like %|searchItem|% || "
            + "word.readingElements like %|searchItem || " + "word.kanjiElements = searchItem || "
            + "word.kanjiElements like searchItem|% || "
            + "word.kanjiElements like %|searchItem|% || "
            + "word.kanjiElements like %|searchItem]", conditions);
    assertEquals(words, wordList);
  }
}
