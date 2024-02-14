package com.yomimashou.creator.dictionary.name;

import com.yomimashou.appscommon.model.Name;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.Entry;
import com.yomimashou.creator.dictionary.name.namecomponents.NameComponent;
import com.yomimashou.creator.dictionary.name.namecomponents.NameComponentType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class NameParser {

    private List<NameComponent> nameComponentsEnrichers;

    public Name parse(final Entry name) {
        final Name nameEntry = new Name();
        final List<Object> entSeqOrKEleOrREleOrTrans = name.getEntSeqOrKEleOrREleOrTrans();
        for (final Object component : entSeqOrKEleOrREleOrTrans) {
            final NameComponentType nameComponentType = NameComponentType
                    .valueOf(component.getClass().getSimpleName().toUpperCase());

            nameComponentsEnrichers.stream()
                    .filter(enricher -> enricher.applies(nameComponentType))
                    .forEach(enricher -> enricher.enrich(nameEntry, component));
        }

        return nameEntry;
    }
}
