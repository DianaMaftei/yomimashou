package com.github.dianamaftei.yomimashou.text;

import com.github.dianamaftei.yomimashou.text.dictionary.Deinflector;
import com.github.dianamaftei.yomimashou.text.dictionary.NameDictionary;
import com.github.dianamaftei.yomimashou.text.dictionary.WordDictionary;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TextParserService {

  private Deinflector deinflector;
  private WordDictionary wordDictionary;
  private NameDictionary nameDictionary;

  @Autowired
  public TextParserService(Deinflector deinflector, WordDictionary wordDictionary,
      NameDictionary nameDictionary) {
    this.deinflector = deinflector;
    this.wordDictionary = wordDictionary;
    this.nameDictionary = nameDictionary;
  }

  Set<String> parseWords(String text) {
    int maxLengthCommonNounsCanHave = 16;

    BiFunction<String, String, Set<String>> getWords = (String textFragment, String word) -> {

      Set<String> deinflectedWords = deinflector.getDeinflectedWords(textFragment);
      Set<String> validWords = deinflectedWords.stream().filter(wordDictionary::isWordInDictionary)
          .collect(Collectors.toSet());

      if (wordDictionary.isWordInDictionary(word)) {
        validWords.add(word);
      }

      return validWords;
    };

    return parse(text, maxLengthCommonNounsCanHave, getWords);
  }

  Set<String> parseNames(String text) {
    int maxLengthProperNounCanHave = 32;

    BiFunction<String, String, Set<String>> getNames = (String unused, String name) -> {
      Set<String> names = new HashSet<>();

      if (nameDictionary.isNameInDictionary(name)) {
        names.add(name);
      }

      return names;
    };

    return parse(text, maxLengthProperNounCanHave, getNames);
  }

  private Set<String> parse(String text, int maxLengthOfTextToParseAtATime,
      BiFunction<String, String, Set<String>> getWordsFromDictionary) {
    Set<String> validWords = new HashSet<>();
    int startOfTextFragment = 0;
    int endOfTextFragment = text.length() < maxLengthOfTextToParseAtATime ? text.length()
        : maxLengthOfTextToParseAtATime;

    String textFragment;

    while (startOfTextFragment < text.length()) {
      textFragment = getValidTextFragment(text.substring(startOfTextFragment, endOfTextFragment));

      int subTo = 1;
      String word;
      while (subTo <= textFragment.length()) {
        word = textFragment.substring(0, subTo);
        Set<String> words = getWordsFromDictionary.apply(textFragment, word);
        if (!words.isEmpty()) {
          validWords.addAll(words);
        }
        subTo++;
      }
      startOfTextFragment++;
      if (endOfTextFragment < text.length()) {
        endOfTextFragment++;
      }
    }

    return validWords;
  }

  private String getValidTextFragment(String text) {
    EndingCharacter[] endingCharacters = EndingCharacter.values();
    int indexOfEndingCharacter = -1;
    for (EndingCharacter endingCharacter : endingCharacters) {
      int index = text.indexOf(endingCharacter.toString());
      if (index != -1 && (indexOfEndingCharacter == -1 || index < indexOfEndingCharacter)) {
        indexOfEndingCharacter = index;
      }
    }
    if (indexOfEndingCharacter != -1) {
      text = text.substring(0, indexOfEndingCharacter);
    }
    return text;
  }
}
