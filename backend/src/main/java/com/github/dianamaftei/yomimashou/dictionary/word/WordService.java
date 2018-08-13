package com.github.dianamaftei.yomimashou.dictionary.word;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.github.dianamaftei.yomimashou.model.QWordEntry.wordEntry;

@Service
public class WordService {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public WordService(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Transactional
    public List<Word> get(String[] words) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        for (String word : words) {
            booleanBuilder.or(wordEntry.readingElements.like(word)).or(wordEntry.kanjiElements.like(word));
            booleanBuilder.or(wordEntry.readingElements.like(word + "|%")).or(wordEntry.kanjiElements.like(word + "|%"));
            booleanBuilder.or(wordEntry.readingElements.like("%|" + word + "|%")).or(wordEntry.kanjiElements.like("%|" + word + "|%"));
            booleanBuilder.or(wordEntry.readingElements.like("%|" + word)).or(wordEntry.kanjiElements.like("%|" + word));
        }
        return (List<Word>) jpaQueryFactory.query().from(wordEntry).where(booleanBuilder).distinct().leftJoin(wordEntry.meanings).fetchJoin().fetch();
    }

    public List<Word> getByStartingKanji(String searchItem) {
        return (List<Word>) jpaQueryFactory.query().from(wordEntry)
                .where(wordEntry.kanjiElements.like("%|" + searchItem)
                        .or(wordEntry.kanjiElements.like(searchItem + "%"))
                ).orderBy(wordEntry.priority.asc())
                .distinct().limit(10).fetch();
    }

    public List<Word> getByEndingKanji(String searchItem) {
        return (List<Word>) jpaQueryFactory.query().from(wordEntry)
                .where(wordEntry.kanjiElements.like(searchItem + "|%")
                        .or(wordEntry.kanjiElements.like("%" + searchItem))
                ).orderBy(wordEntry.priority.asc())
                .distinct().limit(10).fetch();
    }

    public List<Word> getByContainingKanji(String searchItem) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.or(wordEntry.kanjiElements.like("%" + searchItem + "%"));
        booleanBuilder.andNot(wordEntry.kanjiElements.like("%|" + searchItem + "%"));
        booleanBuilder.andNot(wordEntry.kanjiElements.like("%" + searchItem));
        booleanBuilder.andNot(wordEntry.kanjiElements.like(searchItem + "%"));
        booleanBuilder.andNot(wordEntry.kanjiElements.like("%" + searchItem + "|%"));

        return (List<Word>) jpaQueryFactory.query().from(wordEntry)
                .where(booleanBuilder).distinct().orderBy(wordEntry.priority.asc()).limit(10).fetch();
    }
}