package com.github.dianamaftei.yomimashou.Dictionary;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class NameDictionary {
    private static final Object lock = new Object();
    private static volatile NameDictionary instance;

    private static BloomFilter<String> bloomFilterForNames;

    private NameDictionary() {
    }

    public static NameDictionary getInstance() {
        NameDictionary wordDictionary = instance;
        if (wordDictionary == null) {
            synchronized (lock) {
                wordDictionary = instance;
                if (wordDictionary == null) {
                    wordDictionary = new NameDictionary();
                    insertNamesIntoDictionary();
                    instance = wordDictionary;
                }
            }
        }
        return wordDictionary;
    }

    private static void insertNamesIntoDictionary() {
        String file = getFile("dictionaries" + File.separator + "nameEntries.txt");
        Set<String> words = Arrays.stream(file.split("\\|")).parallel().collect(Collectors.toSet());

        double falsePositiveProbability = 0.1;
        int expectedNumberOfElements = words.size();
        bloomFilterForNames = new BloomFilter<String>(falsePositiveProbability, expectedNumberOfElements);

        words.forEach(bloomFilterForNames::add);
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

    public boolean isNameInDictionary(String item) {
        return bloomFilterForNames.contains(item);
    }
}
