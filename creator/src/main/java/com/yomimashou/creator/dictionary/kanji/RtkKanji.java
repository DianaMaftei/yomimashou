package com.yomimashou.creator.dictionary.kanji;

import lombok.Data;

@Data
public class RtkKanji {
    private Character kanji;
    private String components;
    private String keyword;
    private String story1;
    private String story2;
}
