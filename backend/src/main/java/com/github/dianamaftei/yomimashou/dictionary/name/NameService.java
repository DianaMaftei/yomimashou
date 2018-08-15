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

import static com.github.dianamaftei.yomimashou.dictionary.name.QName.name;

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
            booleanBuilder.or(name.reading.like(word)).or(name.kanji.like(word));
        }
        return (List<Name>) jpaQueryFactory.query().from(name).where(booleanBuilder).distinct().limit(20).fetch();
    }

    @Transactional
    public Set<String> get(Set<String> words) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        for (String word : words) {
            booleanBuilder.or(name.kanji.eq(word)).or(name.reading.eq(word));
        }

        List<Tuple> list = jpaQueryFactory.query().select(name.kanji, name.reading).from(name).where(booleanBuilder).distinct().fetch();

        Set<String> names = new HashSet<>();
        list.stream().forEach(tuple -> {
            names.add(tuple.get(name.kanji));
            names.add(tuple.get(name.reading));
        });
        return names.stream().filter(word -> word != null && words.contains(word)).collect(Collectors.toSet());
    }

}
