package com.github.dianamaftei.yomimashou.dictionary.name;

import static com.github.dianamaftei.yomimashou.dictionary.name.QName.name;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NameService {

  private final JPAQueryFactory jpaQueryFactory;

  @Autowired
  public NameService(JPAQueryFactory jpaQueryFactory) {
    this.jpaQueryFactory = jpaQueryFactory;
  }

  public List<Name> get(String[] words) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    for (String word : words) {
      booleanBuilder.or(name.reading.like(word)).or(name.kanji.like(word));
    }
    return (List<Name>) jpaQueryFactory.query().from(name).where(booleanBuilder).distinct()
        .limit(20).fetch();
  }
}
