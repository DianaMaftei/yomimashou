package com.github.dianamaftei.yomimashou.text.dictionary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Deinflector {

    private static final Object lock = new Object();
    private static final Logger LOGGER = LoggerFactory.getLogger(Deinflector.class);
    private static String filePath;
    private static Map<Integer, List<DeinflectRule>> rulesGroupedByRuleLength;
    private static volatile Deinflector instance;

    private Deinflector() {
    }

    public static Deinflector getInstance(String path) {
        filePath = path;

        Deinflector deinflector = instance;
        if (deinflector == null) {
            synchronized (lock) {
                deinflector = instance;
                if (deinflector == null) {
                    deinflector = new Deinflector();
                    addRules();
                    instance = deinflector;
                }
            }
        }
        return deinflector;
    }

    private static void addRules() {
        List<DeinflectRule> ruleList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(filePath + File.separator + "dictionaries" + File.separator + "deinflect.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] ruleComponents = line.split("\t");
                String from = ruleComponents[0];
                String to = ruleComponents.length > 1 ? ruleComponents[1] : "";
                ruleList.add(new DeinflectRule(from, to));
            }
        } catch (IOException e) {
            LOGGER.error("Could not add deinflection rules from file", e);
        }

        rulesGroupedByRuleLength = ruleList.stream().collect(Collectors.groupingBy(item -> item.from.length()));
    }

    public Set<String> getDeinflectedWords(String textFragmentWithInflectedWords) {
        Set<String> deinflectedWords = new HashSet<>();

        while (textFragmentWithInflectedWords.length() > 0) {
            String textFragment = textFragmentWithInflectedWords;
            rulesGroupedByRuleLength.forEach((ruleLength, listOfRules) -> {
                listOfRules.forEach(rule -> {
                    getDeinflectedWordIfEndingMatchesRule(rule, ruleLength, textFragment).ifPresent(deinflectedWords::add);
                });
            });

            textFragmentWithInflectedWords = textFragmentWithInflectedWords.substring(0, textFragmentWithInflectedWords.length() - 1);
        }

        return deinflectedWords;
    }

    private Optional<String> getDeinflectedWordIfEndingMatchesRule(DeinflectRule rule, int ruleLength, String textFragment) {
        if (ruleLength <= textFragment.length()) {
            String textFragmentEnding = textFragment.substring(textFragment.length() - ruleLength);
            if (textFragmentEnding.equals(rule.from)) {
                String newWord = textFragment.substring(0, textFragment.length() - rule.from.length()) + rule.to;
                if (newWord.length() > 1) {
                    return Optional.of(newWord);
                }
            }
        }
        return Optional.empty();
    }

    private static class DeinflectRule {
        private String from;
        private String to;

        private DeinflectRule(String from, String to) {
            this.from = from;
            this.to = to;
        }
    }
}


