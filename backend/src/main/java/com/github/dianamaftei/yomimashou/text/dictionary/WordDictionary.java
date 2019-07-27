package com.github.dianamaftei.yomimashou.text.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WordDictionary {

  private static final Logger LOGGER = LoggerFactory.getLogger(WordDictionary.class);

  @Value("${file.path}")
  private String filePath;

  private Reader reader;
  private Map<String, String> wordsMap;

  public boolean isWordInDictionary(String item) {
    if (wordsMap == null) {
      buildWordDictionary(filePath);
    }
    return wordsMap.containsKey(item);
  }

  private void buildWordDictionary(String filePath) {
    wordsMap = new HashMap<>();
    String file = getFile(filePath);
    Set<String> words = Arrays.stream(file.split("\\|")).parallel().collect(Collectors.toSet());

    words.forEach(item -> wordsMap.put(item, null));
  }

  private String getFile(String fileName) {
    StringBuilder result = new StringBuilder();

    try (BufferedReader reader = new BufferedReader(getReader(fileName))) {
      String line;
      while ((line = reader.readLine()) != null) {
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

    return new FileReader(
        filePath + File.separator + "dictionaries" + File.separator + "wordEntries.txt");
  }

  public void setReader(Reader reader) {
    this.reader = reader;
  }
}
