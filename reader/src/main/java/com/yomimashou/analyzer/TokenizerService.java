package com.yomimashou.analyzer;

import com.atilika.kuromoji.ipadic.Tokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenizerService {

    private static final String PUNCTUATION = " “”‘’。.，、,：:；;！!？?－…》《〈〉「」﹁﹂『』(（）)[]{}\n";
    private static final String DIGITS = "1234567890１２３４５６７８９０";

    @Autowired
    private Tokenizer tokenizer;

    public List<Token> tokenize(String text) {
        List<Token> tokens = tokenizer.tokenize(text).stream()
                .map(this::map)
                .collect(Collectors.toList());
        List<Token> mergedTokens = mergeInflectedTokens(tokens);

        return mergedTokens.stream()
                .filter(this::isNotSymbolOrDigitOrPunctuationMark)
                .collect(Collectors.toList());
    }

    private boolean isNotSymbolOrDigitOrPunctuationMark(Token token) {
        return !(PUNCTUATION.contains(token.getSurface()) ||
                (DIGITS.contains(token.getSurface())
                        || PartOfSpeech.SYMBOL.getValue().equals(token.getPartOfSpeechLevel1())));
    }

    private List<Token> mergeInflectedTokens(List<Token> tokens) {
        List<Token> processed = new ArrayList<>();

        for (int i = 0; i < tokens.size(); i++) {
            Token current = tokens.get(i);
            if (isInflectionable(current.getPartOfSpeechLevel1())) {
                for (int j = i + 1; j < tokens.size(); j++) {
                    Token next = tokens.get(j);
                    merge(current, next);
                    if (!next.isMerged()) {
                        break;
                    }
                    i = j;
                }
            }
            processed.add(current);
        }

        return processed;
    }

    private Token merge(Token current, Token next) {
        if (isInflection(next.getPartOfSpeechLevel1(), next.getPartOfSpeechLevel2())) {
            current.setSurface(current.getSurface() + next.getSurface());
            current.setReading(current.getReading() + next.getReading());
            if (isAdjectivalNoun(current, next)) {
                current.setPartOfSpeechLevel1(PartOfSpeech.NOUN.getValue());
                current.setPartOfSpeechLevel2("*");
            }
            next.setMerged(true);
        }

        return current;
    }

    private boolean isAdjectivalNoun(Token current, Token next) {
        return PartOfSpeech.ADJECTIVE.getValue().equals(current.getPartOfSpeechLevel1()) &&
                (PartOfSpeech.NOUN.getValue().equals(next.getPartOfSpeechLevel1()) && PartOfSpeech.SUFFIX.getValue().equals(next.getPartOfSpeechLevel2()));
    }

    private boolean isInflectionable(String partOfSpeech) {
        List<String> inflectionablePos = Arrays.asList(PartOfSpeech.VERB.getValue(), PartOfSpeech.AUXILIARY_VERB.getValue(), PartOfSpeech.ADJECTIVE.getValue(), PartOfSpeech.ADJECTIVE_VERB.getValue());
        return inflectionablePos.contains(partOfSpeech);
    }

    private boolean isInflection(String primaryPos, String secondaryPos) {
        return PartOfSpeech.AUXILIARY_VERB.getValue().equals(primaryPos) ||
                (PartOfSpeech.VERB.getValue().equals(primaryPos) && PartOfSpeech.SUFFIX.getValue().equals(secondaryPos)) ||
                (PartOfSpeech.NOUN.getValue().equals(primaryPos) && PartOfSpeech.SUFFIX.getValue().equals(secondaryPos)) ||
                (PartOfSpeech.PARTICLE.getValue().equals(primaryPos) &&
                        (PartOfSpeech.CONJUNCTION_PARTICLE.getValue().equals(secondaryPos) || PartOfSpeech.BINDING_PARTICLE.getValue().equals(secondaryPos)));
    }

    private Token map(com.atilika.kuromoji.ipadic.Token token) {
        return Token.builder()
                .surface(token.getSurface())
                .baseForm(token.getBaseForm())
                .reading(token.getReading())
                .partOfSpeechLevel1(token.getPartOfSpeechLevel1())
                .partOfSpeechLevel2(token.getPartOfSpeechLevel2())
                .build();

    }
}
