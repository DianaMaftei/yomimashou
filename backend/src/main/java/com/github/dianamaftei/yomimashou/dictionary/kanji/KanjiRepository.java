package com.github.dianamaftei.yomimashou.dictionary.kanji;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KanjiRepository extends JpaRepository<Kanji, Long> {
    Kanji findByKanji(String kanji);
}
