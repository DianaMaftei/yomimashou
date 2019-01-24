package com.github.dianamaftei.yomimashou.text.dictionary;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class DeinflectorTest {

    private Deinflector deinflector;


    @Before
    public void setUp() {
        deinflector = Deinflector.getInstance(Deinflector.class.getResource("/").getPath());
    }

    @Test
    public void getDeinflectedWordsForTaEndingShouldReturnSingleResult() {
        assertEquals(Collections.singleton("夢を見る"), deinflector.getDeinflectedWords("夢を見た"));
    }

    @Test
    public void getDeinflectedWordsForEnaiEndingShouldReturnMultipleResults() {
        Set<String> deinflectedWords = Stream.of("見える", "見えなう", "見う", "見えないる", "見え", "見えなる").collect(Collectors.toSet());

        assertEquals(deinflectedWords, deinflector.getDeinflectedWords("見えない"));
    }

    @Test
    public void getDeinflectedWordsForYouAwaEndingShouldReturnNoResults() {
        assertEquals(Collections.emptySet(), deinflector.getDeinflectedWords("見あわ"));
    }
}