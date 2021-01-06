package com.yomimashou.creator.xmltransformers.kanji.kanjicomponents;

import com.yomimashou.appscommon.model.Kanji;
import org.springframework.stereotype.Component;

@Component
public interface KanjiComponent {

  boolean applies(KanjiComponentType kanjiComponentType);

  Kanji enrich(Kanji kanji, Object component);

}
