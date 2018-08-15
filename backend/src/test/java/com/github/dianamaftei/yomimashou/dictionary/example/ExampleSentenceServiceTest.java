package com.github.dianamaftei.yomimashou.dictionary.example;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static com.github.dianamaftei.yomimashou.dictionary.example.QExampleSentence.exampleSentence;


@RunWith(MockitoJUnitRunner.class)
public class ExampleSentenceServiceTest {
    private ExampleSentenceService exampleSentenceService;

    @Mock
    private JPAQueryFactory jpaQueryFactory;
    @Mock
    private JPAQuery jpaQuery;

    @Captor
    private ArgumentCaptor<Predicate> predicateArgumentCaptor;

    @Before
    public void setUp() {
        exampleSentenceService = new ExampleSentenceService(jpaQueryFactory);
    }

    @Test
    public void get() {
        String[] searchItems = {"searchItem"};
        when(jpaQueryFactory.query()).thenReturn(jpaQuery);
        when(jpaQuery.from(exampleSentence)).thenReturn(jpaQuery);
        when(jpaQuery.fetch()).thenReturn(Collections.emptyList());
        when(jpaQuery.where(any(Predicate.class))).thenReturn(jpaQuery);
        when(jpaQuery.distinct()).thenReturn(jpaQuery);
        when(jpaQuery.limit(10)).thenReturn(jpaQuery);

        List<ExampleSentence> exampleSentences = exampleSentenceService.get(searchItems);

        verify(jpaQuery).where(predicateArgumentCaptor.capture());
        Predicate value = predicateArgumentCaptor.getValue();
        assertEquals("contains(exampleSentence.sentence,searchItem) || contains(exampleSentence.textBreakdown,searchItem)", value.toString());
        verify(jpaQuery, times(1)).from(exampleSentence);
        verify(jpaQueryFactory, times(1)).query();
        assertTrue(exampleSentences.isEmpty());
    }
}