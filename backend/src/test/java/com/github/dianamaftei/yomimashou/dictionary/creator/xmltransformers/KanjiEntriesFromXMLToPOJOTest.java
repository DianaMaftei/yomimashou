package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.Character;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.Kanjidic2;
import com.github.dianamaftei.yomimashou.dictionary.kanji.Kanji;
import com.github.dianamaftei.yomimashou.dictionary.kanji.KanjiRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    kanjiEntriesFromXMLToPOJO.setRtkKanjiMap(Collections.emptyMap());
  }

  @Test
  void saveToDbShouldAddAllKanjidicCharactersToTheDatabase() {
    final List<Character> characters = new ArrayList<>();
    characters.add(new Character());
    characters.add(new Character());
    characters.add(new Character());

    kanjiEntriesFromXMLToPOJO.saveToDb(characters);

    verify(kanjiRepository, times(3)).save(any(Kanji.class));
  }

  @Test
  void saveToDBShouldParseAllKanji() {
    final List<Character> characters = new ArrayList<>();
    final Character character1 = new Character();
    character1.getLiteralAndCodepointAndRadical().add("大");
    characters.add(character1);
    final Character character2 = new Character();
    character2.getLiteralAndCodepointAndRadical().add("国");
    characters.add(character2);

    final ArgumentCaptor<Kanji> argument = ArgumentCaptor.forClass(Kanji.class);
    kanjiEntriesFromXMLToPOJO.saveToDb(characters);

    verify(kanjiRepository, times(2)).save(argument.capture());

    final List<Kanji> allArgumentValues = argument.getAllValues()
        .stream().sorted(Comparator.comparing(Kanji::getCharacter)).collect(Collectors.toList());

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
