package com.github.dianamaftei.yomimashou.dictionary.word;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/words")
public class WordController {

    private final WordService wordService;

    @Autowired
    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Word> get(@RequestParam("searchItem") String searchItem) {
        if (searchItem != null && searchItem.length() > 0) {
            return wordService.get(searchItem.split(","));
        }
        return Collections.emptyList();
    }

    @RequestMapping(value = "/byStartingKanji", method = RequestMethod.GET)
    public List<Word> getByStartingKanji(@RequestParam("searchItem") String searchItem) {
        if (searchItem != null && searchItem.length() > 0) {
            return wordService.getByStartingKanji(searchItem);
        }
        return Collections.emptyList();
    }

    @RequestMapping(value = "/byEndingKanji", method = RequestMethod.GET)
    public List<Word> getByEndingKanji(@RequestParam("searchItem") String searchItem) {
        if (searchItem != null && searchItem.length() > 0) {
            return wordService.getByEndingKanji(searchItem);
        }
        return Collections.emptyList();
    }

    @RequestMapping(value = "/byContainingKanji", method = RequestMethod.GET)
    public List<Word> getByContainingKanji(@RequestParam("searchItem") String searchItem) {
        if (searchItem != null && searchItem.length() > 0) {
            return wordService.getByContainingKanji(searchItem);
        }
        return Collections.emptyList();
    }
}
