package com.yomimashou.creator.dictionary.kanji;

import com.yomimashou.appscommon.model.Kanji;
import com.yomimashou.appscommon.service.KanjiCategoriesService;
import com.yomimashou.creator.dictionary.kanji.kanjicomponents.*;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Character;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KanjiParserTest {

    @InjectMocks
    private KanjiParser kanjiParser;

    @Mock
    private RtkService rtkService;
    @Mock
    private KanjiCategoriesService kanjiCategoriesService;
    @Mock
    private KanjiCharacterComponent kanjiCharacterComponent;
    @Mock
    private KanjiCodepointComponent kanjiCodepointComponent;
    @Mock
    private KanjiDicNumberComponent kanjiDicNumberComponent;
    @Mock
    private KanjiMiscComponent kanjiMiscComponent;
    @Mock
    private KanjiQueryCodeComponent kanjiQueryCodeComponent;
    @Mock
    private KanjiRadicalComponent kanjiRadicalComponent;
    @Mock
    private KanjiReadingMeaningComponent kanjiReadingMeaningComponent;

    @Spy
    private List<KanjiComponent> kanjiComponentsEnrichers = new ArrayList<>();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        kanjiComponentsEnrichers.add(kanjiCharacterComponent);
        kanjiComponentsEnrichers.add(kanjiCodepointComponent);
        kanjiComponentsEnrichers.add(kanjiDicNumberComponent);
        kanjiComponentsEnrichers.add(kanjiMiscComponent);
        kanjiComponentsEnrichers.add(kanjiQueryCodeComponent);
        kanjiComponentsEnrichers.add(kanjiRadicalComponent);
        kanjiComponentsEnrichers.add(kanjiReadingMeaningComponent);
    }

    @Test
    void parseShouldCheckAllKanjiComponentEnrichersForTheCharacter() {
        final Character character = new Character();
        character.getLiteralAndCodepointAndRadical().add("大");

        kanjiParser.parse(character);

        verify(kanjiCharacterComponent, times(1)).applies(KanjiComponentType.STRING);
        verify(kanjiCodepointComponent, times(1)).applies(KanjiComponentType.STRING);
        verify(kanjiDicNumberComponent, times(1)).applies(KanjiComponentType.STRING);
        verify(kanjiMiscComponent, times(1)).applies(KanjiComponentType.STRING);
        verify(kanjiQueryCodeComponent, times(1)).applies(KanjiComponentType.STRING);
        verify(kanjiRadicalComponent, times(1)).applies(KanjiComponentType.STRING);
        verify(kanjiReadingMeaningComponent, times(1)).applies(KanjiComponentType.STRING);
    }

    @Test
    void parseShouldExtractKanjiLiteral() {
        final Character character = new Character();
        character.getLiteralAndCodepointAndRadical().add("大");
        when(kanjiCharacterComponent.applies(KanjiComponentType.STRING)).thenReturn(true);

        kanjiParser.parse(character);

        verify(kanjiCharacterComponent, times(1)).enrich(any(), eq("大"));
    }

    @Test
    void parseShouldExtractReadingAndMeaning() {
        final Character character = new Character();
        final ReadingMeaning readingMeaning = new ReadingMeaning();
        character.getLiteralAndCodepointAndRadical().add(readingMeaning);
        when(kanjiReadingMeaningComponent.applies(KanjiComponentType.READINGMEANING)).thenReturn(true);

        kanjiParser.parse(character);

        verify(kanjiReadingMeaningComponent, times(1)).enrich(any(), eq(readingMeaning));
    }

    @Test
    void parseShouldExtractCodePoint() {
        final Character character = new Character();
        final Codepoint codepoint = new Codepoint();
        character.getLiteralAndCodepointAndRadical().add(codepoint);
        when(kanjiCodepointComponent.applies(KanjiComponentType.CODEPOINT)).thenReturn(true);

        kanjiParser.parse(character);

        verify(kanjiCodepointComponent, times(1)).enrich(any(), eq(codepoint));
    }


    @Test
    void parseShouldExtractRadical() {
        final Character character = new Character();
        final Radical radical = new Radical();
        character.getLiteralAndCodepointAndRadical().add(radical);
        when(kanjiRadicalComponent.applies(KanjiComponentType.RADICAL)).thenReturn(true);

        kanjiParser.parse(character);

        verify(kanjiRadicalComponent, times(1)).enrich(any(), eq(radical));
    }

    @Test
    void parseShouldExtractQueryCode() {
        final Character character = new Character();
        final QueryCode queryCode = new QueryCode();
        character.getLiteralAndCodepointAndRadical().add(queryCode);
        when(kanjiQueryCodeComponent.applies(KanjiComponentType.QUERYCODE)).thenReturn(true);

        kanjiParser.parse(character);

        verify(kanjiQueryCodeComponent, times(1)).enrich(any(), eq(queryCode));
    }

    @Test
    void parseShouldExtractMisc() {
        final Character character = new Character();
        final Misc misc = new Misc();
        character.getLiteralAndCodepointAndRadical().add(misc);

        when(kanjiMiscComponent.applies(KanjiComponentType.MISC)).thenReturn(true);

        kanjiParser.parse(character);

        verify(kanjiMiscComponent, times(1)).enrich(any(), eq(misc));
    }

    @Test
    void parseShouldExtractDictionaryReferences() {
        final Character character = new Character();
        final DicRef dicRef = new DicRef();
        final DicNumber dicNumber = new DicNumber();
        dicNumber.getDicRef().add(dicRef);
        character.getLiteralAndCodepointAndRadical().add(dicNumber);

        when(kanjiDicNumberComponent.applies(KanjiComponentType.DICNUMBER)).thenReturn(true);

        kanjiParser.parse(character);

        verify(kanjiDicNumberComponent, times(1)).enrich(any(), eq(dicNumber));
    }

    @Test
    void parseShouldPopulateNewJLPTLevel() {
        final Character character = new Character();
        character.getLiteralAndCodepointAndRadical().add("大");
        when(kanjiCategoriesService.getJlptLevel('大')).thenReturn("mock N5");
        populateKanjiLiteral("大");

        Kanji kanji = kanjiParser.parse(character);

        assertEquals("mock N5", kanji.getReferences().getJlptNewLevel());
    }

    @Test
    void parseShouldNotPopulateNewJLPTLevelIfAbsent() {
        final Character character = new Character();

        Kanji kanji = kanjiParser.parse(character);
        assertNull(kanji.getReferences().getJlptNewLevel());
        verify(kanjiCategoriesService, never()).getJlptLevel(anyChar());
    }

    @Test
    void buildKanjiEntryShouldPopulateRtkInfoIfPresent() {
        final RtkKanji rtkKanji = new RtkKanji();
        rtkKanji.setKeyword("mock large");
        rtkKanji.setComponents("mock 大: big, big radical (no. 37)");
        rtkKanji.setStory1(
                "mock A person stretching their arms and legs to look large and scare a bear away.");
        rtkKanji.setStory2("mock Think of a large St. Bernard dog with his paws all stretched out.");
        when(rtkService.get('大')).thenReturn(rtkKanji);
        final Character character = new Character();
        character.getLiteralAndCodepointAndRadical().add("大");
        populateKanjiLiteral("大");

        Kanji kanji = kanjiParser.parse(character);

        assertAll("Should populate kanji with all the values from the rtkkanji object",
                () -> assertEquals(rtkKanji.getKeyword(), kanji.getKeyword()),
                () -> assertEquals(rtkKanji.getComponents(), kanji.getComponents()),
                () -> assertEquals(rtkKanji.getStory1(), kanji.getStory1()),
                () -> assertEquals(rtkKanji.getStory2(), kanji.getStory2())
        );
    }

    @Test
    void buildKanjiEntryShouldNotPopulateRtkInfoIfAbsent() {
        when(rtkService.get('大')).thenReturn(null);
        final Character character = new Character();
        character.getLiteralAndCodepointAndRadical().add("大");
        populateKanjiLiteral("大");

        Kanji kanji = kanjiParser.parse(character);

        assertAll("Should not populate kanji with rtk values if it can't find the rtk kanji",
                () -> assertNull(kanji.getKeyword()),
                () -> assertNull(kanji.getComponents()),
                () -> assertNull(kanji.getStory1()),
                () -> assertNull(kanji.getStory2())
        );
    }

    private void populateKanjiLiteral(String character) {
        when(kanjiCharacterComponent.applies(KanjiComponentType.STRING)).thenReturn(true);
        doAnswer(invocation -> {
            Kanji kanji = invocation.getArgument(0);
            kanji.setCharacter(character);
            return null;
        }).when(kanjiCharacterComponent).enrich(any(Kanji.class), eq(character));
    }
}