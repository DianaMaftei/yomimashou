package com.github.dianamaftei.yomimashou.dictionary.creator;

import com.github.dianamaftei.yomimashou.dictionary.example.ExampleSentence;
import com.github.dianamaftei.yomimashou.dictionary.example.ExampleSentenceRepository;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExampleSentencesCSVtoPOJO {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExampleSentencesCSVtoPOJO.class);

  private final ExampleSentenceRepository exampleSentenceRepository;
  private static final String TAB = "\t";
  private String filePath;
  private Reader reader;

  @Autowired
  public ExampleSentencesCSVtoPOJO(final ExampleSentenceRepository exampleSentenceRepository,
      @Value("${path.sentences}") final String filePath) {
    this.exampleSentenceRepository = exampleSentenceRepository;
    this.filePath = filePath;
  }

  public void saveSentencesFromFileToDB() {
    try (final BufferedReader sentenceReader = new BufferedReader(getReader(filePath))) {
      saveSentencesToDB(sentenceReader.lines().collect(Collectors.toList()));
    } catch (final IOException e) {
      LOGGER.error("Could not read sentences from file: " + filePath + ", and save to db ", e);
    }
  }

  void saveSentencesToDB(final List<String> sentences) {
    sentences.parallelStream()
        .map(this::parseExampleSentenceFromLine)
        .forEach(exampleSentenceRepository::save);
  }

  private ExampleSentence parseExampleSentenceFromLine(final String line) {
    //Structure:  Sentence id [tab] Language [tab] Sentence [tab] Translation(s) [tab] Breakdown
    final String[] columns = line.split(TAB);

    final String sentence = columns[2];
    final String meaning = columns.length > 3 ? columns[3] : null;
    final String breakdown = columns.length > 4 ? columns[4] : null;

    final ExampleSentence exampleSentence = new ExampleSentence();
    exampleSentence.setSentence(sentence);
    exampleSentence.setMeaning(meaning);
    exampleSentence.setTextBreakdown(breakdown);

    return exampleSentence;
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
