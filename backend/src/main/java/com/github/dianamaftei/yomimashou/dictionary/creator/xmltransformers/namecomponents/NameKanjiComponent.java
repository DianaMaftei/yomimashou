package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers.namecomponents;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.KEle;
import com.github.dianamaftei.yomimashou.dictionary.name.Name;
import org.springframework.stereotype.Component;

@Component
public class NameKanjiComponent implements NameComponent {

  @Override
  public boolean applies(final NameComponentType nameComponentType) {
    return NameComponentType.KELE.equals(nameComponentType);
  }

  @Override
  public Name enrich(final Name name, final Object component) {
    final KEle kanjiElement = (KEle) component;
    name.setKanji(kanjiElement.getKeb());
    return name;
  }
}
