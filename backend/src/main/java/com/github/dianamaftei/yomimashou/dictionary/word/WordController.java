package com.github.dianamaftei.yomimashou.dictionary.word;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dictionary/words")
public class WordController {

    private final WordService wordService;

    @Autowired
    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping
    public List<Word> getByEqualsKanji(@RequestParam("searchItem") String searchItem) {
        if (searchItem != null && searchItem.length() > 0) {
            List<Word> words = wordService.getByEqualsKanji(searchItem.split(","));
            Collections.sort(words);
            return words;
        }
        return Collections.emptyList();
    }

    @GetMapping(value = "/byStartingKanji")
    public List<Word> getByStartingKanji(@RequestParam("searchItem") String searchItem) {
        if (searchItem != null && searchItem.length() > 0) {
            List<Word> words = wordService.getByStartingKanji(searchItem);
            Collections.sort(words);
            return words;
        }
        return Collections.emptyList();
    }

    @GetMapping(value = "/byEndingKanji")
    public List<Word> getByEndingKanji(@RequestParam("searchItem") String searchItem) {
        if (searchItem != null && searchItem.length() > 0) {
            List<Word> words = wordService.getByEndingKanji(searchItem);
            Collections.sort(words);
            return words;
        }
        return Collections.emptyList();
    }

    @GetMapping(value = "/byContainingKanji")
    public List<Word> getByContainingKanji(@RequestParam("searchItem") String searchItem) {
        if (searchItem != null && searchItem.length() > 0) {
            List<Word> words = wordService.getByContainingKanji(searchItem);
            Collections.sort(words);
            return words;
        }
        return Collections.emptyList();
    }
}
