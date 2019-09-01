package com.github.dianamaftei.yomimashou.dictionary.word;

import com.github.dianamaftei.appscommon.model.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

  Page<Word> findDistinctByKanjiElementsInOrReadingElementsIn(String[] kanjiElem,
      String[] readingElem, Pageable pageable);

  Page<Word> findDistinctByKanjiElementsLikeOrderByPriority(String kanji, Pageable pageable);
}
