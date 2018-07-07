package com.github.dianamaftei.yomimashou.Dictionary;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class WordDictionary {
    private static final Object lock = new Object();
    private static volatile WordDictionary instance;

    private static final Map<String, String> wordsMap = new HashMap();

    private WordDictionary() {
    }

    public static WordDictionary getInstance() {
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
        String file = getFile("dictionaries" + File.separator + "wordEntries.txt");
        Set<String> words = Arrays.stream(file.split("\\|")).parallel().collect(Collectors.toSet());

        words.forEach(item -> wordsMap.put(item, null));
    }

    private static String getFile(String fileName) {

        StringBuilder result = new StringBuilder();

        ClassPathResource resource = new ClassPathResource(fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
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
