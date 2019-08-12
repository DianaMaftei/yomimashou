package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers.kanjicomponents;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.Codepoint;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.CpValue;
import com.github.dianamaftei.yomimashou.dictionary.kanji.Kanji;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class KanjiCodepointComponent implements KanjiComponent {

  @Override
  public boolean applies(final KanjiComponentType kanjiComponentType) {
    return KanjiComponentType.CODEPOINT.equals(kanjiComponentType);
  }

  @Override
  public Kanji enrich(final Kanji kanji, final Object component) {
    final List<String> codepoints = new ArrayList<>();
    if (!CollectionUtils.isEmpty(((Codepoint) component).getCpValue())) {
      for (final CpValue cpValue : ((Codepoint) component).getCpValue()) {
        codepoints.add(cpValue.getContent() + ";" + cpValue.getCpType());
      }

      kanji.setCodepoint(String.join("|", codepoints));
    }
    return kanji;
  }
}
