package com.yomimashou.dictionary.example;

import com.yomimashou.appscommon.model.ExampleSentence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
class ExampleSentenceControllerTest {

    public static final String API_DICTIONARY_EXAMPLES_URL = "/api/dictionary/examples";
    private MockMvc mvc;
    private static Pageable pageable;

    @Mock
    private ExampleSentenceService exampleSentenceService;

    private ExampleSentenceController exampleSentenceController;

    @BeforeEach
    void setup() {
        exampleSentenceController = new ExampleSentenceController(exampleSentenceService);
        mvc = MockMvcBuilders.standaloneSetup(exampleSentenceController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void shouldGetAListOfExampleSentencesBasedOnASearchItem() throws Exception {
        final ExampleSentence exampleSentence = new ExampleSentence();
        exampleSentence.setSentence("test sentence by search");
        final String searchItem = "test sentence";
        when(exampleSentenceService.get(new String[]{searchItem}, pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(exampleSentence), pageable, 1));

        final MockHttpServletResponse response = mvc.perform(
                get(API_DICTIONARY_EXAMPLES_URL)
                        .param("searchItem", searchItem)
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString().indexOf("sentence\":\"test sentence by search"))
                .isGreaterThan(0);
    }

    @Test
    void shouldThrowExceptionWhenSearchItemIsNull() throws Exception {
        final String searchItem = null;
        final MockHttpServletResponse response = mvc.perform(
                get(API_DICTIONARY_EXAMPLES_URL)
                        .param("searchItem", searchItem)
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldThrowExceptionWhenSearchItemIsAnEmptyString() throws Exception {
        final MockHttpServletResponse response = mvc.perform(
                get(API_DICTIONARY_EXAMPLES_URL)
                        .param("searchItem", "")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
