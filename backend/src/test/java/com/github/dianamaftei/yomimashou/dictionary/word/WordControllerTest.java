package com.github.dianamaftei.yomimashou.dictionary.word;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(MockitoJUnitRunner.class)
public class WordControllerTest {
    private MockMvc mvc;

    @Mock
    private WordService wordService;

    @InjectMocks
    private WordController wordController;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(wordController)
                .build();
    }

    @Test
    public void shouldGetAListOfWordEntriesBasedOnASearchItem() throws Exception {
        Word word = new Word();
        word.setKanjiElements("猫");
        String searchItem = "猫";
        when(wordService.get(new String[]{searchItem})).thenReturn(Collections.singletonList(word));

        MockHttpServletResponse response = mvc.perform(get("/api/words?searchItem={attribute_uri}", "猫")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString().indexOf("kanjiElements\":\"猫")).isGreaterThan(0);
    }

    @Test
    public void shouldReturnAnEmptyListWhenSearchItemIsNull() throws Exception {
        String searchItem = null;

        MockHttpServletResponse response = mvc.perform(get("/api/words?searchItem={non_existent_variable}", searchItem)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    public void shouldReturnAnEmptyListWhenSearchItemIsAnEmptyString() throws Exception {
        String searchItem = "";

        MockHttpServletResponse response = mvc.perform(get("/api/words?searchItem={attribute_uri}", searchItem)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }
}