package com.yomimashou.creator.dictionary.kanji.kanjicomponents;

import com.yomimashou.appscommon.model.Kanji;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Codepoint;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.CpValue;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class KanjiCodepointComponent implements KanjiComponent {

    @Override
    public boolean applies(final KanjiComponentType kanjiComponentType) {
        return KanjiComponentType.CODEPOINT.equals(kanjiComponentType);
    }

    @Override
    public Kanji enrich(final Kanji kanji, final Object component) {
        final List<String> codepoints = new ArrayList<>();
        if (!CollectionUtils.isEmpty(((Codepoint) component).getCpValue())) {
            for (final CpValue cpValue : ((Codepoint) component).getCpValue()) {
                codepoints.add(cpValue.getContent() + ";" + cpValue.getCpType());
            }

            kanji.setCodepoint(String.join("|", codepoints));
        }
        return kanji;
    }
}
