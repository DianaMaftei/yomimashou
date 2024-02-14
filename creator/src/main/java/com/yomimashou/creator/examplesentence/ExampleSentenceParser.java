package com.yomimashou.creator.examplesentence;

import com.yomimashou.appscommon.model.ExampleSentence;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ExampleSentenceParser {

    private static final String TAB = "\t";

    public ExampleSentence parse(final String line) {
        //Structure:  Sentence id [tab] Language [tab] Sentence [tab] Translation(s) [tab] Breakdown
        final String[] columns = line.split(TAB);

        final String sentence = columns[2];
        final String meaning = columns.length > 3 ? columns[3] : null;
        final String breakdown = columns.length > 4 ? columns[4] : null;

        final ExampleSentence exampleSentence = new ExampleSentence();
        exampleSentence.setSentence(sentence);
        exampleSentence.setMeaning(meaning);
        exampleSentence.setTextBreakdown(breakdown);

        return exampleSentence;
    }
}
