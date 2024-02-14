package com.yomimashou.creator.dictionary.name.namecomponents;

import com.yomimashou.appscommon.model.Name;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.REle;
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
