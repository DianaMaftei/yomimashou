package com.yomimashou.creator.examplesentence;

import com.yomimashou.appscommon.model.ExampleSentence;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ExampleSentenceParserTest {

    private final ExampleSentenceParser exampleSentenceParser = new ExampleSentenceParser();

    @ParameterizedTest
    @MethodSource("lines")
    void saveSentencesFromFileToDBShouldParseTheSentencesCorrectly(String line, String sentence, String meaning, String textBreakdown) {

        final ExampleSentence result = exampleSentenceParser.parse(line);

        assertAll("Should extract example sentence elements",
                () -> assertEquals(sentence, result.getSentence()),
                () -> assertEquals(meaning, result.getMeaning()),
                () -> assertEquals(textBreakdown, result.getTextBreakdown())
        );
    }

    private static Stream<Arguments> lines() {
        return Stream.of(
                Arguments.of("3400993\tjpn\tこれ、可愛いな。\tThis is cute.", "これ、可愛いな。", "This is cute.", null),
                Arguments.of("187088\tjpn\t家に帰る時間を知らせてくれ。\tLet me know when you'll return home.\t家(いえ)[01] に 帰る[01] 時間 を 知らせる{知らせて} 呉れる{くれ}", "家に帰る時間を知らせてくれ。", "Let me know when you'll return home.", "家(いえ)[01] に 帰る[01] 時間 を 知らせる{知らせて} 呉れる{くれ}")
        );
    }

}