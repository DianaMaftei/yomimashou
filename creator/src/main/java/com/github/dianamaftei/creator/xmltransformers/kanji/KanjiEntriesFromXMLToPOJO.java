package com.github.dianamaftei.creator.xmltransformers.kanji;

import com.github.dianamaftei.appscommon.model.Kanji;
import com.github.dianamaftei.appscommon.model.KanjiCategories;
import com.github.dianamaftei.appscommon.model.KanjiReferences;
import com.github.dianamaftei.creator.jaxbgeneratedmodels.DictionaryEntry;
import com.github.dianamaftei.creator.jaxbgeneratedmodels.kanjidic.Character;
import com.github.dianamaftei.creator.jaxbgeneratedmodels.kanjidic.Kanjidic2;
import com.github.dianamaftei.creator.xmltransformers.XMLEntryToPOJO;
import com.github.dianamaftei.creator.xmltransformers.kanji.kanjicomponents.KanjiComponent;
import com.github.dianamaftei.creator.xmltransformers.kanji.kanjicomponents.KanjiComponentType;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KanjiEntriesFromXMLToPOJO extends XMLEntryToPOJO {

  private static final Logger LOGGER = LoggerFactory.getLogger(KanjiEntriesFromXMLToPOJO.class);

  private final KanjiRepository kanjiRepository;
  private Reader reader;
  private Map<String, String> kanjiJLPTLevelMap;
  private Map<String, RtkKanji> rtkKanjiMap;
  private static final String TAB = "\t";
  private final String rtkFilePath;
  private List<KanjiComponent> kanjiComponentsEnrichers;

  @Autowired
  public KanjiEntriesFromXMLToPOJO(final KanjiRepository kanjiRepository,
      @Value("${path.dictionary.kanji}") final String kanjiDictionaryPath,
      @Value("${path.rtk}") final String rtkFilePath,
      final List<KanjiComponent> kanjiComponentsEnrichers) {
    this.kanjiRepository = kanjiRepository;
    this.inputFile = kanjiDictionaryPath;
    this.rtkFilePath = rtkFilePath;
    this.kanjiComponentsEnrichers = kanjiComponentsEnrichers;
  }

  @Override
  protected void persist(final List<? extends DictionaryEntry> characters) {
    kanjiRepository.saveAll(
        ((List<Character>) characters).parallelStream()
            .filter(Objects::nonNull)
            .map(this::buildKanjiEntry)
            .collect(Collectors.toList())
    );
  }

  @Override
  protected List<Character> getEntries(final Object dictionaryFile) {
    return ((Kanjidic2) dictionaryFile).getCharacter();
  }

  @Override
  protected Class getClassForJaxb() {
    return Kanjidic2.class;
  }

  private Map<String, RtkKanji> loadRtkKanji() {
    final Map<String, RtkKanji> rtkKanjis = new HashMap<>();

    try (final BufferedReader bufferedReader = new BufferedReader(getReader(rtkFilePath))) {
      bufferedReader.lines()
          .forEach(line -> {
            final RtkKanji rtkKanji = new RtkKanji();
            final String[] splitLine = line.split(TAB);
            rtkKanji.setKanji(splitLine[0]);
            if (splitLine.length > 1) {
              rtkKanji.setComponents(splitLine[1]);
            }
            if (splitLine.length > 2) {
              rtkKanji.setKeyword(splitLine[2]);
              rtkKanji.setStory1(splitLine[3]);
              rtkKanji.setStory2(splitLine[4]);
            }

            rtkKanjis.put(rtkKanji.getKanji(), rtkKanji);
          });

    } catch (final IOException e) {
      LOGGER.error("Could not get rtk info from file", e);
    }

    return rtkKanjis;
  }

  Kanji buildKanjiEntry(final Character character) {
    final Kanji kanjiEntry = new Kanji();
    kanjiEntry.setReferences(new KanjiReferences());

    final List<Object> kanjiComponents = character.getLiteralAndCodepointAndRadical();

    for (final Object component : kanjiComponents) {
      final KanjiComponentType kanjiComponentType = KanjiComponentType
          .valueOf(component.getClass().getSimpleName().toUpperCase());

      kanjiComponentsEnrichers.stream()
          .filter(enricher -> enricher.applies(kanjiComponentType))
          .forEach(enricher -> enricher.enrich(kanjiEntry, component));
    }

    final String kanji = kanjiEntry.getCharacter();

    enrichKanjiEntryWithJLPTInfo(kanji, kanjiEntry);
    enrichKanjiEntryWithRtkInfo(kanji, kanjiEntry);

    return kanjiEntry;
  }

  private void enrichKanjiEntryWithJLPTInfo(final String kanji, final Kanji kanjiEntry) {
    if (kanjiJLPTLevelMap == null) {
      kanjiJLPTLevelMap = KanjiCategories.getKanjiByCategory();
    }

    if (kanjiJLPTLevelMap.get(kanji) != null) {
      kanjiEntry.getReferences().setJlptNewLevel(kanjiJLPTLevelMap.get(kanji));
    }
  }

  private void enrichKanjiEntryWithRtkInfo(final String kanji, final Kanji kanjiEntry) {
    if (rtkKanjiMap == null) {
      rtkKanjiMap = loadRtkKanji();
    }

    final RtkKanji rtkKanji = rtkKanjiMap.get(kanji);
    if (rtkKanji != null) {
      kanjiEntry.setKeyword(rtkKanji.getKeyword());
      kanjiEntry.setComponents(rtkKanji.getComponents());
      kanjiEntry.setStory1(rtkKanji.getStory1());
      kanjiEntry.setStory2(rtkKanji.getStory2());
    }
  }

  void setKanjiJLPTLevelMap(final Map<String, String> kanjiJLPTLevelMap) {
    this.kanjiJLPTLevelMap = kanjiJLPTLevelMap;
  }

  void setRtkKanjiMap(final Map<String, RtkKanji> rtkKanjiMap) {
    this.rtkKanjiMap = rtkKanjiMap;
  }

  private Reader getReader(final String filePath) throws FileNotFoundException {
    if (reader != null) {
      return reader;
    }

    return new FileReader(filePath);
  }

  public void setReader(final Reader reader) {
    this.reader = reader;
  }

  public List<KanjiComponent> getKanjiComponentsEnrichers() {
    return kanjiComponentsEnrichers;
  }

  public void setKanjiComponentsEnrichers(
      final List<KanjiComponent> kanjiComponentsEnrichers) {
    this.kanjiComponentsEnrichers = kanjiComponentsEnrichers;
  }
}
