package com.yomimashou.dictionary.word;

import com.yomimashou.appscommon.model.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
class WordControllerTest {

    public static final String API_DICTIONARY_WORDS_URL = "/api/dictionary/words";
    private MockMvc mvc;

    @Mock
    private WordService wordService;

    @InjectMocks
    private WordController wordController;

    private Pageable pageable;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(wordController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void getByStartingKanjiShouldGetAListOfWordsThatStartWithTheGivenKanji() throws Exception {
        // Arrange
        Word word = Word.builder().kanjiElements(Collections.singleton("猫舌")).build();
        Word word2 = Word.builder().kanjiElements(Collections.singleton("猫背")).build();
        final String searchItem = "猫";
        when(wordService.getByStartingKanji(searchItem, pageable))
                .thenReturn(new PageImpl<>(Arrays.asList(word, word2), pageable, 2));

        // Act
        final MockHttpServletResponse response = getMockHttpServletResponse("/byStartingKanji", searchItem);

        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(StandardCharsets.UTF_8).indexOf("kanjiElements\":[\"猫舌\"]")).isGreaterThan(0);
        assertThat(response.getContentAsString(StandardCharsets.UTF_8).indexOf("kanjiElements\":[\"猫背\"]")).isGreaterThan(0);
    }

    @Test
    void getByContainingKanjiShouldGetAListOfWordsThatContainTheGivenKanji() throws Exception {
        // Arrange
        Word word = Word.builder().kanjiElements(Collections.singleton("家系")).build();
        Word word2 = Word.builder().kanjiElements(Collections.singleton("国家独占")).build();
        Word word3 = Word.builder().kanjiElements(Collections.singleton("愛煙家")).build();
        final String searchItem = "家";
        when(wordService.getByContainingKanji(searchItem, pageable))
                .thenReturn(new PageImpl<>(Arrays.asList(word, word2, word3), pageable, 3));

        // Act
        final MockHttpServletResponse response = getMockHttpServletResponse("/byContainingKanji", searchItem);

        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(StandardCharsets.UTF_8).indexOf("kanjiElements\":[\"家系\"]")).isGreaterThan(0);
        assertThat(response.getContentAsString(StandardCharsets.UTF_8).indexOf("kanjiElements\":[\"国家独占\"]")).isGreaterThan(0);
        assertThat(response.getContentAsString(StandardCharsets.UTF_8).indexOf("kanjiElements\":[\"愛煙家\"]")).isGreaterThan(0);
    }

    @Test
    void getByEndingKanjiShouldGetAListOfWordsThatEndWithTheGivenKanji() throws Exception {
        // Arrange
        Word word = Word.builder().kanjiElements(Collections.singleton("金曜日")).build();
        Word word2 = Word.builder().kanjiElements(Collections.singleton("在りし日")).build();
        final String searchItem = "日";
        when(wordService.getByEndingKanji(searchItem, pageable))
                .thenReturn(new PageImpl<>(Arrays.asList(word, word2), pageable, 2));

        // Act
        final MockHttpServletResponse response = getMockHttpServletResponse("/byEndingKanji", searchItem);

        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString(StandardCharsets.UTF_8).indexOf("kanjiElements\":[\"金曜日\"]")).isGreaterThan(0);
        assertThat(response.getContentAsString(StandardCharsets.UTF_8).indexOf("kanjiElements\":[\"在りし日\"]")).isGreaterThan(0);
    }

    private MockHttpServletResponse getMockHttpServletResponse(String urlPath, String searchItem) throws Exception {
        final MockHttpServletResponse response = mvc.perform(
                get(API_DICTIONARY_WORDS_URL + urlPath)
                        .param("searchItem", searchItem)
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        return response;
    }
}
