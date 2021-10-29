package com.yomimashou.dictionary.kanji;

import com.yomimashou.appscommon.model.Kanji;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dictionary/kanji")
public class KanjiController {

  private final KanjiService kanjiService;

  @Autowired
  public KanjiController(final KanjiService kanjiService) {
    this.kanjiService = kanjiService;
  }

  @GetMapping
  public Kanji get(@RequestParam("searchItem") final String searchItem) {
    return kanjiService.get(searchItem);
  }

  @GetMapping(value = "/svg/{kanji}", produces = "image/svg+xml")
  public byte[] getStrokesSVG(@PathVariable final String kanji) {
    return kanjiService.getStrokesSVG(kanji);
  }
}
