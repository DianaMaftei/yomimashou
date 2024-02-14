package com.yomimashou.creator.dictionary.kanji.kanjicomponents;

import com.yomimashou.appscommon.model.Kanji;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Character;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.QCode;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.QueryCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class KanjiQueryCodeComponentTest {
    private final KanjiQueryCodeComponent kanjiQueryCodeComponent = new KanjiQueryCodeComponent();

    @ParameterizedTest
    @EnumSource(KanjiComponentType.class)
    void applies_should_returnTrue_onlyFor_codepointComponentType(KanjiComponentType type) {
        boolean expected = type == KanjiComponentType.QUERYCODE;
        assertEquals(expected, kanjiQueryCodeComponent.applies(type));
    }

    @Test
    void enrich_shouldParse_codepoint() {
        final Character character = new Character();
        final QueryCode queryCode = new QueryCode();
        final QCode qCode = new QCode();
        qCode.setQcType("skip");
        qCode.setContent("skip code");
        queryCode.getQCode().add(qCode);
        character.getLiteralAndCodepointAndRadical().add(queryCode);

        Kanji kanji = new Kanji();

        kanjiQueryCodeComponent.enrich(kanji, queryCode);
        assertEquals("skip code", kanji.getSkipCode());
    }

    @Test
    void enrich_shouldNotParse_queryCode_ifAbsent() {
        final Character character = new Character();
        final QueryCode queryCode = new QueryCode();
        character.getLiteralAndCodepointAndRadical().add(queryCode);

        Kanji kanji = new Kanji();

        kanjiQueryCodeComponent.enrich(kanji, queryCode);
        assertNull(kanji.getSkipCode());
    }
}