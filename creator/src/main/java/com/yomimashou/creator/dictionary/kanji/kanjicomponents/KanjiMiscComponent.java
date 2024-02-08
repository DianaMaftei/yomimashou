package com.yomimashou.creator.dictionary.kanji.kanjicomponents;

import com.yomimashou.appscommon.model.Kanji;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Misc;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class KanjiMiscComponent implements KanjiComponent {

  @Override
  public boolean applies(final KanjiComponentType kanjiComponentType) {
    return KanjiComponentType.MISC.equals(kanjiComponentType);
  }

  @Override
  public Kanji enrich(final Kanji kanji, final Object component) {
    final Misc misc = (Misc) component;
    if (!StringUtils.isEmpty(misc.getGrade())) {
      kanji.setGrade(Integer.valueOf(misc.getGrade()));
    }
    //  the first is the accepted count, the rest are common miscounts
    if (!CollectionUtils.isEmpty(misc.getStrokeCount()) && !StringUtils
        .isEmpty(misc.getStrokeCount().get(0))) {
      kanji.setStrokeCount(Integer.valueOf(misc.getStrokeCount().get(0)));
    }

    if (!StringUtils.isEmpty(misc.getFreq())) {
      kanji.setFrequency(Integer.valueOf(misc.getFreq()));
    }

    kanji.getReferences().setJlptOldLevel(misc.getJlpt());
    return kanji;
  }
}
