package com.yomimashou.creator.dictionary.kanji;

import com.yomimashou.appscommon.model.Kanji;
import com.yomimashou.appscommon.service.KanjiCategoriesService;
import com.yomimashou.creator.dictionary.kanji.kanjicomponents.KanjiCharacterComponent;
import com.yomimashou.creator.dictionary.kanji.kanjicomponents.KanjiComponent;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Character;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Kanjidic2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KanjiEntriesFromXMLToPOJOTest {

    @InjectMocks
    private KanjiEntriesFromXMLToPOJO kanjiEntriesFromXMLToPOJO;

    @Mock
    private KanjiRepository kanjiRepository;

    @Mock
    private Kanjidic2 dictionaryFile;

    @Mock
    private KanjiCategoriesService kanjiCategoriesService;

    @Captor
    private ArgumentCaptor<List<Kanji>> argumentCaptor;

    @BeforeEach
    void setUp() {
        final List<KanjiComponent> enrichers = new ArrayList<>();
        enrichers.add(new KanjiCharacterComponent());
        kanjiEntriesFromXMLToPOJO.setKanjiComponentsEnrichers(enrichers);
        kanjiEntriesFromXMLToPOJO.setRtkKanjiMap(Collections.emptyMap());
        lenient().when(kanjiCategoriesService.getJlptLevel(anyChar())).thenReturn("mock jlpt level");
    }

    @Test
    void persistShouldAddAllKanjidicCharactersToTheDatabase() {
        final List<Character> characters = new ArrayList<>();
        characters.add(new Character());
        characters.add(new Character());
        characters.add(new Character());

        kanjiEntriesFromXMLToPOJO.persist(characters);

        verify(kanjiRepository, times(1)).saveAll(argumentCaptor.capture());
    }

    @Test
    void persistShouldParseAllKanji() {
        final List<Character> characters = new ArrayList<>();
        final Character character1 = new Character();
        character1.getLiteralAndCodepointAndRadical().add("大");
        characters.add(character1);
        final Character character2 = new Character();
        character2.getLiteralAndCodepointAndRadical().add("国");
        characters.add(character2);

        kanjiEntriesFromXMLToPOJO.persist(characters);

        verify(kanjiRepository, times(1)).saveAll(argumentCaptor.capture());

        final List<Kanji> allArgumentValues = argumentCaptor.getAllValues()
                .stream().flatMap(List::stream).sorted(Comparator.comparing(Kanji::getCharacter))
                .collect(Collectors.toList());

        assertAll("Should contain the kanji literals from the Characters list",
                () -> assertEquals(2, allArgumentValues.size()),
                () -> assertEquals("国", allArgumentValues.get(0).getCharacter()),
                () -> assertEquals("大", allArgumentValues.get(1).getCharacter())
        );
    }

    @Test
    void getEntriesShouldReturnAListOfCharacterEntries() {
        final List<Character> entries = kanjiEntriesFromXMLToPOJO.getEntries(dictionaryFile);
        assertEquals(dictionaryFile.getCharacter(), entries);
    }

    @Test
    void getClassForJaxbShouldReturnKanjidic2() {
        assertEquals(Kanjidic2.class, kanjiEntriesFromXMLToPOJO.getClassForJaxb());
    }
}
