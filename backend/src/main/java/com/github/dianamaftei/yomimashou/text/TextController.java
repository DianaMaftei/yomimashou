package com.github.dianamaftei.yomimashou.text;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Set;

@RestController
@RequestMapping("/api/text")
@CrossOrigin
public class TextController {

    private final TextParserService textParserService;

    @Autowired
    public TextController(TextParserService textParserService) {
        this.textParserService = textParserService;
    }

    @PostMapping(value = "/parse/words")
    public Set<String> getWords(@RequestBody String text) {
        if (text != null && text.length() > 0) {
            return textParserService.parseWords(text);
        }
        return Collections.emptySet();
    }

    @PostMapping(value = "/parse/names")
    public Set<String> getNames(@RequestBody String text) {
        if (text != null && text.length() > 0) {
            return textParserService.parseNames(text);
        }
        return Collections.emptySet();
    }
}
