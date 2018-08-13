package com.github.dianamaftei.yomimashou.dictionary.kanji;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;

@RestController
@RequestMapping("/api/kanji")
public class KanjiController {

    private final KanjiService kanjiService;

    @Autowired
    public KanjiController(KanjiService kanjiService) {
        this.kanjiService = kanjiService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Kanji get(@RequestParam("searchItem") String searchItem) {
        return kanjiService.get(searchItem);
    }

    @ResponseBody
    @GetMapping(value = "/svg/{kanji}", produces = MediaType.APPLICATION_SVG_XML)
    public byte[] getKanjiSVG(@PathVariable String kanji) {
        return kanjiService.getKanjiSVG(kanji);
    }
}
