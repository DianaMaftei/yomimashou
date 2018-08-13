package com.github.dianamaftei.yomimashou.dictionary.kanji;

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

import static com.github.dianamaftei.yomimashou.model.QKanjiEntry.kanjiEntry;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class KanjiServiceTest {

    private KanjiService kanjiService;

    @Mock
    private JPAQuery jpaQuery;
    @Mock
    private JPAQueryFactory jpaQueryFactory;

    @Captor
    private ArgumentCaptor<Predicate> predicateArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        kanjiService = new KanjiService(jpaQueryFactory);
    }

    @Test
    public void get() {
        String searchItem = "searchItem";
        Kanji kanji = new Kanji();
        kanji.setKanji("kanji");
        when(jpaQueryFactory.query()).thenReturn(jpaQuery);
        when(jpaQuery.from(kanjiEntry)).thenReturn(jpaQuery);
        when(jpaQuery.fetchOne()).thenReturn(kanji);
        when(jpaQuery.where(any(Predicate.class))).thenReturn(jpaQuery);

        Kanji result = kanjiService.get(searchItem);

        verify(jpaQuery).where(predicateArgumentCaptor.capture());
        Predicate value = predicateArgumentCaptor.getValue();
        assertEquals("kanji.kanji = searchItem", value.toString());
        verify(jpaQuery, times(1)).from(kanjiEntry);
        verify(jpaQueryFactory, times(1)).query();
        assertEquals("kanji", result.getKanji());
    }

    @Test
    public void getKanjiSVG() {
        //TODO test this method
    }
}