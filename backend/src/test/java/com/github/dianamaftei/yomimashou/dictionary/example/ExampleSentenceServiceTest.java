package com.github.dianamaftei.yomimashou.dictionary.example;

import com.github.dianamaftei.appscommon.model.ExampleSentence;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static com.github.dianamaftei.appscommon.model.QExampleSentence.exampleSentence;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class ExampleSentenceServiceTest {

  private ExampleSentenceService exampleSentenceService;

  @Mock
  private JPAQueryFactory jpaQueryFactory;

  @Mock
  private JPAQuery jpaQuery;

  @Captor
  private ArgumentCaptor<Predicate> predicateArgumentCaptor;

  private static Pageable pageable;

  @BeforeEach
  void setUp() {
    exampleSentenceService = new ExampleSentenceService(jpaQueryFactory);
    pageable = new PageRequest(0, 10);
  }

  @Test
  void get() {
    final String[] searchItems = {"searchItem"};
    when(jpaQueryFactory.query()).thenReturn(jpaQuery);
    when(jpaQuery.from(exampleSentence)).thenReturn(jpaQuery);
    when(jpaQuery.fetch()).thenReturn(Collections.emptyList());
    when(jpaQuery.where(any(Predicate.class))).thenReturn(jpaQuery);
    when(jpaQuery.distinct()).thenReturn(jpaQuery);
    when(jpaQuery.limit(10)).thenReturn(jpaQuery);
    final List<ExampleSentence> exampleSentences = exampleSentenceService.get(searchItems, pageable);
    verify(jpaQuery).where(predicateArgumentCaptor.capture());
    final Predicate value = predicateArgumentCaptor.getValue();
    assertEquals(
        "contains(exampleSentence.sentence,searchItem) || contains(exampleSentence.textBreakdown,searchItem)",
        value.toString());
    verify(jpaQuery, times(1)).from(exampleSentence);
    verify(jpaQueryFactory, times(1)).query();
    assertTrue(exampleSentences.isEmpty());
  }
}
