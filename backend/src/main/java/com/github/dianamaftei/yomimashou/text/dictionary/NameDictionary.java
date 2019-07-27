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
public class NameDictionary {

  private static final Logger LOGGER = LoggerFactory.getLogger(NameDictionary.class);

  @Value("${file.path}")
  private String filePath;

  private Reader reader;
  private BloomFilter<String> bloomFilterForNames;

  public boolean isNameInDictionary(String item) {
    if (bloomFilterForNames == null) {
      buildNamesDictionary(filePath);
    }

    return bloomFilterForNames.contains(item);
  }

  private void buildNamesDictionary(String filePath) {
    String file = getFile(filePath);
    Set<String> names = Arrays.stream(file.split("\\|")).parallel().collect(Collectors.toSet());

    double falsePositiveProbability = 0.1;
    int expectedNumberOfElements = names.size();
    bloomFilterForNames = new BloomFilter<>(falsePositiveProbability, expectedNumberOfElements);

    names.forEach(bloomFilterForNames::add);
  }

  private String getFile(String fileName) {
    StringBuilder result = new StringBuilder();

    try (BufferedReader reader = new BufferedReader(getReader(fileName))) {
      String line;
      while ((line = reader.readLine()) != null) {
        result.append(line);
      }
    } catch (IOException e) {
      LOGGER.error("Could not getByEqualsKanji file " + fileName, e);
    }

    return result.toString();
  }

  private Reader getReader(String filePath) throws FileNotFoundException {
    if (reader != null) {
      return reader;
    }

    return new FileReader(
        filePath + File.separator + "dictionaries" + File.separator + "nameEntries.txt");
  }

  public void setReader(Reader reader) {
    this.reader = reader;
  }
}
