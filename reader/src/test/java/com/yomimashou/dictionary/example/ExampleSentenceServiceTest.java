package com.yomimashou.dictionary.example;

import com.yomimashou.appscommon.model.ExampleSentence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class ExampleSentenceServiceTest {


    @InjectMocks
    private ExampleSentenceService exampleSentenceService;

    @Mock
    private ExampleSentenceRepository exampleSentenceRepository;

    private static Pageable pageable = PageRequest.of(0, 10);

    @Test
    void get() {
        final String[] searchItems = {"searchItem"};
        final ExampleSentence exampleSentence = new ExampleSentence();
        exampleSentence.setSentence("test sentence by search");
        when(exampleSentenceRepository.findBySentenceOrTextBreakdownIn(searchItems, pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(exampleSentence), pageable, 1));

        Page<ExampleSentence> exampleSentences = exampleSentenceService.get(searchItems, pageable);

        assertEquals(1, exampleSentences.getTotalElements());
        assertEquals("test sentence by search", exampleSentences.getContent().get(0).getSentence());
        verify(exampleSentenceRepository, times(1)).findBySentenceOrTextBreakdownIn(searchItems, pageable);
    }
}
