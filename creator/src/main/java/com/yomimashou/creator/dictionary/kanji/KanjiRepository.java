package com.yomimashou.creator.dictionary.kanji;

import com.yomimashou.appscommon.model.Kanji;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KanjiRepository extends JpaRepository<Kanji, Long> {

}
