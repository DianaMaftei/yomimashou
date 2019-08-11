package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.DictionaryEntry;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.Character;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.Codepoint;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.CpValue;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.DicNumber;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.DicRef;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.Kanjidic2;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.Meaning;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.Misc;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.QCode;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.QueryCode;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.Radical;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.Reading;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.ReadingMeaning;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.kanjidic.Rmgroup;
import com.github.dianamaftei.yomimashou.dictionary.kanji.Kanji;
import com.github.dianamaftei.yomimashou.dictionary.kanji.KanjiReferences;
import com.github.dianamaftei.yomimashou.dictionary.kanji.KanjiRepository;
import com.github.dianamaftei.yomimashou.text.KanjiCategories;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class KanjiEntriesFromXMLToPOJO extends XMLEntryToPOJO {

  private static final Logger LOGGER = LoggerFactory.getLogger(KanjiEntriesFromXMLToPOJO.class);

  private final KanjiRepository kanjiRepository;
  private Reader reader;
  private Map<String, String> kanjiJLPTLevelMap;
  private Map<String, RtkKanji> rtkKanjiMap;
  private static final String ON_READING = "ja_on";
  private static final String KUN_READING = "ja_kun";
  private static final char UNDERSCORE = '_';
  private static final String SKIP = "skip";
  private static final String TAB = "\t";
  private static final String SETTER_METHOD_PREFIX = "set";
  private String rtkFilePath;

  @Autowired
  public KanjiEntriesFromXMLToPOJO(final KanjiRepository kanjiRepository,
      @Value("${path.dictionary.kanji}") final String kanjiDictionaryPath,
      @Value("${path.rtk}") final String rtkFilePath) {
    this.kanjiRepository = kanjiRepository;
    this.inputFile = kanjiDictionaryPath;
    this.rtkFilePath = rtkFilePath;
  }

  @Override
  void saveToDb(final List<? extends DictionaryEntry> characters) {
    ((List<Character>) characters).parallelStream()
        .filter(Objects::nonNull)
        .map(this::buildKanjiEntry)
        .forEach(kanjiRepository::save);
  }

  @Override
  List<Character> getEntries(final Object dictionaryFile) {
    return ((Kanjidic2) dictionaryFile).getCharacter();
  }

  @Override
  Class getClassForJaxb() {
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

    String kanji = "";

    for (final Object component : kanjiComponents) {
      final KanjiComponentClass kanjiComponentClass = KanjiComponentClass
          .valueOf(component.getClass().getSimpleName().toUpperCase());

      switch (kanjiComponentClass) {
        case STRING:
          kanji = (String) component;
          kanjiEntry.setCharacter(kanji);
          break;
        case CODEPOINT:
          final List<String> codepoints = new ArrayList<>();
          if (!CollectionUtils.isEmpty(((Codepoint) component).getCpValue())) {
            for (final CpValue cpValue : ((Codepoint) component).getCpValue()) {
              codepoints.add(cpValue.getContent() + ";" + cpValue.getCpType());
            }

            kanjiEntry.setCodepoint(String.join("|", codepoints));
          }

          break;
        case RADICAL:
          // only one main radical
          if (!CollectionUtils.isEmpty(((Radical) component).getRadValue())) {
            kanjiEntry.setRadical(((Radical) component).getRadValue().get(0).getContent());
          }
          break;
        case MISC:
          final Misc misc = (Misc) component;
          if (!StringUtils.isEmpty(misc.getGrade())) {
            kanjiEntry.setGrade(Integer.valueOf(misc.getGrade()));
          }
          //  the first is the accepted count, the rest are common miscounts
          if (!CollectionUtils.isEmpty(misc.getStrokeCount()) && !StringUtils
              .isEmpty(misc.getStrokeCount().get(0))) {
            kanjiEntry.setStrokeCount(Integer.valueOf(misc.getStrokeCount().get(0)));
          }

          if (!StringUtils.isEmpty(misc.getFreq())) {
            kanjiEntry.setFrequency(Integer.valueOf(misc.getFreq()));
          }

          kanjiEntry.getReferences().setJlptOldLevel(misc.getJlpt());
          break;
        case DICNUMBER:
          setAllKanjiDictionaryReferences(kanjiEntry.getReferences(), (DicNumber) component);
          break;
        case QUERYCODE:
          final QueryCode queryCode = (QueryCode) component;
          for (final QCode qCode : queryCode.getQCode()) {
            if (SKIP.equals(qCode.getQcType())) {
              kanjiEntry.setSkipCode(qCode.getContent());
            }
          }
          break;
        case READINGMEANING:
          enrichKanjiEntryWithReadingsAndMeanings(kanjiEntry, (ReadingMeaning) component);
          break;
      }
    }

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

  private void enrichKanjiEntryWithReadingsAndMeanings(final Kanji kanji,
      final ReadingMeaning component) {
    final List<String> onReadings = new ArrayList<>();
    final List<String> kunReadings = new ArrayList<>();
    final List<String> meanings = new ArrayList<>();

    for (final Rmgroup rmgroup : component.getRmgroup()) {

      for (final Reading reading : rmgroup.getReading()) {
        if (ON_READING.equals(reading.getRType())) {
          onReadings.add(reading.getContent());
          continue;
        }

        if (KUN_READING.equals(reading.getRType())) {
          kunReadings.add(reading.getContent());
        }
      }

      for (final Meaning meaning : rmgroup.getMeaning()) {
        if (meaning.getMLang() == null) {
          meanings.add(meaning.getContent());
        }
      }
    }

    kanji.setOnReading(String.join("|", onReadings));
    kanji.setKunReading(String.join("|", kunReadings));
    kanji.setMeaning(String.join("|", meanings));
  }

  private void setAllKanjiDictionaryReferences(final KanjiReferences references,
      final DicNumber component) {
    final List<DicRef> dicRefs = component.getDicRef();

    for (final DicRef dicRef : dicRefs) {
      try {
        KanjiReferences.class
            .getMethod(SETTER_METHOD_PREFIX + convertSnakeCaseToPascalCase(dicRef.getDrType()),
                String.class)
            .invoke(references, dicRef.getContent());
      } catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        LOGGER.error("Could not set field: " + dicRef.getDrType(), e);
      }
    }
  }

  private String convertSnakeCaseToPascalCase(final String snakeCaseText) {
    return StringUtils.remove(WordUtils.capitalizeFully(snakeCaseText, UNDERSCORE), UNDERSCORE);
  }

  private enum KanjiComponentClass {
    STRING, CODEPOINT, RADICAL, MISC, DICNUMBER, QUERYCODE, READINGMEANING
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
}
