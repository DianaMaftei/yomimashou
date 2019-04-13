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

public class NameDictionary {

  private static final Logger LOGGER = LoggerFactory.getLogger(NameDictionary.class);

  private static final Object lock = new Object();
  private static volatile NameDictionary instance;
  private static Reader reader;
  private static BloomFilter<String> bloomFilterForNames;

  private NameDictionary() {
  }

  public static NameDictionary getInstance(String filePath) {
    NameDictionary wordDictionary = instance;
    if (wordDictionary == null) {
      synchronized (lock) {
        wordDictionary = instance;
        if (wordDictionary == null) {
          wordDictionary = new NameDictionary();
          buildNamesDictionary(filePath);
          instance = wordDictionary;
        }
      }
    }
    return wordDictionary;
  }

  private static void buildNamesDictionary(String filePath) {
    String file = getFile(filePath);
    Set<String> names = Arrays.stream(file.split("\\|")).parallel().collect(Collectors.toSet());

    double falsePositiveProbability = 0.1;
    int expectedNumberOfElements = names.size();
    bloomFilterForNames = new BloomFilter<>(falsePositiveProbability, expectedNumberOfElements);

    names.forEach(bloomFilterForNames::add);
  }


  static String getFile(String fileName) {
    StringBuilder result = new StringBuilder();

    try (BufferedReader reader = new BufferedReader(getReader(fileName))) {
      String line;
      while ((line = reader.readLine()) != null) {
        result.append(line);
      }
    } catch (IOException e) {
      LOGGER.error("Could not get file " + fileName, e);
    }

    return result.toString();
  }

  public boolean isNameInDictionary(String item) {
    return bloomFilterForNames.contains(item);
  }

  static Reader getReader(String filePath) throws FileNotFoundException {
    if (reader != null) {
      return reader;
    }

    return new FileReader(
        filePath + File.separator + "dictionaries" + File.separator + "nameEntries.txt");
  }

  public static void setReader(Reader reader) {
    NameDictionary.reader = reader;
  }
}
