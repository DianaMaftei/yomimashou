package com.github.dianamaftei.yomimashou.dictionary.word;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static com.github.dianamaftei.yomimashou.model.QWordEntry.wordEntry;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    }

    @Test
    public void get() {
        String[] searchItems = {"searchItem"};
        List<Word> words = Collections.emptyList();

        when(jpaQueryFactory.query()).thenReturn(jpaQuery);
        when(jpaQuery.from(wordEntry)).thenReturn(jpaQuery);
        when(jpaQuery.where(any(Predicate.class))).thenReturn(jpaQuery);
        when(jpaQuery.distinct()).thenReturn(jpaQuery);
        when(jpaQuery.leftJoin(wordEntry.meanings)).thenReturn(jpaQuery);
        when(jpaQuery.fetchJoin()).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(words);

        List<Word> wordList = wordService.get(searchItems);
        verify(jpaQueryFactory).query();
        verify(jpaQuery).where(predicateArgumentCaptor.capture());
        String conditions = predicateArgumentCaptor.getAllValues().toString();
        assertEquals("[word.readingElements like searchItem || word.kanjiElements like searchItem || word.readingElements like searchItem|% || word.kanjiElements like searchItem|% || word.readingElements like %|searchItem|% || word.kanjiElements like %|searchItem|% || word.readingElements like %|searchItem || word.kanjiElements like %|searchItem]", conditions);
        assertEquals(words, wordList);
    }
}