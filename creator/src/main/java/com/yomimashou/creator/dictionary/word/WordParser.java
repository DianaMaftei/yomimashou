package com.yomimashou.creator.dictionary.word;

import com.yomimashou.appscommon.model.Word;
import com.yomimashou.appscommon.model.WordMeaning;
import com.yomimashou.creator.dictionary.word.jmdictXMLmodels.*;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class WordParser {

    private static final int HIGHEST_PRIORITY = 1;
    private static final int MODERATE_PRIORITY = 2;
    private static final int LOW_PRIORITY = 3;

    public Word parse(final Entry entry) {
        final Word word = new Word();

        word.setKanjiElements(
                entry.getKEle().stream().map(KEle::getKeb).collect(Collectors.toSet()));
        word.setReadingElements(
                entry.getREle().stream().map(REle::getReb).collect(Collectors.toSet()));
        word.setMeanings(buildMeaningsList(entry));
        word.setPriority(extractHighestPriority(entry));

        return word;
    }

    private Integer extractHighestPriority(final Entry entry) {
        final Set<Integer> priorities = new HashSet<>();
        entry.getKEle().forEach(kEle -> {
            if (!kEle.getKePri().isEmpty()) {
                kEle.getKePri().forEach(priority -> priorities.add(getPriorityNumber(priority)));
            }
        });
        entry.getREle().forEach(rEle -> {
            if (!rEle.getRePri().isEmpty()) {
                rEle.getRePri().forEach(priority -> priorities.add(getPriorityNumber(priority)));
            }
        });

        if (priorities.isEmpty()) {
            return LOW_PRIORITY;
        }

        // 1 is the highest priority
        return Collections.min(priorities);
    }

    private int getPriorityNumber(final String priority) {
        final List<String> highestPriorities = Arrays
                .asList("news1", "ichi1", "spec1", "spec2", "gai1");

        if (highestPriorities.contains(priority)) {
            return HIGHEST_PRIORITY;
        }
        return MODERATE_PRIORITY;
    }

    private List<WordMeaning> buildMeaningsList(final Entry entry) {
        final List<WordMeaning> meanings = new ArrayList<>();

        for (final Sense sense : entry.getSense()) {
            final WordMeaning meaning = new WordMeaning();
            meaning.setPartOfSpeech(String.join("|", sense.getPos().stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList())));
            meaning.setFieldOfApplication(String.join("|", sense.getField()));
            meaning.setAntonym(String.join("|", sense.getAnt()));
            meaning.setGlosses(String.join("|", sense.getGloss().stream()
                    .map(Gloss::getContent)
                    .flatMap(Collection::stream)
                    .map(Object::toString)
                    .collect(Collectors.toList())));

            meanings.add(meaning);
        }

        return meanings;
    }
}
