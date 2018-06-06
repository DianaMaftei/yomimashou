package com.github.dianamaftei.yomimashou.repository;

import com.github.dianamaftei.yomimashou.model.KanjiEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KanjiEntryRepository extends JpaRepository<KanjiEntry, Long> {
    KanjiEntry findByKanji(String kanji);
}
