package com.yomimashou.creator.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class CreatorProperties {

    @Value("${path.dictionary.kanji}")
    private String kanjiDictionaryPath;
    @Value("${path.dictionary.name}")
    private String nameDictionaryPath;
    @Value("${path.dictionary.word}")
    private String wordDictionaryPath;
    @Value("${path.name.entries}")
    private String nameEntriesPath;
    @Value("${path.word.entries}")
    private String wordEntriesPath;
    @Value("${path.rtk}")
    private String rtkFilePath;
    @Value("${path.sentences}")
    private String sentencesFilePath;
    @Value("${path.aozoraBunko}")
    private String aozoraBunkoCSVFilePath;

    @Value("${api.creator.username}")
    private String username;
    @Value("${api.creator.password}")
    private String password;
}
