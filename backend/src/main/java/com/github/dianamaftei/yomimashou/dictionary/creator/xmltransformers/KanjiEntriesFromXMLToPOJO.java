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
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class KanjiEntriesFromXMLToPOJO extends XMLEntryToPOJO {

  private static final Logger LOGGER = LoggerFactory.getLogger(KanjiEntriesFromXMLToPOJO.class);

  private final KanjiRepository kanjiRepository;
  private Map<String, String> kanjiJLPTLevelMap;
  private Map<String, RtkKanji> rtkKanjiMap;

  @Autowired
  public KanjiEntriesFromXMLToPOJO(KanjiRepository kanjiRepository) {
    this.kanjiRepository = kanjiRepository;
    this.dictionarySource = "http://ftp.monash.edu/pub/nihongo/kanjidic2.xml.gz";
    this.fileName = "kanjiEntries.txt";
  }

  @Override
  void saveToDb(List<? extends DictionaryEntry> characters) {
    ((List<Character>) characters).parallelStream().forEach(character -> {
      Kanji kanji = buildKanjiEntry(character);
      kanjiRepository.save(kanji);
    });
  }

  @Override
  List<Character> getEntries(Object dictionaryFile) {
    return ((Kanjidic2) dictionaryFile).getCharacter();
  }

  @Override
  Class getClassForJaxb() {
    return Kanjidic2.class;
  }

  @Override
  void saveToFile(List<? extends DictionaryEntry> characters) {
    try {
      Set<String> kanjiEntries = ((List<Character>) characters).parallelStream()
          .filter(Objects::nonNull)
          .map(this::buildKanjiEntry)
          .map(Kanji::getCharacter)
          .collect(Collectors.toSet());

      writeToFile(kanjiEntries, fileName);
    } catch (Exception e) {
      LOGGER.error("Could not save to file: " + fileName, e);
    }
  }

  private Map<String, String> getKanjiAndCorrespondingJLPTLevel() {
    ClassPathResource resource = new ClassPathResource(
        "dictionaries" + File.separator + "kanjiByJLPTLevel.csv");

    //TODO use Kanji Categories
    Map<String, String> kanjiJLPTLevel = new HashMap<>();
    String line;
    String kanji;
    String level;
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
      while ((line = reader.readLine()) != null) {
        kanji = line.split("\t")[0];
        level = line.split("\t")[1];
        kanjiJLPTLevel.put(kanji, level);
      }
    } catch (IOException e) {
      LOGGER.error("Could not getByEqualsKanji kanji and JLPT levels", e);
    }

    return kanjiJLPTLevel;
  }

  private Map<String, RtkKanji> getRtkKanji() {
    ClassPathResource resource = new ClassPathResource("dictionaries" + File.separator + "rtk.csv");

    Map<String, RtkKanji> rtkKanjis = new HashMap<>();
    String line;
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
      while ((line = reader.readLine()) != null) {
        RtkKanji rtkKanji = new RtkKanji();
        rtkKanji.setKanji(line.split("\t")[0]);
        if (line.split("\t").length > 1) {
          rtkKanji.setComponents(line.split("\t")[1]);
        }
        if (line.split("\t").length > 2) {
          rtkKanji.setKeyword(line.split("\t")[2]);
          rtkKanji.setStory1(line.split("\t")[3]);
          rtkKanji.setStory2(line.split("\t")[4]);
        }

        rtkKanjis.put(rtkKanji.getKanji(), rtkKanji);
      }
    } catch (IOException e) {
      LOGGER.error("Could not getByEqualsKanji rtk info from file", e);
    }

    return rtkKanjis;
  }

  Kanji buildKanjiEntry(Character character) {
    Kanji kanjiEntry = new Kanji();
    kanjiEntry.setReferences(new KanjiReferences());

    List<Object> kanjiComponents = character.getLiteralAndCodepointAndRadical();

    String kanji = "";

    for (Object component : kanjiComponents) {
      KanjiComponentClass kanjiComponentClass = KanjiComponentClass
          .valueOf(component.getClass().getSimpleName().toUpperCase());

      switch (kanjiComponentClass) {
        case STRING:
          kanji = (String) component;
          kanjiEntry.setCharacter(kanji);
          break;
        case CODEPOINT:
          List<String> codepoints = new ArrayList<>();
          if (!CollectionUtils.isEmpty(((Codepoint) component).getCpValue())) {
            for (CpValue cpValue : ((Codepoint) component).getCpValue()) {
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
          Misc misc = (Misc) component;
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
          QueryCode queryCode = (QueryCode) component;
          for (QCode qCode : queryCode.getQCode()) {
            if ("skip".equals(qCode.getQcType())) {
              kanjiEntry.setSkipCode(qCode.getContent());
            }
          }
          break;
        case READINGMEANING:
          addReadingsAndMeaningsToKanji(kanjiEntry, (ReadingMeaning) component);
          break;
      }
    }

    if (kanjiJLPTLevelMap == null) {
      kanjiJLPTLevelMap = getKanjiAndCorrespondingJLPTLevel();
    }

    if (kanjiJLPTLevelMap.get(kanji) != null) {
      kanjiEntry.getReferences().setJlptNewLevel(kanjiJLPTLevelMap.get(kanji));
    }

    if (rtkKanjiMap == null) {
      rtkKanjiMap = getRtkKanji();
    }

    RtkKanji rtkKanji = rtkKanjiMap.get(kanji);
    if (rtkKanji != null) {
      kanjiEntry.setKeyword(rtkKanji.getKeyword());
      kanjiEntry.setComponents(rtkKanji.getComponents());
      kanjiEntry.setStory1(rtkKanji.getStory1());
      kanjiEntry.setStory2(rtkKanji.getStory2());
    }

    return kanjiEntry;
  }

  private void addReadingsAndMeaningsToKanji(Kanji kanji, ReadingMeaning component) {
    List<String> onReadings = new ArrayList<>();
    List<String> kunReadings = new ArrayList<>();
    List<String> meanings = new ArrayList<>();

    for (Rmgroup rmgroup : component.getRmgroup()) {

      for (Reading reading : rmgroup.getReading()) {
        if ("ja_on".equals(reading.getRType())) {
          onReadings.add(reading.getContent());
          continue;
        }

        if ("ja_kun".equals(reading.getRType())) {
          kunReadings.add(reading.getContent());
        }
      }

      for (Meaning meaning : rmgroup.getMeaning()) {
        if (meaning.getMLang() == null) {
          meanings.add(meaning.getContent());
        }
      }
    }

    kanji.setOnReading(String.join("|", onReadings));
    kanji.setKunReading(String.join("|", kunReadings));
    kanji.setMeaning(String.join("|", meanings));
  }

  private void setAllKanjiDictionaryReferences(KanjiReferences references, DicNumber component) {
    List<DicRef> dicRefs = component.getDicRef();

    for (DicRef dicRef : dicRefs) {
      try {
        KanjiReferences.class
            .getMethod("set" + convertSnakeCaseToPascalCase(dicRef.getDrType()), String.class)
            .invoke(references, dicRef.getContent());
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        LOGGER.error("Could not set field: " + dicRef.getDrType(), e);
      }
    }
  }

  private String convertSnakeCaseToPascalCase(String snakeCaseText) {
    return StringUtils.remove(WordUtils.capitalizeFully(snakeCaseText, '_'), "_");
  }

  private enum KanjiComponentClass {
    STRING, CODEPOINT, RADICAL, MISC, DICNUMBER, QUERYCODE, READINGMEANING
  }

  void setKanjiJLPTLevelMap(Map<String, String> kanjiJLPTLevelMap) {
    this.kanjiJLPTLevelMap = kanjiJLPTLevelMap;
  }

  void setRtkKanjiMap(Map<String, RtkKanji> rtkKanjiMap) {
    this.rtkKanjiMap = rtkKanjiMap;
  }
}
