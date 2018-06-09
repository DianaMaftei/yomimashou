package com.github.dianamaftei.yomimashou.creator;

import com.github.dianamaftei.yomimashou.model.ExampleSentence;
import com.github.dianamaftei.yomimashou.repository.ExampleSentenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Component
public class SentencesCSVtoPOJO {
    private final ExampleSentenceRepository exampleSentenceRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(SentencesCSVtoPOJO.class);

    @Autowired
    public SentencesCSVtoPOJO(ExampleSentenceRepository exampleSentenceRepository) {
        this.exampleSentenceRepository = exampleSentenceRepository;
    }

    public void run() {
        String line;
        List<ExampleSentence> exampleSentenceList = new ArrayList<>();

        //Structure:  Sentence id [tab] Language [tab] Sentence [tab] Translation(s) [tab] Breakdown
        try (BufferedReader sentenceReader = Files.newBufferedReader(new File("src/main/resources/sentencesWithTranslationAndBreakdown.csv").toPath(), Charset.forName("UTF-8"))) {
            while ((line = sentenceReader.readLine()) != null) {
                String[] columns = line.split("\t");
                String sentence = columns[2];
                String meaning = columns.length > 3 ? columns[3] : null;
                String breakdown = columns.length > 4 ? columns[4] : null;

                ExampleSentence exampleSentence = new ExampleSentence();
                exampleSentence.setSentence(sentence);
                exampleSentence.setMeaning(meaning);
                exampleSentence.setTextBreakdown(breakdown);

                exampleSentenceList.add(exampleSentence);
            }

            exampleSentenceList.parallelStream().forEach(exampleSentenceRepository::save);
        } catch (IOException e) {
            LOGGER.error("Could not read csv file sentencesWithTranslationAndBreakdown", e);
        }
    }
}
