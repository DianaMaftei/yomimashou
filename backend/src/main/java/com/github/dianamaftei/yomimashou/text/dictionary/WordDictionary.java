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

public class WordDictionary {

  private static final Object lock = new Object();
  private static volatile WordDictionary instance;
  private static Reader reader;
  private static final Map<String, String> wordsMap = new HashMap();

  private WordDictionary() {
  }

  public static WordDictionary getInstance(String filePath) {

    WordDictionary wordDictionary = instance;
    if (wordDictionary == null) {
      synchronized (lock) {
        wordDictionary = instance;
        if (wordDictionary == null) {
          wordDictionary = new WordDictionary();
          buildWordDictionary(filePath);
          instance = wordDictionary;
        }
      }
    }
    return wordDictionary;
  }

  private static void buildWordDictionary(String filePath) {
    String file = getFile(filePath);
    Set<String> words = Arrays.stream(file.split("\\|")).parallel().collect(Collectors.toSet());

    words.forEach(item -> wordsMap.put(item, null));
  }

  private static String getFile(String fileName) {
    StringBuilder result = new StringBuilder();

    try (BufferedReader reader = new BufferedReader(getReader(fileName))) {
      String line;
      while ((line = reader.readLine()) != null) {
        result.append(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return result.toString();
  }


  public boolean isWordInDictionary(String item) {
    return wordsMap.containsKey(item);
  }

  static Reader getReader(String filePath) throws FileNotFoundException {
    if (reader != null) {
      return reader;
    }

    return new FileReader(
        filePath + File.separator + "dictionaries" + File.separator + "wordEntries.txt");
  }

  public static void setReader(Reader reader) {
    WordDictionary.reader = reader;
  }
}
