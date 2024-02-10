package com.yomimashou.creator.dictionary.kanji.kanjicomponents;

import com.yomimashou.appscommon.model.Kanji;
import com.yomimashou.appscommon.model.KanjiReferences;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.DicNumber;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.DicRef;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Component
@Slf4j
public class KanjiDicNumberComponent implements KanjiComponent {

    private static final char UNDERSCORE = '_';
    private static final String SETTER_METHOD_PREFIX = "set";

    @Override
    public boolean applies(final KanjiComponentType kanjiComponentType) {
        return KanjiComponentType.DICNUMBER.equals(kanjiComponentType);
    }

    @Override
    public Kanji enrich(final Kanji kanji, final Object component) {
        setAllKanjiDictionaryReferences(kanji.getReferences(), (DicNumber) component);
        return kanji;
    }

    private void setAllKanjiDictionaryReferences(final KanjiReferences references, final DicNumber component) {
        final List<DicRef> dicRefs = component.getDicRef();

        for (final DicRef dicRef : dicRefs) {
            try {
                KanjiReferences.class
                        .getMethod(SETTER_METHOD_PREFIX + convertSnakeCaseToPascalCase(dicRef.getDrType()), String.class)
                        .invoke(references, dicRef.getContent());
            } catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                log.error("Could not set field: " + dicRef.getDrType(), e);
            }
        }
    }

    private String convertSnakeCaseToPascalCase(final String snakeCaseText) {
        return StringUtils.remove(WordUtils.capitalizeFully(snakeCaseText, UNDERSCORE), UNDERSCORE);
    }
}
