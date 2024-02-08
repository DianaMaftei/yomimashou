package com.yomimashou.dictionary.kanji;

import com.yomimashou.appscommon.model.Kanji;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KanjiServiceTest {

    @InjectMocks
    private KanjiService kanjiService;

    @Mock
    private KanjiRepository kanjiRepository;

    @Test
    void findByCharacters() {
        // Arrange
        Set<String> characters = new HashSet<>();
        List<Kanji> expectedKanji = Arrays.asList(
                Kanji.builder().character("猫").build(),
                Kanji.builder().character("犬").build());
        when(kanjiRepository.findByCharacterIn(characters)).thenReturn(expectedKanji);

        // Act
        List<Kanji> actualKanji = kanjiService.findByCharacters(characters);

        // Assert
        assertEquals(expectedKanji.size(), actualKanji.size());
        assertEquals(expectedKanji.get(0).getCharacter(), actualKanji.get(0).getCharacter());
        assertEquals(expectedKanji.get(1).getCharacter(), actualKanji.get(1).getCharacter());
    }

    @Test
    void findByCharactersShouldThrowExceptionWhenKanjiSetIsNull() {
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            kanjiService.findByCharacters(null);
        });
    }
}
