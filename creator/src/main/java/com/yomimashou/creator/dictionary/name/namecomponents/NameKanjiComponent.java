package com.yomimashou.creator.dictionary.name.namecomponents;

import com.yomimashou.appscommon.model.Name;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.KEle;
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
