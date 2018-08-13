package com.github.dianamaftei.yomimashou.dictionary.creator;

import com.github.dianamaftei.yomimashou.dictionary.example.ExampleSentence;
import com.github.dianamaftei.yomimashou.dictionary.example.ExampleSentenceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExampleSentencesCSVtoPOJOTest {

    @InjectMocks
    private ExampleSentencesCSVtoPOJO exampleSentencesCSVtoPOJO;

    @Mock
    private ExampleSentenceRepository exampleSentenceRepository;

    @Mock
    private ClassPathResource classPathResource;

    @Captor
    private ArgumentCaptor<ExampleSentence> argumentCaptor;

    @Test
    public void saveSentencesFromFileToDBShouldSaveAllExampleSentences() throws IOException {
        exampleSentencesCSVtoPOJO.setResource(classPathResource);
        when(classPathResource.getInputStream()).thenReturn(new ByteArrayInputStream(
                ("3400993\tjpn\tこれ、可愛いな。\tThis is cute.\n" +
                 "1204364\tjpn\t明日の午前２時４０分てそれ１回目？それとも２回目？\tIs it the first time at 2:40AM tomorrow? Or is it the second time?")
                 .getBytes()));

        exampleSentencesCSVtoPOJO.saveSentencesFromFileToDB();

        verify(exampleSentenceRepository, times(2)).save(any());
    }

    @Test
    public void saveSentencesFromFileToDBShouldParseTheSentencesCorrectly() throws IOException {
        exampleSentencesCSVtoPOJO.setResource(classPathResource);
        when(classPathResource.getInputStream()).thenReturn(new ByteArrayInputStream("3400993\tjpn\tこれ、可愛いな。\tThis is cute.".getBytes()));

        exampleSentencesCSVtoPOJO.saveSentencesFromFileToDB();

        verify(exampleSentenceRepository).save(argumentCaptor.capture());
        ExampleSentence exampleSentence = argumentCaptor.getValue();

        assertEquals("これ、可愛いな。", exampleSentence.getSentence());
        assertEquals("This is cute.", exampleSentence.getMeaning());
        assertNull(exampleSentence.getTextBreakdown());
    }

    @Test
    public void saveSentencesFromFileToDBShouldNotSaveAnythingToTheDBIfReadingTheFileThrowsAnError() throws IOException {
        exampleSentencesCSVtoPOJO.setResource(classPathResource);
        when(classPathResource.getInputStream()).thenThrow(new IOException());

        exampleSentencesCSVtoPOJO.saveSentencesFromFileToDB();

        verify(exampleSentenceRepository, times(0)).save(any());
    }
}