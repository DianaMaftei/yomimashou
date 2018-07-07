package com.github.dianamaftei.yomimashou.service;

import com.github.dianamaftei.yomimashou.Dictionary.Deinflector;
import com.github.dianamaftei.yomimashou.Dictionary.NameDictionary;
import com.github.dianamaftei.yomimashou.Dictionary.WordDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TextParserService {

    private final static WordDictionary wordDictionary = WordDictionary.getInstance();
    private final static NameDictionary nameDictionary = NameDictionary.getInstance();

    @Autowired
    private NameEntryService nameEntryService;

    public Set<String> parseWords(String text) {
        int maxLengthOfTextForParsingWords = 16;

        BiFunction<String, String, Set<String>> getWords = (String textFragment, String word) -> {
            Set<String> validWords = new HashSet<>();

            Set<String> deinflectedWords = Deinflector.getDeinflectedWords(textFragment);
            validWords.addAll(deinflectedWords.stream().filter(wordDictionary::isWordInDictionary).collect(Collectors.toSet()));

            if (wordDictionary.isWordInDictionary(word)) {
                validWords.add(word);
            }

            return validWords;
        };

        return parse(text, maxLengthOfTextForParsingWords, getWords);
    }

    public Set<String> parseNames(String text) {
        Set<String> validNames;
        int maxLengthOfTextForParsingNames = 32;

        BiFunction<String, String, Set<String>> getNames = (String unused, String name) -> {
            Set<String> names = new HashSet<>();

            if (nameDictionary.isNameInDictionary(name)) {
                names.add(name);
            }

            return names;
        };

        validNames = parse(text, maxLengthOfTextForParsingNames, getNames);
        return nameEntryService.get(validNames);
    }

    private Set<String> parse(String text, int maxLengthOfTextToParseAtATime, BiFunction<String, String, Set<String>> getWordsFromDictionary) {
        Set<String> validWords = new HashSet<>();
        int startOfTextFragment = 0;
        int endOfTextFragment = text.length() < maxLengthOfTextToParseAtATime ? text.length() : maxLengthOfTextToParseAtATime;

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
        List<String> endingCharacters = Stream.of("、", "。").collect(Collectors.toList());
        int indexOfEndingCharacter = -1;
        for (int i = 0; i < endingCharacters.size(); i++) {
            int index = text.indexOf(endingCharacters.get(i));
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
