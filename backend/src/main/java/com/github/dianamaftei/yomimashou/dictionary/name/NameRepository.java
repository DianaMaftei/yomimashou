package com.github.dianamaftei.yomimashou.dictionary.name;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NameRepository extends JpaRepository<Name, Long> {

  Page<Name> findDistinctByKanjiInOrReadingIn(String[] kanjiElem,
      String[] readingElem, Pageable pageable);
}
