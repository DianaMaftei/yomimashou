package com.github.dianamaftei.yomimashou.dictionary.word;

import static com.github.dianamaftei.yomimashou.dictionary.word.QWord.word;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WordService {

  private final JPAQueryFactory jpaQueryFactory;

  @Autowired
  public WordService(JPAQueryFactory jpaQueryFactory) {
    this.jpaQueryFactory = jpaQueryFactory;
  }

  public List<Word> getByEqualsKanji(String[] words) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    for (String wordString : words) {
      booleanBuilder.or(byReadingElements(wordString)).or(byKanjiElements(wordString));
    }
    return (List<Word>) jpaQueryFactory.query().from(word).where(booleanBuilder).distinct()
        .leftJoin(word.meanings).fetchJoin().fetch();
  }

  public List<Word> getByStartingKanji(String searchItem) {
    return (List<Word>) jpaQueryFactory.query().from(word).where(
        word.kanjiElements.like("%|" + searchItem).or(word.kanjiElements.like(searchItem + "%"))
    ).orderBy(word.priority.asc()).distinct().limit(10).fetch();
  }

  public List<Word> getByEndingKanji(String searchItem) {
    return (List<Word>) jpaQueryFactory.query().from(word).where(
        word.kanjiElements.like(searchItem + "|%").or(word.kanjiElements.like("%" + searchItem)))
        .orderBy(word.priority.asc()).distinct().limit(10).fetch();
  }

  public List<Word> getByContainingKanji(String searchItem) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.or(word.kanjiElements.like("%" + searchItem + "%"));
    booleanBuilder.andNot(word.kanjiElements.like("%|" + searchItem + "%"));
    booleanBuilder.andNot(word.kanjiElements.like("%" + searchItem));
    booleanBuilder.andNot(word.kanjiElements.like(searchItem + "%"));
    booleanBuilder.andNot(word.kanjiElements.like("%" + searchItem + "|%"));

    return (List<Word>) jpaQueryFactory.query().from(word)
        .where(booleanBuilder).distinct().orderBy(word.priority.asc()).limit(10).fetch();
  }

  private BooleanBuilder byReadingElements(String wordString) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();

    booleanBuilder.or(word.readingElements.eq(wordString))
        .or(word.readingElements.like(wordString + "|%"))
        .or(word.readingElements.like("%|" + wordString + "|%"))
        .or(word.readingElements.like("%|" + wordString));

    return booleanBuilder;
  }

  private BooleanBuilder byKanjiElements(String wordString) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();

    booleanBuilder.or(word.kanjiElements.eq(wordString))
        .or(word.kanjiElements.like(wordString + "|%"))
        .or(word.kanjiElements.like("%|" + wordString + "|%"))
        .or(word.kanjiElements.like("%|" + wordString));

    return booleanBuilder;
  }
}
