package com.github.dianamaftei.yomimashou.dictionary.name;

import static com.github.dianamaftei.yomimashou.dictionary.name.QName.name;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
class NameServiceTest {

  @Mock
  private JPAQuery jpaQuery;

  @Mock
  private JPAQueryFactory jpaQueryFactory;

  private NameService nameService;

  @Captor
  private ArgumentCaptor<Predicate> predicateArgumentCaptor;

  @BeforeEach
  void setUp() {
    nameService = new NameService(jpaQueryFactory);
    when(jpaQueryFactory.query()).thenReturn(jpaQuery);
    when(jpaQuery.from(name)).thenReturn(jpaQuery);
    when(jpaQuery.where(any(Predicate.class))).thenReturn(jpaQuery);
    when(jpaQuery.distinct()).thenReturn(jpaQuery);
    when(jpaQuery.limit(anyLong())).thenReturn(jpaQuery);
  }

  @Test
  void getShouldReturnAListOfNames() {
    Name name = new Name();
    name.setKanji("のりみや");
    name.setId(5L);
    when(jpaQuery.fetch()).thenReturn(Collections.singletonList(name));

    List<Name> names = nameService.get(new String[]{"のりみや"});

    verify(jpaQuery).where(predicateArgumentCaptor.capture());
    assertEquals("[name.reading like のりみや || name.kanji like のりみや]",
        predicateArgumentCaptor.getAllValues().toString());
    assertAll("Should return a list with only the name element declared above",
        () -> assertEquals(1, names.size()),
        () -> assertEquals(name.getId(), names.get(0).getId()));
  }
}
