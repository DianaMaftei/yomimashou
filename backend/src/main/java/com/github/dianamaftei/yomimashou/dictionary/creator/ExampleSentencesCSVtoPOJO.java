package com.github.dianamaftei.yomimashou.dictionary.creator;

import com.github.dianamaftei.yomimashou.dictionary.example.ExampleSentence;
import com.github.dianamaftei.yomimashou.dictionary.example.ExampleSentenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExampleSentencesCSVtoPOJO {
    private final ExampleSentenceRepository exampleSentenceRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleSentencesCSVtoPOJO.class);
    private ClassPathResource resource;

    @Autowired
    public ExampleSentencesCSVtoPOJO(ExampleSentenceRepository exampleSentenceRepository) {
        this.exampleSentenceRepository = exampleSentenceRepository;
        resource = new ClassPathResource("dictionaries" + File.separator + "sentencesWithTranslationAndBreakdown.csv");
    }

    public void saveSentencesFromFileToDB() {
        List<ExampleSentence> exampleSentenceList = new ArrayList<>();

        try (BufferedReader sentenceReader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            sentenceReader.lines().forEach(line -> {
                exampleSentenceList.add(getExampleSentenceFromCSVLine(line));
            });

            exampleSentenceList.parallelStream().forEach(exampleSentenceRepository::save);

        } catch (IOException e) {
            LOGGER.error("Could not read sentences from resource: " + resource.getFilename() + ", and save to db ", e);
        }
    }

    private ExampleSentence getExampleSentenceFromCSVLine(String line) {
        //Structure:  Sentence id [tab] Language [tab] Sentence [tab] Translation(s) [tab] Breakdown
        String[] columns = line.split("\t");

        String sentence = columns[2];
        String meaning = columns.length > 3 ? columns[3] : null;
        String breakdown = columns.length > 4 ? columns[4] : null;

        ExampleSentence exampleSentence = new ExampleSentence();
        exampleSentence.setSentence(sentence);
        exampleSentence.setMeaning(meaning);
        exampleSentence.setTextBreakdown(breakdown);

        return exampleSentence;
    }

    void setResource(ClassPathResource resource) {
        this.resource = resource;
    }
}
