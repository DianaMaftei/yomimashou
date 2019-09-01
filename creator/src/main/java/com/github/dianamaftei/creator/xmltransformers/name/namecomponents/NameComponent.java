package com.github.dianamaftei.creator.xmltransformers.name.namecomponents;

import com.github.dianamaftei.appscommon.model.Name;
import org.springframework.stereotype.Component;

@Component
public interface NameComponent {

  boolean applies(NameComponentType nameComponentType);

  Name enrich(Name name, Object component);

}
