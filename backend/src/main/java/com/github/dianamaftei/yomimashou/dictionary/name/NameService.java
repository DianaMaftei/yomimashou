package com.github.dianamaftei.yomimashou.dictionary.name;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.dianamaftei.yomimashou.model.QNameEntry.nameEntry;

@Service
public class NameService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NameService.class);

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public NameService(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Transactional
    public List<Name> get(String[] words) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        for (String word : words) {
            booleanBuilder.or(nameEntry.reading.like(word)).or(nameEntry.kanji.like(word));
        }
        return (List<Name>) jpaQueryFactory.query().from(nameEntry).where(booleanBuilder).distinct().limit(20).fetch();
    }

    @Transactional
    public Set<String> get(Set<String> words) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        for (String word : words) {
            booleanBuilder.or(nameEntry.kanji.eq(word)).or(nameEntry.reading.eq(word));
        }

        List<Tuple> list = jpaQueryFactory.query().select(nameEntry.kanji, nameEntry.reading).from(nameEntry).where(booleanBuilder).distinct().fetch();

        Set<String> names = new HashSet<>();
        list.stream().forEach(tuple -> {
            names.add(tuple.get(nameEntry.kanji));
            names.add(tuple.get(nameEntry.reading));
        });
        return names.stream().filter(word -> word != null && words.contains(word)).collect(Collectors.toSet());
    }

}
