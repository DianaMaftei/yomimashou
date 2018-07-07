package com.github.dianamaftei.yomimashou.Dictionary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Deinflector {

    private static final Logger LOGGER = LoggerFactory.getLogger(Deinflector.class);

    private static Map<Integer, List<DeinflectRule>> rulesGroupedByRuleLength;

    static {
        addRules();
    }

    private static void addRules() {
        List<DeinflectRule> ruleList = new ArrayList<>();

        ClassPathResource resource = new ClassPathResource("dictionaries" + File.separator + "deinflect.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
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

    public static Set<String> getDeinflectedWords(String textFragmentWithInflectedWords) {
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

    private static Optional<String> getDeinflectedWordIfEndingMatchesRule(DeinflectRule rule, int ruleLength, String textFragment) {
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


