package com.yomimashou.creator.dictionary.kanji;

import com.yomimashou.creator.config.CreatorProperties;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class RtkService {
    @Setter
    private Reader reader;
    private CreatorProperties creatorProperties;
    @Setter
    private Map<Character, RtkKanji> rtkKanjiMap;
    private static final String TAB = "\t";

    @PostConstruct
    private void postConstruct() {
        this.rtkKanjiMap = loadRtkKanji();
    }

    public RtkKanji get(final Character kanji) {
        return rtkKanjiMap.get(kanji);
    }

    private Map<Character, RtkKanji> loadRtkKanji() {
        final Map<Character, RtkKanji> rtkKanjis = new HashMap<>();

        try (final BufferedReader bufferedReader = new BufferedReader(getReader(creatorProperties.getRtkFilePath()))) {
            bufferedReader.lines()
                    .forEach(line -> {
                        final RtkKanji rtkKanji = new RtkKanji();
                        final String[] splitLine = line.split(TAB);
                        rtkKanji.setKanji(splitLine[0].charAt(0));
                        if (splitLine.length > 1) {
                            rtkKanji.setComponents(splitLine[1]);
                        }
                        if (splitLine.length > 2) {
                            rtkKanji.setKeyword(splitLine[2]);
                            rtkKanji.setStory1(splitLine[3]);
                            rtkKanji.setStory2(splitLine[4]);
                        }

                        rtkKanjis.put(rtkKanji.getKanji(), rtkKanji);
                    });

        } catch (final IOException e) {
            log.error("Could not get rtk info from file", e);
        }

        return rtkKanjis;
    }

    private Reader getReader(final String filePath) throws FileNotFoundException {
        if (reader != null) {
            return reader;
        }

        return new FileReader(filePath);
    }
}
