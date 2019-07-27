package com.github.dianamaftei.yomimashou.text.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Deinflector {

  private static final Logger LOGGER = LoggerFactory.getLogger(Deinflector.class);

  @Value("${file.path}")
  private String filePath;

  private Reader reader;
  private Map<Integer, List<DeinflectRule>> rulesGroupedByRuleLength;

  private static final String TAB = "\t";

  public Set<String> getDeinflectedWords(String textFragmentWithInflectedWords) {
    Set<String> deinflectedWords = new HashSet<>();

    if (rulesGroupedByRuleLength == null) {
      rulesGroupedByRuleLength = buildRuleList(filePath);
    }

    while (textFragmentWithInflectedWords.length() > 0) {
      String textFragment = textFragmentWithInflectedWords;
      rulesGroupedByRuleLength.forEach((ruleLength, listOfRules) -> listOfRules.forEach(
          rule -> getDeinflectedWordIfEndingMatchesRule(rule, ruleLength, textFragment)
              .ifPresent(deinflectedWords::add)));

      textFragmentWithInflectedWords = textFragmentWithInflectedWords
          .substring(0, textFragmentWithInflectedWords.length() - 1);
    }

    return deinflectedWords;
  }

  private Map<Integer, List<DeinflectRule>> buildRuleList(String filePath) {
    List<DeinflectRule> ruleList = new ArrayList<>();

    try (BufferedReader bufferedReader = new BufferedReader(getReader(filePath))) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        String[] ruleComponents = line.split(TAB);
        String from = ruleComponents[0];
        String to = ruleComponents.length > 1 ? ruleComponents[1] : "";
        ruleList.add(new DeinflectRule(from, to));
      }
    } catch (IOException e) {
      LOGGER.error("Could not add deinflection rules from file", e);
    }

    return ruleList.stream().collect(Collectors.groupingBy(item -> item.from.length()));
  }

  private Reader getReader(String filePath) throws FileNotFoundException {
    if (reader != null) {
      return reader;
    }

    return new FileReader(
        filePath + File.separator + "dictionaries" + File.separator + "deinflect.txt");
  }

  private Optional<String> getDeinflectedWordIfEndingMatchesRule(DeinflectRule rule, int ruleLength,
      String textFragment) {
    if (ruleLength <= textFragment.length()) {
      String textFragmentEnding = textFragment.substring(textFragment.length() - ruleLength);
      if (textFragmentEnding.equals(rule.from)) {
        String newWord =
            textFragment.substring(0, textFragment.length() - rule.from.length()) + rule.to;
        if (newWord.length() > 1) {
          return Optional.of(newWord);
        }
      }
    }
    return Optional.empty();
  }

  public void setReader(Reader reader) {
    this.reader = reader;
  }

  private static class DeinflectRule {

    private final String from;
    private String to;

    private DeinflectRule(String from, String to) {
      this.from = from;
      this.to = to;
    }

    @Override
    public String toString() {
      return "DeinflectRule{" +
          "from='" + from + '\'' +
          ", to='" + to + '\'' +
          '}';
    }
  }
}


