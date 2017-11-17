package com.github.dianamaftei.yomimashou.service;

import com.github.dianamaftei.yomimashou.model.WordEntry;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.github.dianamaftei.yomimashou.model.QWordEntry.wordEntry;

@Service
public class WordEntryService {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public WordEntryService(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Transactional
    public WordEntry get(long id) {
        return (WordEntry) jpaQueryFactory.query().from(wordEntry).where(wordEntry.id.eq(id)).leftJoin(wordEntry.meanings).fetchJoin().fetchOne();
    }
}
