package com.yomimashou.creator.xmltransformers.word;

import com.yomimashou.appscommon.model.Word;
import com.yomimashou.appscommon.model.WordMeaning;
import com.yomimashou.creator.jaxbgeneratedmodels.DictionaryEntry;
import com.yomimashou.creator.jaxbgeneratedmodels.jmdict.Entry;
import com.yomimashou.creator.jaxbgeneratedmodels.jmdict.Gloss;
import com.yomimashou.creator.jaxbgeneratedmodels.jmdict.JMdict;
import com.yomimashou.creator.jaxbgeneratedmodels.jmdict.KEle;
import com.yomimashou.creator.jaxbgeneratedmodels.jmdict.REle;
import com.yomimashou.creator.jaxbgeneratedmodels.jmdict.Sense;
import com.yomimashou.creator.xmltransformers.XMLEntryToPOJO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WordEntriesFromXMLToPOJO extends XMLEntryToPOJO {

  private static final int HIGHEST_PRIORITY = 1;
  private static final int MODERATE_PRIORITY = 2;
  private static final int LOW_PRIORITY = 3;

  private final WordRepository wordRepository;

  @Autowired
  public WordEntriesFromXMLToPOJO(final WordRepository wordRepository,
      @Value("${path.dictionary.word}") final String wordDictionaryPath,
      @Value("${path.word.entries}") final String wordEntriesPath) {
    this.wordRepository = wordRepository;
    this.inputFile = wordDictionaryPath;
    this.outputFile = wordEntriesPath;
  }

  @Override
  protected List<Entry> getEntries(final Object dictionaryFile) {
    return ((JMdict) dictionaryFile).getEntry();
  }

  @Override
  protected Class getClassForJaxb() {
    return JMdict.class;
  }

  @Override
  protected void persist(final List<? extends DictionaryEntry> entries) {
    final List<Word> words = ((List<Entry>) entries).parallelStream()
        .filter(Objects::nonNull)
        .map(this::buildWordEntry)
        .collect(Collectors.toList());

    final Set<String> wordKanjiAndReadings = words.parallelStream().map(entry -> Arrays.asList(
        entry.getKanjiElements(),
        entry.getReadingElements())
    ).flatMap(List::stream)
        .flatMap(Set::stream)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());

    wordRepository.saveAll(words);
    writeToFile(wordKanjiAndReadings, outputFile);
  }

  Word buildWordEntry(final Entry entry) {
    final Word word = new Word();

    word.setKanjiElements(
        entry.getKEle().stream().map(KEle::getKeb).collect(Collectors.toSet()));
    word.setReadingElements(
        entry.getREle().stream().map(REle::getReb).collect(Collectors.toSet()));
    word.setMeanings(buildMeaningsList(entry));
    word.setPriority(extractHighestPriority(entry));

    return word;
  }

  private Integer extractHighestPriority(final Entry entry) {
    final Set<Integer> priorities = new HashSet<>();
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

  private int getPriorityNumber(final String priority) {
    final List<String> highestPriorities = Arrays
        .asList("news1", "ichi1", "spec1", "spec2", "gai1");

    if (highestPriorities.contains(priority)) {
      return HIGHEST_PRIORITY;
    }
    return MODERATE_PRIORITY;
  }

  private List<WordMeaning> buildMeaningsList(final Entry entry) {
    final List<WordMeaning> meanings = new ArrayList<>();

    for (final Sense sense : entry.getSense()) {
      final WordMeaning meaning = new WordMeaning();
      meaning.setPartOfSpeech(String.join("|", sense.getPos().stream()
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
