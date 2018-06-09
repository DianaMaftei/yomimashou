package com.github.dianamaftei.yomimashou.service;

import com.github.dianamaftei.yomimashou.model.ExampleSentence;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.github.dianamaftei.yomimashou.model.QExampleSentence.exampleSentence;

@Service
public class ExampleSentenceService {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public ExampleSentenceService(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Transactional
    public List<ExampleSentence> get(String searchItem) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.or(exampleSentence.sentence.like("%" + searchItem + "%").or(exampleSentence.textBreakdown.like("%" + searchItem + "%")));

        return (List<ExampleSentence>) jpaQueryFactory.query().from(exampleSentence).where(booleanBuilder).distinct().limit(10).fetch();
    }
}
