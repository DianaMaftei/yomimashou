package com.github.dianamaftei.yomimashou.dictionary.example;

import static com.github.dianamaftei.yomimashou.dictionary.example.QExampleSentence.exampleSentence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExampleSentenceService {

  @Value("${paginatation.limit}")
  private int paginationLimit;

  private final JPAQueryFactory jpaQueryFactory;

  @Autowired
  public ExampleSentenceService(final JPAQueryFactory jpaQueryFactory) {
    this.jpaQueryFactory = jpaQueryFactory;
  }

  @Transactional
  public List<ExampleSentence> get(final String[] searchItems) {
    final BooleanBuilder booleanBuilder = new BooleanBuilder();
    for (final String searchItem : searchItems) {
      booleanBuilder.or(exampleSentence.sentence.contains(searchItem))
          .or(exampleSentence.textBreakdown.contains(searchItem));
    }
    return (List<ExampleSentence>) jpaQueryFactory.query().from(exampleSentence)
        .where(booleanBuilder).distinct().limit(paginationLimit).fetch();
  }
}
