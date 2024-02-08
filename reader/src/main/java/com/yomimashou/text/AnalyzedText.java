package com.yomimashou.text;

import com.yomimashou.appscommon.model.Kanji;
import com.yomimashou.appscommon.model.Word;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyzedText {
    private List<Word> words;
    private List<Kanji> kanji;
}
