package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers.namecomponents;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.NameType;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.Trans;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmnedict.TransDet;
import com.github.dianamaftei.yomimashou.dictionary.name.Name;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class NameTranslationComponent implements NameComponent {

  @Override
  public boolean applies(final NameComponentType nameComponentType) {
    return NameComponentType.TRANS.equals(nameComponentType);
  }

  @Override
  public Name enrich(final Name name, final Object component) {
    final Trans translationElement = (Trans) component;
    final List<String> nameTypeList = translationElement.getNameType()
        .stream().map(NameType::getvalue).collect(Collectors.toList());
    final List<String> transList = translationElement.getTransDet()
        .stream().map(TransDet::getvalue).collect(Collectors.toList());

    name.setType(String.join("|", nameTypeList));
    name.setTranslations(String.join("|", transList));
    return name;
  }
}
