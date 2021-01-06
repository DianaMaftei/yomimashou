package com.yomimashou.creator.xmltransformers.name.namecomponents;

import com.yomimashou.appscommon.model.Name;
import com.yomimashou.creator.jaxbgeneratedmodels.jmnedict.NameType;
import com.yomimashou.creator.jaxbgeneratedmodels.jmnedict.Trans;
import com.yomimashou.creator.jaxbgeneratedmodels.jmnedict.TransDet;
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
