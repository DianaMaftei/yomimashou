package com.yomimashou.creator.examplesentence;

import com.yomimashou.creator.config.CreatorProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class ExampleSentencesDictionary {

    @Setter
    private Reader reader;
    private ExampleSentenceRepository exampleSentenceRepository;
    private ExampleSentenceParser exampleSentenceParser;
    private CreatorProperties creatorProperties;
    private String filePath;


    @PostConstruct
    private void postConstruct() {
        this.filePath = creatorProperties.getSentencesFilePath();
    }

    public void saveSentencesFromFileToDB() {
        try (final BufferedReader sentenceReader = new BufferedReader(getReader(filePath))) {
            saveSentencesToDB(sentenceReader.lines().collect(Collectors.toList()));
        } catch (final IOException e) {
            log.error("Could not read sentences from file: " + filePath + ", and save to db ", e);
        }
    }

    private void saveSentencesToDB(final List<String> sentences) {
        exampleSentenceRepository.saveAll(
                sentences.parallelStream()
                        .map(exampleSentenceParser::parse)
                        .collect(Collectors.toSet())
        );
    }

    private Reader getReader(final String filePath) throws FileNotFoundException {
        if (reader != null) {
            return reader;
        }

        return new FileReader(filePath);
    }
}
