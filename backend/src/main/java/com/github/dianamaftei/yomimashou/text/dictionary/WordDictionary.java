package com.github.dianamaftei.yomimashou.text.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class WordDictionary {
    private static final Object lock = new Object();
    private static volatile WordDictionary instance;
    private static String filePath;

    private static final Map<String, String> wordsMap = new HashMap();

    private WordDictionary() {
    }

    public static WordDictionary getInstance(String path) {
        filePath = path;

        WordDictionary wordDictionary = instance;
        if (wordDictionary == null) {
            synchronized (lock) {
                wordDictionary = instance;
                if (wordDictionary == null) {
                    wordDictionary = new WordDictionary();
                    insertWordsIntoDictionary();
                    instance = wordDictionary;
                }
            }
        }
        return wordDictionary;
    }

    private static void insertWordsIntoDictionary() {
        String file = getFile(filePath + File.separator + "dictionaries" + File.separator + "wordEntries.txt");
        Set<String> words = Arrays.stream(file.split("\\|")).parallel().collect(Collectors.toSet());

        words.forEach(item -> wordsMap.put(item, null));
    }

    private static String getFile(String fileName) {

        StringBuilder result = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)))) {
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
}
