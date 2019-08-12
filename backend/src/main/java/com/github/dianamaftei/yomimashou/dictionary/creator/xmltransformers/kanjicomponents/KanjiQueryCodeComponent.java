package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers.kanjicomponents;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.QCode;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.QueryCode;
import com.github.dianamaftei.yomimashou.dictionary.kanji.Kanji;
import org.springframework.stereotype.Component;

@Component
public class KanjiQueryCodeComponent implements KanjiComponent {

  private static final String SKIP = "skip";

  @Override
  public boolean applies(final KanjiComponentType kanjiComponentType) {
    return KanjiComponentType.QUERYCODE.equals(kanjiComponentType);
  }

  @Override
  public Kanji enrich(final Kanji kanji, final Object component) {
    final QueryCode queryCode = (QueryCode) component;
    for (final QCode qCode : queryCode.getQCode()) {
      if (SKIP.equals(qCode.getQcType())) {
        kanji.setSkipCode(qCode.getContent());
      }
    }
    return kanji;
  }

}
