package com.yomimashou.creator.dictionary.kanji.kanjicomponents;

import com.yomimashou.appscommon.model.Kanji;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Character;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KanjiReadingMeaningComponentTest {

    private final KanjiReadingMeaningComponent kanjiReadingMeaningComponent = new KanjiReadingMeaningComponent();

    @ParameterizedTest
    @EnumSource(KanjiComponentType.class)
    void applies_should_returnTrue_onlyFor_keleComponentType(KanjiComponentType type) {
        boolean expected = type == KanjiComponentType.READINGMEANING;
        assertEquals(expected, kanjiReadingMeaningComponent.applies(type));
    }

    @Test
    void enrich_shouldParse_meaning() {
        final Character character = new Character();
        final ReadingMeaning readingMeaning = new ReadingMeaning();
        final Rmgroup rmgroup = new Rmgroup();

        final Meaning meaning = new Meaning();
        meaning.setContent("big");
        rmgroup.getMeaning().add(meaning);

        final Meaning meaning2 = new Meaning();
        meaning2.setContent("large");
        rmgroup.getMeaning().add(meaning2);

        readingMeaning.getRmgroup().add(rmgroup);
        character.getLiteralAndCodepointAndRadical().add(readingMeaning);

        Kanji kanji = new Kanji();

        kanjiReadingMeaningComponent.enrich(kanji, readingMeaning);

        assertEquals("big|large", kanji.getMeaning());
    }

    @Test
    void enrich_shouldParse_onReadings() {
        final Character character = new Character();
        final ReadingMeaning readingMeaning = new ReadingMeaning();
        final Rmgroup rmgroup = new Rmgroup();

        final Reading ja_on = new Reading();
        ja_on.setRType("ja_on");
        ja_on.setContent("ダイ");
        rmgroup.getReading().add(ja_on);

        final Reading ja_on2 = new Reading();
        ja_on2.setRType("ja_on");
        ja_on2.setContent("タイ");
        rmgroup.getReading().add(ja_on2);

        readingMeaning.getRmgroup().add(rmgroup);
        character.getLiteralAndCodepointAndRadical().add(readingMeaning);

        Kanji kanji = new Kanji();

        kanjiReadingMeaningComponent.enrich(kanji, readingMeaning);

        assertEquals("ダイ|タイ", kanji.getOnReading());
    }

    @Test
    void enrich_shouldParse_kunReadings() {
        final Character character = new Character();
        final ReadingMeaning readingMeaning = new ReadingMeaning();
        final Rmgroup rmgroup = new Rmgroup();

        final Reading ja_kun = new Reading();
        ja_kun.setRType("ja_kun");
        ja_kun.setContent("おお-");
        rmgroup.getReading().add(ja_kun);

        final Reading ja_kun2 = new Reading();
        ja_kun2.setRType("ja_kun");
        ja_kun2.setContent("おお.きい");
        rmgroup.getReading().add(ja_kun2);

        readingMeaning.getRmgroup().add(rmgroup);
        character.getLiteralAndCodepointAndRadical().add(readingMeaning);

        Kanji kanji = new Kanji();

        kanjiReadingMeaningComponent.enrich(kanji, readingMeaning);

        assertEquals("おお-|おお.きい", kanji.getKunReading());
    }

}