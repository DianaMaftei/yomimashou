package com.yomimashou.creator.dictionary.kanji.kanjicomponents;

import com.yomimashou.appscommon.model.Kanji;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Character;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KanjiCharacterComponentTest {

    private final KanjiCharacterComponent kanjiCharacterComponent = new KanjiCharacterComponent();

    @ParameterizedTest
    @EnumSource(KanjiComponentType.class)
    void applies_should_returnTrue_onlyFor_stringComponentType(KanjiComponentType type) {
        boolean expected = type == KanjiComponentType.STRING;
        assertEquals(expected, kanjiCharacterComponent.applies(type));
    }

    @Test
    void enrich_shouldParse_kanji() {
        final Character character = new Character();
        character.getLiteralAndCodepointAndRadical().add("大");

        Kanji kanji = new Kanji();

        kanjiCharacterComponent.enrich(kanji, "大");

        assertEquals("大", kanji.getCharacter());
    }
}