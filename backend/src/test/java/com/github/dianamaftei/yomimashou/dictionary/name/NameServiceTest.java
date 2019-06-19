package com.github.dianamaftei.yomimashou.dictionary.name;

import static com.github.dianamaftei.yomimashou.dictionary.name.QName.name;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NameServiceTest {

  @Mock
  private JPAQuery jpaQuery;

  @Mock
  private JPAQueryFactory jpaQueryFactory;

  private NameService nameService;

  @BeforeEach
  void setUp() {
    nameService = new NameService(jpaQueryFactory);
  }

  @Test
  void getShouldReturnAListOfNames() {
    when(jpaQueryFactory.query()).thenReturn(jpaQuery);
    when(jpaQuery.from(name)).thenReturn(jpaQuery);
    when(jpaQuery.where(any(Predicate.class))).thenReturn(jpaQuery);
    when(jpaQuery.distinct()).thenReturn(jpaQuery);
    when(jpaQuery.limit(20)).thenReturn(jpaQuery);
    when(jpaQuery.fetch()).thenReturn(Collections.singletonList(new Name()));
    List<Name> names = nameService.get(new String[]{"のりみや"});
    assertEquals(names.size(), 1);
  }
}
