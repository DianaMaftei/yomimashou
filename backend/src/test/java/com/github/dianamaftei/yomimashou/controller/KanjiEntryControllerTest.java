package com.github.dianamaftei.yomimashou.controller;

import com.github.dianamaftei.yomimashou.model.KanjiEntry;
import com.github.dianamaftei.yomimashou.service.KanjiEntryService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(MockitoJUnitRunner.class)
public class KanjiEntryControllerTest {
    private MockMvc mvc;

    @Mock
    private KanjiEntryService kanjiEntryService;

    @InjectMocks
    private KanjiEntryController kanjiEntryController;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(kanjiEntryController)
                .build();
    }

    @Test
    public void shouldGetAKanjiEntryBasedOnASearchItem() throws Exception {
        // given
        KanjiEntry kanjiEntry = new KanjiEntry();
        kanjiEntry.setMeaning("cat");
        given(kanjiEntryService.get("猫")).willReturn(kanjiEntry);

        // when
        MockHttpServletResponse response = mvc.perform(get("/api/kanji?searchItem=猫")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString().indexOf("meaning\":\"cat")).isGreaterThan(0);
    }

}