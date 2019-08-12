package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers.namecomponents;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.REle;
import com.github.dianamaftei.yomimashou.dictionary.name.Name;
import org.springframework.stereotype.Component;

@Component
public class NameReadingComponent implements NameComponent {

  @Override
  public boolean applies(final NameComponentType nameComponentType) {
    return NameComponentType.RELE.equals(nameComponentType);
  }

  @Override
  public Name enrich(final Name name, final Object component) {
    final REle readingElement = (REle) component;
    name.setReading(readingElement.getReb());
    return name;
  }
}
