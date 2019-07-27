package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.Character;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.Kanjidic2;
import com.github.dianamaftei.yomimashou.dictionary.kanji.Kanji;
import com.github.dianamaftei.yomimashou.dictionary.kanji.KanjiRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class KanjiEntriesFromXMLToPOJOTest {

  @InjectMocks
  private KanjiEntriesFromXMLToPOJO kanjiEntriesFromXMLToPOJO;

  @Mock
  private KanjiRepository kanjiRepository;

  @Mock
  private Kanjidic2 dictionaryFile;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(kanjiEntriesFromXMLToPOJO, "filePath", "mockFilePath");
  }

  @Test
  void saveToDbShouldAddAllKanjidicCharactersToTheDatabase() {
    List<Character> characters = new ArrayList<>();
    characters.add(new Character());
    characters.add(new Character());
    characters.add(new Character());

    kanjiEntriesFromXMLToPOJO.saveToDb(characters);

    verify(kanjiRepository, times(3)).save(any(Kanji.class));
  }

  @Test
  void getEntriesShouldReturnAListOfCharacterEntries() {
    List<Character> entries = kanjiEntriesFromXMLToPOJO.getEntries(dictionaryFile);
    assertEquals(dictionaryFile.getCharacter(), entries);
  }

  @Test
  void getClassForJaxbShouldReturnKanjidic2() {
    assertEquals(Kanjidic2.class, kanjiEntriesFromXMLToPOJO.getClassForJaxb());
  }

  @Test
  void saveToFileShouldParseAllKanjiLiteralsFromKanjiEntries() {
    List<Character> characters = new ArrayList<>();
    final Character character1 = new Character();
    character1.getLiteralAndCodepointAndRadical().add("大");
    characters.add(character1);
    final Character character2 = new Character();
    character2.getLiteralAndCodepointAndRadical().add("国");
    characters.add(character2);

    KanjiEntriesFromXMLToPOJO spyKanjiEntriesFromXMLToPOJO = spy(kanjiEntriesFromXMLToPOJO);
    doNothing().when((XMLEntryToPOJO) spyKanjiEntriesFromXMLToPOJO).writeToFile(any(), any());
    ArgumentCaptor<Set> argument = ArgumentCaptor.forClass(Set.class);
    spyKanjiEntriesFromXMLToPOJO.saveToFile(characters);

    verify(spyKanjiEntriesFromXMLToPOJO).writeToFile(argument.capture(), any(String.class));

    assertAll("Should contain the kanji literals from the Characters list",
        () -> assertTrue(argument.getValue().contains("大")),
        () -> assertTrue(argument.getValue().contains("国")),
        () -> assertEquals(2, argument.getValue().size())
    );
  }

  @Test
  void saveToFileShouldNotSaveDuplicateKanji() {
    List<Character> characters = new ArrayList<>();
    final Character character1 = new Character();
    character1.getLiteralAndCodepointAndRadical().add("大");
    characters.add(character1);
    final Character character2 = new Character();
    character2.getLiteralAndCodepointAndRadical().add("大");
    characters.add(character2);
    final Character character3 = new Character();
    character3.getLiteralAndCodepointAndRadical().add("国");
    characters.add(character3);

    KanjiEntriesFromXMLToPOJO spyKanjiEntriesFromXMLToPOJO = spy(kanjiEntriesFromXMLToPOJO);
    doNothing().when((XMLEntryToPOJO) spyKanjiEntriesFromXMLToPOJO).writeToFile(any(), any());
    ArgumentCaptor<Set> argument = ArgumentCaptor.forClass(Set.class);
    spyKanjiEntriesFromXMLToPOJO.saveToFile(characters);

    verify(spyKanjiEntriesFromXMLToPOJO).writeToFile(argument.capture(), any(String.class));

    assertAll("Should contain the kanji literals from the Characters list, without duplicates",
        () -> assertTrue(argument.getValue().contains("大")),
        () -> assertTrue(argument.getValue().contains("国")),
        () -> assertEquals(2, argument.getValue().size())
    );
  }

  @Test
  void writeToFileShouldSaveEntriesToFile() {
    kanjiEntriesFromXMLToPOJO.writeToFile(null, null);
  }
}
