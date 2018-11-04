package com.github.dianamaftei.yomimashou.dictionary.word;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/dictionary/words")
@CrossOrigin
public class WordController {

    private final WordService wordService;

    @Autowired
    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping
    public List<Word> get(@RequestParam("searchItem") String searchItem) {
        if (searchItem != null && searchItem.length() > 0) {
            return wordService.get(searchItem.split(","));
        }
        return Collections.emptyList();
    }

    @GetMapping(value = "/byStartingKanji")
    public List<Word> getByStartingKanji(@RequestParam("searchItem") String searchItem) {
        if (searchItem != null && searchItem.length() > 0) {
            return wordService.getByStartingKanji(searchItem);
        }
        return Collections.emptyList();
    }

    @GetMapping(value = "/byEndingKanji")
    public List<Word> getByEndingKanji(@RequestParam("searchItem") String searchItem) {
        if (searchItem != null && searchItem.length() > 0) {
            return wordService.getByEndingKanji(searchItem);
        }
        return Collections.emptyList();
    }

    @GetMapping(value = "/byContainingKanji")
    public List<Word> getByContainingKanji(@RequestParam("searchItem") String searchItem) {
        if (searchItem != null && searchItem.length() > 0) {
            return wordService.getByContainingKanji(searchItem);
        }
        return Collections.emptyList();
    }
}
