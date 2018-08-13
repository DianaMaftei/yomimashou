package com.github.dianamaftei.yomimashou.text;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Set;

@RestController
@RequestMapping("/api/text")
public class TextController {

    private final TextParserService textParserService;

    @Autowired
    public TextController(TextParserService textParserService) {
        this.textParserService = textParserService;
    }

    @GetMapping(value = "/parse/words")
    public Set<String> getWords(@RequestParam("text") String text) {
        if (text != null && text.length() > 0) {
            return textParserService.parseWords(text);
        }
        return Collections.emptySet();
    }

    @GetMapping(value = "/parse/names")
    public Set<String> getNames(@RequestParam("text") String text) {
        if (text != null && text.length() > 0) {
            return textParserService.parseNames(text);
        }
        return Collections.emptySet();
    }
}
