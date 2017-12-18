package com.github.dianamaftei.yomimashou.service;

import com.github.dianamaftei.yomimashou.model.WordEntry;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.github.dianamaftei.yomimashou.model.QWordEntry.wordEntry;

@Service
public class WordEntryService {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public WordEntryService(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Transactional
    public List<WordEntry> get(String[] words) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        for (String word : words) {
            booleanBuilder.or(wordEntry.readingElements.like(word)).or(wordEntry.kanjiElements.like(word));
            booleanBuilder.or(wordEntry.readingElements.like(word + "/%")).or(wordEntry.kanjiElements.like(word + "/%"));
            booleanBuilder.or(wordEntry.readingElements.like("%/" + word + "/%")).or(wordEntry.kanjiElements.like("%/" + word + "/%"));
            booleanBuilder.or(wordEntry.readingElements.like("%/" + word)).or(wordEntry.kanjiElements.like("%/" + word));
        }
        return (List<WordEntry>) jpaQueryFactory.query().from(wordEntry).where(booleanBuilder).distinct().leftJoin(wordEntry.meanings).fetchJoin().fetch();
    }
}
