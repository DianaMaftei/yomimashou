package com.github.dianamaftei.yomimashou.controller;

import com.github.dianamaftei.yomimashou.controller.WordEntryController;
import com.github.dianamaftei.yomimashou.model.WordEntry;
import com.github.dianamaftei.yomimashou.service.WordEntryService;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(MockitoJUnitRunner.class)
public class WordEntryControllerTest {
    private MockMvc mvc;

    @Mock
    private WordEntryService wordEntryService;

    @InjectMocks
    private WordEntryController wordEntryController;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(wordEntryController)
                .build();
    }

    @Test
    public void shouldGetAListOfWordEntriesBasedOnASearchItem() throws Exception {
        // given
        WordEntry wordEntry = new WordEntry();
        wordEntry.setKanjiElements("猫");
        String searchItem = "猫";
        given(wordEntryService.get(new String[]{searchItem})).willReturn(Collections.singletonList(wordEntry));

        // when
        MockHttpServletResponse response = mvc.perform(get("/api/words?searchItem={attribute_uri}", "猫")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString().indexOf("kanjiElements\":\"猫")).isGreaterThan(0);
    }

    @Test
    public void shouldReturnAnEmptyListWhenSearchItemIsNull() throws Exception {
        // given
        String searchItem = null;

        // when
        MockHttpServletResponse response = mvc.perform(get("/api/words?searchItem={non_existent_variable}", searchItem)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    public void shouldReturnAnEmptyListWhenSearchItemIsAnEmptyString() throws Exception {
        // given
        String searchItem = "";

        // when
        MockHttpServletResponse response = mvc.perform(get("/api/words?searchItem={attribute_uri}", searchItem)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }
}