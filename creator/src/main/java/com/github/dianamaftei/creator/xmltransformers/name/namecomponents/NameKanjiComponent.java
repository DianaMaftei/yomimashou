package com.github.dianamaftei.creator.xmltransformers.name.namecomponents;

import com.github.dianamaftei.appscommon.model.Name;
import com.github.dianamaftei.creator.jaxbgeneratedmodels.jmnedict.KEle;
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
