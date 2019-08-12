package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers.kanjicomponents;

import com.github.dianamaftei.yomimashou.dictionary.kanji.Kanji;
import org.springframework.stereotype.Component;

@Component
public interface KanjiComponent {

  boolean applies(KanjiComponentType kanjiComponentType);

  Kanji enrich(Kanji kanji, Object component);

}
