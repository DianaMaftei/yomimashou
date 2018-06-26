package com.github.dianamaftei.yomimashou.service;

import com.github.dianamaftei.yomimashou.model.NameEntry;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.List;

import static com.github.dianamaftei.yomimashou.model.QNameEntry.nameEntry;


@Service
public class NameEntryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NameEntryService.class);

    private final JPAQueryFactory jpaQueryFactory;
    @Value("${kanji.path}")
    private String kanjiPath;

    @Autowired
    public NameEntryService(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Transactional
    public List<NameEntry> get(String[] words) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        for (String word : words) {
            booleanBuilder.or(nameEntry.reading.like(word)).or(nameEntry.kanji.like(word));
            booleanBuilder.or(nameEntry.reading.like(word + "|%")).or(nameEntry.kanji.like(word + "|%"));
            booleanBuilder.or(nameEntry.reading.like("%|" + word + "|%")).or(nameEntry.kanji.like("%|" + word + "|%"));
            booleanBuilder.or(nameEntry.reading.like("%|" + word)).or(nameEntry.kanji.like("%|" + word));
        }
        return (List<NameEntry>) jpaQueryFactory.query().from(nameEntry).where(booleanBuilder).distinct().fetch();
    }
}
