package com.github.dianamaftei.yomimashou.dictionary.kanji;

import com.github.dianamaftei.appscommon.model.Kanji;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KanjiRepository extends JpaRepository<Kanji, Long> {

  Kanji findByCharacter(String character);
}
