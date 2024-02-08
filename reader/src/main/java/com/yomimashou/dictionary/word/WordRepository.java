package com.yomimashou.dictionary.word;

import com.yomimashou.appscommon.model.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "kanjiElements",
                    "readingElements",
                    "meanings"
            }
    )
    List<Word> findDistinctByReadingElementsInOrKanjiElementsIn(Set<String> kanjiWords, Set<String> readingWords);

    Page<Word> findDistinctByKanjiElementsStartsWithOrderByPriority(String kanji, Pageable pageable);

    Page<Word> findDistinctByKanjiElementsEndsWithOrderByPriority(String kanji, Pageable pageable);

    Page<Word> findDistinctByKanjiElementsContainsOrderByPriority(String kanji, Pageable pageable);

}
