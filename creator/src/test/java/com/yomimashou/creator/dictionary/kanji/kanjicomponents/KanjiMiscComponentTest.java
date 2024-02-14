package com.yomimashou.creator.dictionary.kanji.kanjicomponents;

import com.yomimashou.appscommon.model.Kanji;
import com.yomimashou.appscommon.model.KanjiReferences;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Character;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Misc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class KanjiMiscComponentTest {
    private final KanjiMiscComponent kanjiMiscComponent = new KanjiMiscComponent();

    @ParameterizedTest
    @EnumSource(KanjiComponentType.class)
    void applies_should_returnTrue_onlyFor_miscComponentType(KanjiComponentType type) {
        boolean expected = type == KanjiComponentType.MISC;
        assertEquals(expected, kanjiMiscComponent.applies(type));
    }

    @Test
    void enrich_shouldParse_grade() {
        final Character character = new Character();
        final Misc misc = new Misc();
        misc.setGrade("5");
        character.getLiteralAndCodepointAndRadical().add(misc);

        Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiMiscComponent.enrich(kanji, misc);

        assertEquals(Integer.valueOf(5), kanji.getGrade());
    }

    @Test
    void enrich_shouldNotParse_grade_ifAbsent() {
        final Character character = new Character();
        final Misc misc = new Misc();
        character.getLiteralAndCodepointAndRadical().add(misc);

        Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiMiscComponent.enrich(kanji, misc);

        assertNull(kanji.getGrade());
    }

    @Test
    void enrich_shouldParse_strokeCount() {
        final Character character = new Character();
        final Misc misc = new Misc();
        misc.getStrokeCount().add("7");
        character.getLiteralAndCodepointAndRadical().add(misc);

        Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiMiscComponent.enrich(kanji, misc);

        assertEquals(Integer.valueOf(7), kanji.getStrokeCount());
    }

    @Test
    void enrich_shouldNotParse_strokeCount_ifAbsent() {
        final Character character = new Character();
        final Misc misc = new Misc();
        character.getLiteralAndCodepointAndRadical().add(misc);

        Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiMiscComponent.
                enrich(kanji, misc);

        assertNull(kanji.getStrokeCount());
    }

    @Test
    void enrich_shouldParse_frequency() {
        final Character character = new Character();
        final Misc misc = new Misc();
        misc.setFreq("8");
        character.getLiteralAndCodepointAndRadical().add(misc);

        Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiMiscComponent.enrich(kanji, misc);

        assertEquals(Integer.valueOf(8), kanji.getFrequency());
    }

    @Test
    void enrich_shouldNotParse_frequency_ifAbsent() {
        final Character character = new Character();
        final Misc misc = new Misc();
        character.getLiteralAndCodepointAndRadical().add(misc);

        Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiMiscComponent.enrich(kanji, misc);

        assertNull(kanji.getFrequency());
    }

    @Test
    void enrich_shouldParse_oldJLPTLevel() {
        final Character character = new Character();
        final Misc misc = new Misc();
        misc.setJlpt("4");
        character.getLiteralAndCodepointAndRadical().add(misc);

        Kanji kanji = new Kanji();
        kanji.setReferences(new KanjiReferences());

        kanjiMiscComponent.enrich(kanji, misc);

        assertEquals("4", kanji.getReferences().getJlptOldLevel());
    }
}