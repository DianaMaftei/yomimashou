package com.yomimashou.creator.dictionary.kanji.kanjicomponents;

import com.yomimashou.appscommon.model.Kanji;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Character;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.RadValue;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Radical;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class KanjiRadicalComponentTest {

    private final KanjiRadicalComponent kanjiRadicalComponent = new KanjiRadicalComponent();

    @ParameterizedTest
    @EnumSource(KanjiComponentType.class)
    void applies_should_returnTrue_onlyFor_radicalComponentType(KanjiComponentType type) {
        boolean expected = type == KanjiComponentType.RADICAL;
        assertEquals(expected, kanjiRadicalComponent.applies(type));
    }

    @Test
    void enrich_shouldParse_firstRadical() {
        final Character character = new Character();
        final Radical radical = new Radical();
        final RadValue radValue = new RadValue();
        radValue.setContent("radical value");
        radical.getRadValue().add(radValue);
        final RadValue radValue2 = new RadValue();
        radValue2.setContent("2nd radical value");
        radical.getRadValue().add(radValue2);
        character.getLiteralAndCodepointAndRadical().add(radical);

        Kanji kanji = new Kanji();

        kanjiRadicalComponent.enrich(kanji, radical);

        assertEquals("radical value", kanji.getRadical());
    }

    @Test
    void enrich_shouldNotParse_radical_ifAbsent() {
        final Character character = new Character();
        final Radical radical = new Radical();
        character.getLiteralAndCodepointAndRadical().add(radical);

        Kanji kanji = new Kanji();

        kanjiRadicalComponent.enrich(kanji, radical);
        assertNull(kanji.getRadical());
    }

}