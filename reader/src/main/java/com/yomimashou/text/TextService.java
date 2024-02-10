package com.yomimashou.text;

import com.yomimashou.analyzer.Token;
import com.yomimashou.analyzer.TokenizerService;
import com.yomimashou.appscommon.model.Kanji;
import com.yomimashou.appscommon.service.KanjiCategoriesService;
import com.yomimashou.appscommon.model.Text;
import com.yomimashou.appscommon.model.Word;
import com.yomimashou.dictionary.kanji.KanjiService;
import com.yomimashou.dictionary.word.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TextService {

    private static final String[] SENTENCE_ENDING_CHARACTERS = {"。", ".", "…", "‥", "！", "？"};
    private static final String CREATION_DATE = "creationDate";
    private static final String ELLIPSIS = "...";
    private static final int MAX_LENGTH_OF_EXCERPT = 350;
    private static final String DELIMITER = "|";

    private final TextRepository textRepository;
    private final KanjiCategoriesService kanjiCategories;
    private final TokenizerService tokenizerService;
    private final WordService wordService;
    private final KanjiService kanjiService;

    @Autowired
    public TextService(final TextRepository textRepository, final KanjiCategoriesService kanjiCategories, TokenizerService tokenizerService, WordService wordService, KanjiService kanjiService) {
        this.textRepository = textRepository;
        this.kanjiCategories = kanjiCategories;
        this.tokenizerService = tokenizerService;
        this.wordService = wordService;
        this.kanjiService = kanjiService;
    }

    public Text add(final Text text) {
        text.setExcerpt(getExcerpt(text.getContent()));
        text.setKanjiCountByLevel(kanjiCategories.getKanjiCountByCategory(text.getContent()));
        text.setCharacterCount(text.getContent().length());
        tokenize(text);
        return textRepository.save(text);
    }

    public List<Text> getAll() {
        return this.textRepository.findAll(Sort.by(Sort.Direction.DESC, CREATION_DATE));
    }

    public Optional<Text> getById(final Long id) {
        return this.textRepository.findById(id);
    }

    public AnalyzedText analyze(Text text) {
        return analyze(new HashSet<>(text.getParsedWords().values()),
                Arrays.stream(text.getParsedKanji().split(DELIMITER)).collect(Collectors.toSet()));
    }

    private Text tokenize(Text text) {
        List<Token> tokens = tokenizerService.tokenize(text.getContent());
        if (tokens.isEmpty()) {
            return text;
        }
        Map<String, String> parsedWords = new LinkedHashMap<>();
        tokens.stream().filter(token -> !"*".equals(token.getBaseForm())).forEach(token -> parsedWords.put(token.getSurface(), token.getBaseForm()));
        Set<String> parsedKanji = Arrays.stream(String.join("", parsedWords.keySet()).split(""))
                .filter(ch -> isKanji(ch.charAt(0)))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        text.setParsedWords(parsedWords);
        text.setParsedKanji(String.join(DELIMITER, parsedKanji));

        return text;
    }

    private AnalyzedText analyze(Set<String> parsedWords, Set<String> parsedKanji) {
        List<Word> wordsFromDictionary = wordService.findByReadingElementsOrKanjiElements(parsedWords);
        List<Kanji> kanjiFromDictionary = kanjiService.findByCharacters(parsedKanji);

        return AnalyzedText.builder()
                .words(wordsFromDictionary)
                .kanji(kanjiFromDictionary)
                .build();
    }

    private boolean isKanji(char ch) {
        return (ch >= 19968) && (ch <= 40864);
    }

    private String getExcerpt(final String textContent) {
        if (textContent.length() <= MAX_LENGTH_OF_EXCERPT) {
            return textContent;
        }

        final String snippet = textContent.substring(0, getIndexOfSentenceEnd(textContent));
        boolean snippetEndsWithEndingCharacter = false;

        for (final String sentenceEndingCharacter : SENTENCE_ENDING_CHARACTERS) {
            if (snippet.endsWith(sentenceEndingCharacter)) {
                snippetEndsWithEndingCharacter = true;
                break;
            }
        }

        if (!snippetEndsWithEndingCharacter) {
            return snippet + ELLIPSIS;
        }

        return snippet;
    }

    private int getIndexOfSentenceEnd(final String text) {
        final int startOfEndingCharacterSearch = 250;
        final int endOfEndingCharacterSearch = MAX_LENGTH_OF_EXCERPT;
        int indexOfEndingCharacter = 100;

        if (text.length() < endOfEndingCharacterSearch) {
            return text.length();
        }

        final String substring = text
                .substring(startOfEndingCharacterSearch, endOfEndingCharacterSearch + 1);

        for (final String character : SENTENCE_ENDING_CHARACTERS) {
            if (substring.contains(character) && substring.indexOf(character) < indexOfEndingCharacter) {
                indexOfEndingCharacter = substring.indexOf(character);
            }
        }

        return startOfEndingCharacterSearch + indexOfEndingCharacter + 1;
    }

}
