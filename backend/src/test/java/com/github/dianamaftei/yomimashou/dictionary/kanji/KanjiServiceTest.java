package com.github.dianamaftei.yomimashou.dictionary.kanji;

import static com.github.dianamaftei.yomimashou.dictionary.kanji.QKanji.kanji;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KanjiServiceTest {

  private KanjiService kanjiService;

  @Mock
  private JPAQuery jpaQuery;

  @Mock
  private JPAQueryFactory jpaQueryFactory;

  @Captor
  private ArgumentCaptor<Predicate> predicateArgumentCaptor;

  @BeforeEach
  void setUp() throws Exception {
    kanjiService = new KanjiService(jpaQueryFactory);
  }

  @Test
  void get() {
    String searchItem = "searchItem";
    Kanji kanjiEntry = new Kanji();
    kanjiEntry.setCharacter("kanji");
    when(jpaQueryFactory.query()).thenReturn(jpaQuery);
    when(jpaQuery.from(kanji)).thenReturn(jpaQuery);
    when(jpaQuery.fetchOne()).thenReturn(kanjiEntry);
    when(jpaQuery.where(any(Predicate.class))).thenReturn(jpaQuery);
    Kanji result = kanjiService.get(searchItem);
    verify(jpaQuery).where(predicateArgumentCaptor.capture());
    Predicate value = predicateArgumentCaptor.getValue();
    assertEquals("kanji.character = searchItem", value.toString());
    verify(jpaQuery, times(1)).from(kanji);
    verify(jpaQueryFactory, times(1)).query();
    assertEquals("kanji", result.getCharacter());
  }

  @Test
  void getKanjiSVG() {
    // TODO test this method
  }
}
