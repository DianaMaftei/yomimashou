package com.yomimashou.appscommon.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KanjiCategoriesServiceTest {

    private final KanjiCategoriesService kanjiCategoriesService = new KanjiCategoriesService();

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void getKanjiCountByCategory_shouldReturn_emptyKanjiInfo_forBlankText(String text) {
        // Act
        Map<String, Integer> kanjiCountByCategory = kanjiCategoriesService.getKanjiCountByCategory(text);

        // Assert
        assertTrue(kanjiCountByCategory.isEmpty());
    }

    @Test
    void getKanjiCountByCategory_shouldReturn_populatedKanjiInfo_forTextWithKanji() {
        // Arrange
        String text = "彼女はきれいな花を見つけました。";

        // Act
        Map<String, Integer> kanjiCountByCategory = kanjiCategoriesService.getKanjiCountByCategory(text);

        // Assert
        assertAll(
                () -> assertEquals(0, kanjiCountByCategory.get("N1")),
                () -> assertEquals(0, kanjiCountByCategory.get("N2")),
                () -> assertEquals(1, kanjiCountByCategory.get("N3")),
                () -> assertEquals(1, kanjiCountByCategory.get("N4")),
                () -> assertEquals(2, kanjiCountByCategory.get("N5")),
                () -> assertEquals(3, kanjiCountByCategory.get("jouyou-1")),
                () -> assertEquals(0, kanjiCountByCategory.get("jouyou-2")),
                () -> assertEquals(0, kanjiCountByCategory.get("jouyou-3")),
                () -> assertEquals(1, kanjiCountByCategory.get("jouyou-S")),
                () -> assertEquals(0, kanjiCountByCategory.get("jouyou-4")),
                () -> assertEquals(0, kanjiCountByCategory.get("jouyou-5")),
                () -> assertEquals(0, kanjiCountByCategory.get("jouyou-6")),
                () -> assertEquals(0, kanjiCountByCategory.get("unknown"))
        );
    }

    @Test
    void getKanjiCountByCategory_shouldReturn_unknownKanjiCount_forKanjiNotInJLPTOrJouyou() {
        // Arrange
        String text = "鼠龠龜";

        // Act
        Map<String, Integer> kanjiCountByCategory = kanjiCategoriesService.getKanjiCountByCategory(text);

        // Assert
        assertEquals(3, kanjiCountByCategory.get("unknown"));
    }

    @ParameterizedTest
    @CsvSource({
            "高,N5",
            "的,N4",
            "息,N3",
            "丸,N2",
            "審,N1"
    })
    void getJlptLevel_shouldReturnNewJlptLevel_ifKanjiIsInJLPTLists(Character kanji, String expectedResult) {
        // Act
        String actualResult = kanjiCategoriesService.getJlptLevel(kanji);

        // Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getJlptLevel_shouldReturnNull_ifKanjiIsInNotInJLPTLists() {
        // Act
        String actualResult = kanjiCategoriesService.getJlptLevel('鼠');

        // Assert
        assertNull(actualResult);
    }
}