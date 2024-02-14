package com.yomimashou.creator.dictionary.name.namecomponents;

import com.yomimashou.appscommon.model.Name;
import com.yomimashou.creator.dictionary.name.jmnedictXMLmodels.KEle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NameKanjiComponentTest {
    private final NameKanjiComponent nameKanjiComponent = new NameKanjiComponent();

    @ParameterizedTest
    @EnumSource(NameComponentType.class)
    void applies_should_returnTrue_onlyFor_keleComponentType(NameComponentType type) {
        boolean expected = type == NameComponentType.KELE;
        assertEquals(expected, nameKanjiComponent.applies(type));
    }

    @Test
    void enrich_shouldParse_kanji() {
        final KEle kEle = new KEle();
        kEle.setKeb("安部浩平");
        Name name = new Name();

        nameKanjiComponent.enrich(name, kEle);

        assertEquals(kEle.getKeb(), name.getKanji());
    }
}