package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers.kanjicomponents;

import com.github.dianamaftei.yomimashou.dictionary.kanji.Kanji;
import org.springframework.stereotype.Component;

@Component
public class KanjiCharacterComponent implements KanjiComponent {

  @Override
  public boolean applies(final KanjiComponentType kanjiComponentType) {
    return KanjiComponentType.STRING.equals(kanjiComponentType);
  }

  @Override
  public Kanji enrich(final Kanji kanji, final Object component) {
    final String character = (String) component;
    kanji.setCharacter(character);
    return kanji;
  }
}
