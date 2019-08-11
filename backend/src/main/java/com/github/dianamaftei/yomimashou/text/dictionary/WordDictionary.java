package com.github.dianamaftei.yomimashou.text.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WordDictionary {

  private static final Logger LOGGER = LoggerFactory.getLogger(WordDictionary.class);

  @Value("${path.word.entries}")
  private String filePath;

  private Reader reader;
  private Set<String> words;

  public boolean isWordInDictionary(String word) {
    if (words == null) {
      words = buildWordDictionary(filePath);
    }
    return words.contains(word);
  }

  private Set<String> buildWordDictionary(String filePath) {
    String file = getFileContent(filePath);
    return Arrays.stream(file.split("\\|")).parallel().collect(Collectors.toSet());
  }

  private String getFileContent(String fileName) {
    StringBuilder result = new StringBuilder();

    try (BufferedReader bufferedReader = new BufferedReader(getReader(fileName))) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        result.append(line);
      }
    } catch (IOException e) {
      LOGGER.error("Could not read from file: " + fileName, e);
    }

    return result.toString();
  }

  private Reader getReader(String filePath) throws FileNotFoundException {
    if (reader != null) {
      return reader;
    }

    return new FileReader(filePath);
  }

  public void setReader(Reader reader) {
    this.reader = reader;
  }
}
