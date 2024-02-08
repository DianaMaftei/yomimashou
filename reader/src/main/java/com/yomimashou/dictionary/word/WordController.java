package com.yomimashou.dictionary.word;

import com.yomimashou.appscommon.model.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/dictionary/words")
public class WordController {

  private final WordService wordService;

  @Autowired
  public WordController(final WordService wordService) {
    this.wordService = wordService;
  }

  @GetMapping(value = "/byStartingKanji")
  public Page<Word> getByStartingKanji(@RequestParam("searchItem") final String searchItem, final Pageable pageable) {
    if (searchItem.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing search item");
    }
    return wordService.getByStartingKanji(searchItem, pageable);
  }

  @GetMapping(value = "/byEndingKanji")
  public Page<Word> getByEndingKanji(@RequestParam("searchItem") final String searchItem, final Pageable pageable) {
    if (searchItem.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing search item");
    }
    return wordService.getByEndingKanji(searchItem, pageable);
  }

  @GetMapping(value = "/byContainingKanji")
  public Page<Word> getByContainingKanji(@RequestParam("searchItem") final String searchItem, final Pageable pageable) {
    if (searchItem.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing search item");
    }
    return wordService.getByContainingKanji(searchItem, pageable);
  }
}
