package com.github.dianamaftei.yomimashou.service;

import com.github.dianamaftei.yomimashou.model.KanjiEntry;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.github.dianamaftei.yomimashou.model.QKanjiEntry.kanjiEntry;


@Service
public class KanjiEntryService {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public KanjiEntryService(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Transactional
    public KanjiEntry get(String searchItem) {
        return (KanjiEntry) jpaQueryFactory.query().from(kanjiEntry).where(kanjiEntry.kanji.eq(searchItem)).fetchOne();
    }
}
