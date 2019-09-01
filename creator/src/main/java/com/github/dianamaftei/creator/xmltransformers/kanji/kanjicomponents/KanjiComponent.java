package com.github.dianamaftei.creator.xmltransformers.kanji.kanjicomponents;

import com.github.dianamaftei.appscommon.model.Kanji;
import org.springframework.stereotype.Component;

@Component
public interface KanjiComponent {

  boolean applies(KanjiComponentType kanjiComponentType);

  Kanji enrich(Kanji kanji, Object component);

}
