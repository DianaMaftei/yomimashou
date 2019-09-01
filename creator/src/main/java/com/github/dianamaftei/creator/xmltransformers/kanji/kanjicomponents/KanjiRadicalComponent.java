package com.github.dianamaftei.creator.xmltransformers.kanji.kanjicomponents;

import com.github.dianamaftei.appscommon.model.Kanji;
import com.github.dianamaftei.creator.jaxbgeneratedmodels.kanjidic.Radical;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class KanjiRadicalComponent implements KanjiComponent {

  @Override
  public boolean applies(final KanjiComponentType kanjiComponentType) {
    return KanjiComponentType.RADICAL.equals(kanjiComponentType);
  }

  @Override
  public Kanji enrich(final Kanji kanji, final Object component) {
    // only one main radical
    if (!CollectionUtils.isEmpty(((Radical) component).getRadValue())) {
      kanji.setRadical(((Radical) component).getRadValue().get(0).getContent());
    }
    return kanji;
  }
}
