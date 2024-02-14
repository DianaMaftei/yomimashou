package com.yomimashou.creator.dictionary.kanji;

import com.yomimashou.appscommon.model.Kanji;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Character;
import com.yomimashou.creator.dictionary.kanji.kanjidicXMLmodels.Kanjidic2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KanjiDictionaryServiceTest {

    @InjectMocks
    private KanjiDictionaryService kanjiDictionaryService;

    @Mock
    private KanjiRepository kanjiRepository;

    @Mock
    private KanjiParser kanjiParser;

    @Mock
    private Kanjidic2 dictionaryFile;

    @Captor
    private ArgumentCaptor<List<Kanji>> argumentCaptor;

    @Test
    void persistShouldParseAllCharacters() {
        final List<Character> characters = Arrays.asList(new Character(), new Character(), new Character());

        kanjiDictionaryService.persist(characters);

        verify(kanjiParser, times(3)).parse(any(Character.class));
    }

    @Test
    void persistShouldAddAllCharactersToTheDatabase() {
        final List<Character> characters = Arrays.asList(new Character(), new Character(), new Character());

        kanjiDictionaryService.persist(characters);

        verify(kanjiRepository, times(1)).saveAll(argumentCaptor.capture());
        assertEquals(3, argumentCaptor.getValue().size());
    }

    @Test
    void getEntriesShouldReturnAListOfCharacterEntries() {
        final List<Character> entries = kanjiDictionaryService.getEntries(dictionaryFile);
        assertEquals(dictionaryFile.getCharacter(), entries);
    }

    @Test
    void getClassForJaxbShouldReturnKanjidic2() {
        assertEquals(Kanjidic2.class, kanjiDictionaryService.getClassForJaxb());
    }
}
