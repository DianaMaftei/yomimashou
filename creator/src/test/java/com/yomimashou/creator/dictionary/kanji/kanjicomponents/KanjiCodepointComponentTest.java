package com.yomimashou.creator.dictionary.kanji.kanjicomponents;

import com.yomimashou.appscommon.model.Kanji;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Character;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Codepoint;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.CpValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class KanjiCodepointComponentTest {

    private final KanjiCodepointComponent kanjiCodepointComponent = new KanjiCodepointComponent();

    @ParameterizedTest
    @EnumSource(KanjiComponentType.class)
    void applies_should_returnTrue_onlyFor_codepointComponentType(KanjiComponentType type) {
        boolean expected = type == KanjiComponentType.CODEPOINT;
        assertEquals(expected, kanjiCodepointComponent.applies(type));
    }

    @Test
    void enrich_shouldParse_codepoint() {
        final Character character = new Character();
        final Codepoint codepoint = new Codepoint();
        final CpValue cpValue1 = new CpValue();
        cpValue1.setContent("5927");
        cpValue1.setCpType("ucs");
        final CpValue cpValue2 = new CpValue();
        cpValue2.setContent("34-71");
        cpValue2.setCpType("jis208");
        codepoint.getCpValue().add(cpValue1);
        codepoint.getCpValue().add(cpValue2);
        character.getLiteralAndCodepointAndRadical().add(codepoint);

        Kanji kanji = new Kanji();

        kanjiCodepointComponent.enrich(kanji, codepoint);
        assertEquals("5927;ucs|34-71;jis208", kanji.getCodepoint());
    }

    @Test
    void enrich_shouldNotParse_codepoint_ifAbsent() {
        final Character character = new Character();
        final Codepoint codepoint = new Codepoint();
        character.getLiteralAndCodepointAndRadical().add(codepoint);

        Kanji kanji = new Kanji();

        kanjiCodepointComponent.enrich(kanji, codepoint);
        assertNull(kanji.getCodepoint());
    }
}