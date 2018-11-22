package com.github.dianamaftei.yomimashou.text;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TextService {

    private final TextRepository textRepository;
    private final String[] SENTENCE_ENDING_CHARACTERS = {"。", ".", "…", "‥", "！", "？"};


    @Autowired
    public TextService(TextRepository textRepository) {
        this.textRepository = textRepository;
    }

    public Text add(Text text) {
        text.setExcerpt(getExerpt(text));
        return textRepository.save(text);
    }

    public List<Text> getAll() {
        return this.textRepository.findAll(new Sort(Sort.Direction.DESC, "creationDate"));
    }

    public Text getById(Long id) {
        return this.textRepository.getOne(id);
    }


    private String getExerpt(Text text) {
        String snippet = text.getContent().substring(0, getIndexOfSentenceEnd(text.getContent()));
        boolean snippetEndsWithEndingCharacter = false;

        for (String sentenceEndingCharacter : SENTENCE_ENDING_CHARACTERS) {
            if (snippet.endsWith(sentenceEndingCharacter)) {
                snippetEndsWithEndingCharacter = true;
                break;
            }
        }

        if (!snippetEndsWithEndingCharacter) {
            return snippet + "...";
        }

        return snippet;
    }

    private int getIndexOfSentenceEnd(String text) {
        int startOfEndingCharacterSearch = 250;
        int endOfEndingCharacterSearch = 350;
        int indexOfEndingCharacter = 100;

        String substring = text.substring(startOfEndingCharacterSearch, endOfEndingCharacterSearch + 1);

        for (String character : SENTENCE_ENDING_CHARACTERS) {
            if (substring.contains(character) && substring.indexOf(character) < indexOfEndingCharacter) {
                indexOfEndingCharacter = substring.indexOf(character);
            }
        }

        return startOfEndingCharacterSearch + indexOfEndingCharacter + 1;
    }
}
