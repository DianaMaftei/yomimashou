package com.github.dianamaftei.yomimashou.dictionary.word;

import static com.github.dianamaftei.yomimashou.dictionary.word.QWord.word;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WordServiceTest {

  private WordService wordService;

  @Captor
  private ArgumentCaptor<Predicate> predicateArgumentCaptor;
  @Mock
  private JPAQuery jpaQuery;
  @Mock
  private JPAQueryFactory jpaQueryFactory;

  @Before
  public void setUp() throws Exception {
    wordService = new WordService(jpaQueryFactory);
    when(jpaQueryFactory.query()).thenReturn(jpaQuery);
    when(jpaQuery.from(word)).thenReturn(jpaQuery);
    when(jpaQuery.where(any(Predicate.class))).thenReturn(jpaQuery);
    when(jpaQuery.distinct()).thenReturn(jpaQuery);
    when(jpaQuery.leftJoin(word.meanings)).thenReturn(jpaQuery);
    when(jpaQuery.fetchJoin()).thenReturn(jpaQuery);
  }

  @Test
  public void get() {
    String[] searchItems = {"searchItem"};
    List<Word> words = Collections.emptyList();

    when(jpaQuery.fetch()).thenReturn(words);

    List<Word> wordList = wordService.get(searchItems);
    verify(jpaQueryFactory).query();
    verify(jpaQuery).where(predicateArgumentCaptor.capture());
    String conditions = predicateArgumentCaptor.getAllValues().toString();

    assertEquals("[word.readingElements = searchItem || "
        + "word.readingElements like searchItem|% || "
        + "word.readingElements like %|searchItem|% || "
        + "word.readingElements like %|searchItem || "
        + "word.kanjiElements = searchItem || "
        + "word.kanjiElements like searchItem|% || "
        + "word.kanjiElements like %|searchItem|% || "
        + "word.kanjiElements like %|searchItem]", conditions);
    assertEquals(words, wordList);
  }
}