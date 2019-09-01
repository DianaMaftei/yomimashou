package com.github.dianamaftei.yomimashou.dictionary.kanji;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.github.dianamaftei.appscommon.model.Kanji;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class KanjiControllerTest {

  private MockMvc mvc;

  @Mock
  private KanjiService kanjiService;

  @InjectMocks
  private KanjiController kanjiController;

  @BeforeEach
  void setup() {
    mvc = MockMvcBuilders.standaloneSetup(kanjiController).build();
  }

  @Test
  void shouldGetAKanjiEntryBasedOnASearchItem() throws Exception {
    final Kanji kanji = new Kanji();
    kanji.setMeaning("cat");
    when(kanjiService.get("猫")).thenReturn(kanji);
    final MockHttpServletResponse response = mvc
        .perform(get("/api/dictionary/kanji?searchItem=猫").accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString().indexOf("meaning\":\"cat")).isGreaterThan(0);
  }

  @Test
  void shouldGetStrokesSVG() throws Exception {
    final String kanji = "猫";
    final String svg = "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">\n"
        + "<circle cx=\"100\" cy=\"50\" r=\"40\" stroke=\"black\" stroke-width=\"2\" fill=\"red\" />\n"
        + "</svg> ";
    final byte[] svgBytes = svg.getBytes();
    when(kanjiService.getStrokesSVG(kanji)).thenReturn(svgBytes);
    final MockHttpServletResponse response = mvc.perform(
        get("/api/dictionary/kanji/svg/" + kanji).characterEncoding("UTF-8")
            .accept("image/svg+xml")).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertEquals(svg, response.getContentAsString());
  }
}
