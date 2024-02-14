package com.yomimashou.creator.examplesentence;

import com.yomimashou.appscommon.model.ExampleSentence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExampleSentencesDictionaryTest {
    @InjectMocks
    private ExampleSentencesDictionary exampleSentencesDictionary;

    @Mock
    private ExampleSentenceRepository exampleSentenceRepository;
    @Mock
    private ExampleSentenceParser exampleSentenceParser;

    @Captor
    private ArgumentCaptor<Set<ExampleSentence>> argumentCaptor;

    @Test
    void saveSentencesFromFileToDBShouldSaveAllExampleSentences() {
        String lines = "3400993\tjpn\tこれ、可愛いな。\tThis is cute.\n" +
                "187088\tjpn\t家に帰る時間を知らせてくれ。\tLet me know when you'll return home.\t家(いえ)[01] に 帰る[01] 時間 を 知らせる{知らせて} 呉れる{くれ}";
        StringReader reader = new StringReader(lines);
        exampleSentencesDictionary.setReader(reader);
        ExampleSentence exampleSentence1 = new ExampleSentence();
        ExampleSentence exampleSentence2 = new ExampleSentence();
        when(exampleSentenceParser.parse(anyString())).thenReturn(exampleSentence1).thenReturn(exampleSentence2);

        exampleSentencesDictionary.saveSentencesFromFileToDB();

        verify(exampleSentenceRepository, times(1)).saveAll(argumentCaptor.capture());

        Set<ExampleSentence> exampleSentences = argumentCaptor.getAllValues().get(0);
        assertEquals(2, exampleSentences.size());

        assertIterableEquals(Arrays.asList(exampleSentence1, exampleSentence2), exampleSentences);
    }
}
