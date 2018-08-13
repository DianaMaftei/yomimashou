package com.github.dianamaftei.yomimashou.dictionary.kanji;

import com.github.dianamaftei.yomimashou.dictionary.kanji.Kanji;
import com.github.dianamaftei.yomimashou.dictionary.kanji.KanjiController;
import com.github.dianamaftei.yomimashou.dictionary.kanji.KanjiService;
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
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(MockitoJUnitRunner.class)
public class KanjiControllerTest {
    private MockMvc mvc;

    @Mock
    private KanjiService kanjiService;

    @InjectMocks
    private KanjiController kanjiController;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(kanjiController).build();
    }

    @Test
    public void shouldGetAKanjiEntryBasedOnASearchItem() throws Exception {
        Kanji kanji = new Kanji();
        kanji.setMeaning("cat");
        when(kanjiService.get("猫")).thenReturn(kanji);

        MockHttpServletResponse response = mvc.perform(get("/api/kanji?searchItem=猫")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString().indexOf("meaning\":\"cat")).isGreaterThan(0);
    }

    @Test
    public void shouldGetAKanjiSVG() throws Exception {
        String kanji = "猫";
        String svg = "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">\n" +
                "<circle cx=\"100\" cy=\"50\" r=\"40\" stroke=\"black\" stroke-width=\"2\" fill=\"red\" />\n" +
                "</svg> ";
        byte[] svgBytes = svg.getBytes();
        when(kanjiService.getKanjiSVG(kanji)).thenReturn(svgBytes);

        MockHttpServletResponse response = mvc.perform(get("/api/kanji/svg/" + kanji)
                .characterEncoding("UTF-8")
                .accept(javax.ws.rs.core.MediaType.APPLICATION_SVG_XML))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertEquals(svg, response.getContentAsString());
    }

}