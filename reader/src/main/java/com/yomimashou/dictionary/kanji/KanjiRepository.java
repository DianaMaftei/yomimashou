package com.yomimashou.dictionary.kanji;

import com.yomimashou.appscommon.model.Kanji;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface KanjiRepository extends JpaRepository<Kanji, Long> {

    Kanji findByCharacter(String character);

    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "references"
            }
    )
    List<Kanji> findByCharacterIn(Set<String> kanji);
}
