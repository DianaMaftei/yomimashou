package com.github.dianamaftei.yomimashou.dictionary.word;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
  public Page<Word> getByReadingElemOrKanjiElem(@RequestParam("searchItem") String searchItem,
      @PageableDefault(value = 10, page = 0) Pageable pageable) {
    if (searchItem.length() > 0) {
      return wordService.getByReadingElemOrKanjiElem(searchItem.split(","), pageable);
    }
    return Page.empty();
  }

  @GetMapping(value = "/byStartingKanji")
  public Page<Word> getByStartingKanji(@RequestParam("searchItem") String searchItem,
      @PageableDefault(value = 10, page = 0) Pageable pageable) {
    if (searchItem.length() > 0) {
      return wordService.getByStartingKanji(searchItem, pageable);
    }
    return Page.empty();
  }

  @GetMapping(value = "/byEndingKanji")
  public Page<Word> getByEndingKanji(@RequestParam("searchItem") String searchItem,
      @PageableDefault(value = 10, page = 0) Pageable pageable) {
    if (searchItem.length() > 0) {
      return wordService.getByEndingKanji(searchItem, pageable);
    }
    return Page.empty();
  }

  @GetMapping(value = "/byContainingKanji")
  public Page<Word> getByContainingKanji(@RequestParam("searchItem") String searchItem,
      @PageableDefault(value = 10, page = 0) Pageable pageable) {
    if (searchItem.length() > 0) {
      return wordService.getByContainingKanji(searchItem, pageable);
    }
    return Page.empty();
  }
}
