package com.github.dianamaftei.yomimashou.dictionary.creator.xmltransformers;

import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.DictionaryEntry;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.Entry;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.Gloss;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.JMdict;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.KEle;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.REle;
import com.github.dianamaftei.yomimashou.dictionary.creator.jaxbgeneratedmodels.jmdict.Sense;
import com.github.dianamaftei.yomimashou.dictionary.word.Word;
import com.github.dianamaftei.yomimashou.dictionary.word.WordMeaning;
import com.github.dianamaftei.yomimashou.dictionary.word.WordRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WordEntriesFromXMLToPOJO extends XMLEntryToPOJO {

  private static final Logger LOGGER = LoggerFactory.getLogger(WordEntriesFromXMLToPOJO.class);

  private static final int HIGHEST_PRIORITY = 1;
  private static final int MODERATE_PRIORITY = 2;
  private static final int LOW_PRIORITY = 3;

  private final WordRepository wordRepository;
  private Map<String, String> partsOfSpeech;

  @Autowired
  public WordEntriesFromXMLToPOJO(WordRepository wordRepository) {
    this.wordRepository = wordRepository;
    this.dictionarySource = "http://ftp.monash.edu/pub/nihongo/JMdict_e.gz";
    this.fileName = "wordEntries.txt";
  }

  @Override
  List<Entry> getEntries(Object dictionaryFile) {
    return ((JMdict) dictionaryFile).getEntry();
  }

  @Override
  Class getClassForJaxb() {
    return JMdict.class;
  }

  @Override
  void saveToFile(List<? extends DictionaryEntry> entries) {
    try {
      Set<String> wordEntries = ((List<Entry>) entries).parallelStream()
          .filter(Objects::nonNull)
          .map(entry -> Arrays.asList(
              entry.getKEle().stream().map(KEle::getKeb).collect(Collectors.toList()),
              entry.getREle().stream().map(REle::getReb).collect(Collectors.toList()))
          ).flatMap(List::stream)
          .flatMap(List::stream)
          .collect(Collectors.toSet());

      writeToFile(wordEntries, fileName);
    } catch (Exception e) {
      LOGGER.error("Could not save to file: " + fileName, e);
    }
  }

  @Override
  void saveToDb(List<? extends DictionaryEntry> entries) {
    ((List<Entry>) entries).parallelStream()
        .map(this::buildWordEntry)
        .collect(Collectors.toList())
        .forEach(wordRepository::save);
  }

  Word buildWordEntry(Entry entry) {
    Word word = new Word();

    word.setKanjiElements(
        entry.getKEle().stream().map(KEle::getKeb).collect(Collectors.toSet()));
    word.setReadingElements(
        entry.getREle().stream().map(REle::getReb).collect(Collectors.toSet()));
    word.setMeanings(buildMeaningsList(entry));
    word.setPriority(extractHighestPriority(entry));

    return word;
  }

  private Integer extractHighestPriority(Entry entry) {
    Set<Integer> priorities = new HashSet<>();
    entry.getKEle().forEach(kEle -> {
      if (!kEle.getKePri().isEmpty()) {
        kEle.getKePri().forEach(priority -> priorities.add(getPriorityNumber(priority)));
      }
    });
    entry.getREle().forEach(rEle -> {
      if (!rEle.getRePri().isEmpty()) {
        rEle.getRePri().forEach(priority -> priorities.add(getPriorityNumber(priority)));
      }
    });

    if (priorities.isEmpty()) {
      return LOW_PRIORITY;
    }

    // 1 is the highest priority
    return Collections.min(priorities);
  }

  private int getPriorityNumber(String priority) {
    List<String> highestPriorities = Arrays.asList("news1", "ichi1", "spec1", "spec2", "gai1");

    if (highestPriorities.contains(priority)) {
      return HIGHEST_PRIORITY;
    }
    return MODERATE_PRIORITY;
  }

  private List<WordMeaning> buildMeaningsList(Entry entry) {
    List<WordMeaning> meanings = new ArrayList<>();

    if (this.partsOfSpeech == null) {
      this.partsOfSpeech = PartsOfSpeech.buildListOfPartsOfSpeech();
    }

    for (Sense sense : entry.getSense()) {
      WordMeaning meaning = new WordMeaning();
      meaning.setPartOfSpeech(String.join("|", sense.getPos().stream()
          .map(partsOfSpeech::get)
          .filter(Objects::nonNull)
          .collect(Collectors.toList())));
      meaning.setFieldOfApplication(String.join("|", sense.getField()));
      meaning.setAntonym(String.join("|", sense.getAnt()));
      meaning.setGlosses(String.join("|", sense.getGloss().stream()
          .map(Gloss::getContent)
          .flatMap(Collection::stream)
          .map(Object::toString)
          .collect(Collectors.toList())));

      meanings.add(meaning);
    }

    return meanings;
  }



}
