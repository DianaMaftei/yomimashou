package com.github.dianamaftei.yomimashou.dictionary.example;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.github.dianamaftei.yomimashou.dictionary.example.QExampleSentence.exampleSentence;

@Service
public class ExampleSentenceService {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public ExampleSentenceService(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Transactional
    public List<ExampleSentence> get(String[] searchItems) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        for (String searchItem : searchItems) {
            booleanBuilder.or(exampleSentence.sentence.contains(searchItem)).or(exampleSentence.textBreakdown.contains(searchItem));
        }
        return (List<ExampleSentence>) jpaQueryFactory.query().from(exampleSentence).where(booleanBuilder).distinct().limit(10).fetch();
    }
}
