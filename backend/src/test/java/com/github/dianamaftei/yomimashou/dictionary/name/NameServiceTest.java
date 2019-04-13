package com.github.dianamaftei.yomimashou.dictionary.name;

import static com.github.dianamaftei.yomimashou.dictionary.name.QName.name;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NameServiceTest {

  @Mock
  private JPAQuery jpaQuery;

  @Mock
  private JPAQueryFactory jpaQueryFactory;

  private NameService nameService;

  @Before
  public void setUp() {
    nameService = new NameService(jpaQueryFactory);
  }

  @Test
  public void getShouldReturnAListOfNames() {
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