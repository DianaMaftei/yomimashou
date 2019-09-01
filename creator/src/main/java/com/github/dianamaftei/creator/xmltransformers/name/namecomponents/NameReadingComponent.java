package com.github.dianamaftei.creator.xmltransformers.name.namecomponents;

import com.github.dianamaftei.appscommon.model.Name;
import com.github.dianamaftei.creator.jaxbgeneratedmodels.jmnedict.REle;
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
