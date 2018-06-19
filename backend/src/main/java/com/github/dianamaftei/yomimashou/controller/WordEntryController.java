package com.github.dianamaftei.yomimashou.controller;

import com.github.dianamaftei.yomimashou.model.WordEntry;
import com.github.dianamaftei.yomimashou.service.WordEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/words")
public class WordEntryController {

    private final WordEntryService wordEntryService;

    @Autowired
    public WordEntryController(WordEntryService wordEntryService) {
        this.wordEntryService = wordEntryService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<WordEntry> get(@RequestParam("searchItem") String searchItem) {
        if (searchItem != null && searchItem.length() > 0) {
            return wordEntryService.get(searchItem.split(","));
        }
        return Collections.emptyList();
    }

    @RequestMapping(value = "/byStartingKanji", method = RequestMethod.GET)
    public List<WordEntry> getByStartingKanji(@RequestParam("searchItem") String searchItem) {
        if (searchItem != null && searchItem.length() > 0) {
            return wordEntryService.getByStartingKanji(searchItem);
        }
        return Collections.emptyList();
    }

    @RequestMapping(value = "/byEndingKanji", method = RequestMethod.GET)
    public List<WordEntry> getByEndingKanji(@RequestParam("searchItem") String searchItem) {
        if (searchItem != null && searchItem.length() > 0) {
            return wordEntryService.getByEndingKanji(searchItem);
        }
        return Collections.emptyList();
    }

    @RequestMapping(value = "/byContainingKanji", method = RequestMethod.GET)
    public List<WordEntry> getByContainingKanji(@RequestParam("searchItem") String searchItem) {
        if (searchItem != null && searchItem.length() > 0) {
            return wordEntryService.getByContainingKanji(searchItem);
        }
        return Collections.emptyList();
    }
}
