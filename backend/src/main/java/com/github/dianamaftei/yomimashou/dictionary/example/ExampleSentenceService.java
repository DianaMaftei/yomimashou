package com.github.dianamaftei.yomimashou.dictionary.example;

import com.github.dianamaftei.appscommon.model.ExampleSentence;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.github.dianamaftei.appscommon.model.QExampleSentence.exampleSentence;

@Service
public class ExampleSentenceService {

  private final JPAQueryFactory jpaQueryFactory;

  @Autowired
  public ExampleSentenceService(final JPAQueryFactory jpaQueryFactory) {
    this.jpaQueryFactory = jpaQueryFactory;
  }

  @Transactional
  public List<ExampleSentence> get(final String[] searchItems, Pageable pageable) {
    final BooleanBuilder booleanBuilder = new BooleanBuilder();
    for (final String searchItem : searchItems) {
      booleanBuilder.or(exampleSentence.sentence.contains(searchItem))
          .or(exampleSentence.textBreakdown.contains(searchItem));
    }
    return (List<ExampleSentence>) jpaQueryFactory.query().from(exampleSentence)
        .where(booleanBuilder).distinct().limit(pageable.getPageSize()).fetch();
  }
}
