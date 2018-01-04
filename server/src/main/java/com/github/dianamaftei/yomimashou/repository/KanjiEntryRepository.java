package com.github.dianamaftei.yomimashou.repository;

import com.github.dianamaftei.yomimashou.model.KanjiEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface KanjiEntryRepository extends CrudRepository<KanjiEntry, Long> {
    KanjiEntry findByKanji(String kanji);
}
