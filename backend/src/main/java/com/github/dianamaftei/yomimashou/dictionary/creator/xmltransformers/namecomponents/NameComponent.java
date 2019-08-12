package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers.namecomponents;

import com.github.dianamaftei.yomimashou.dictionary.name.Name;
import org.springframework.stereotype.Component;

@Component
public interface NameComponent {

  boolean applies(NameComponentType nameComponentType);

  Name enrich(Name name, Object component);

}
