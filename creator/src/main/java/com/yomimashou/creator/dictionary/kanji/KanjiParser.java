package com.yomimashou.creator.dictionary.kanji;

import com.yomimashou.appscommon.model.Kanji;
import com.yomimashou.appscommon.model.KanjiReferences;
import com.yomimashou.appscommon.service.KanjiCategoriesService;
import com.yomimashou.creator.dictionary.kanji.kanjicomponents.KanjiComponent;
import com.yomimashou.creator.dictionary.kanji.kanjicomponents.KanjiComponentType;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Character;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class KanjiParser {

    private KanjiCategoriesService kanjiCategoriesService;
    private RtkService rtkService;
    private List<KanjiComponent> kanjiComponentsEnrichers;

    public Kanji parse(final Character character) {
        final Kanji kanjiEntry = new Kanji();
        kanjiEntry.setReferences(new KanjiReferences());

        final List<Object> kanjiComponents = character.getLiteralAndCodepointAndRadical();

        for (final Object component : kanjiComponents) {
            final KanjiComponentType kanjiComponentType = KanjiComponentType
                    .valueOf(component.getClass().getSimpleName().toUpperCase());

            kanjiComponentsEnrichers.stream()
                    .filter(enricher -> enricher.applies(kanjiComponentType))
                    .forEach(enricher -> enricher.enrich(kanjiEntry, component));
        }

        enrichKanjiEntryWithJLPTInfo(kanjiEntry);
        enrichKanjiEntryWithRtkInfo(kanjiEntry);

        return kanjiEntry;
    }

    private void enrichKanjiEntryWithJLPTInfo(final Kanji kanjiEntry) {
        final String character = kanjiEntry.getCharacter();
        if (StringUtils.isNotBlank(character)) {
            kanjiEntry.getReferences().setJlptNewLevel(kanjiCategoriesService.getJlptLevel(character.charAt(0)));
        }
    }

    private void enrichKanjiEntryWithRtkInfo(final Kanji kanjiEntry) {
        final String character = kanjiEntry.getCharacter();
        if (StringUtils.isNotBlank(character)) {
            final RtkKanji rtkKanji = rtkService.get(character.charAt(0));
            if (rtkKanji != null) {
                kanjiEntry.setKeyword(rtkKanji.getKeyword());
                kanjiEntry.setComponents(rtkKanji.getComponents());
                kanjiEntry.setStory1(rtkKanji.getStory1());
                kanjiEntry.setStory2(rtkKanji.getStory2());
            }
        }
    }
}
