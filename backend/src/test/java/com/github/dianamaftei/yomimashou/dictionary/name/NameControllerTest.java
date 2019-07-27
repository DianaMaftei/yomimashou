package com.github.dianamaftei.yomimashou.dictionary.name;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Collections;
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
public class NameControllerTest {

  private MockMvc mvc;

  @Mock
  private NameService nameService;

  @InjectMocks
  private NameController nameController;

  @BeforeEach
  void setup() {
    mvc = MockMvcBuilders.standaloneSetup(nameController).build();
  }

  @Test
  void shouldGetANameEntryBasedOnASearchItem() throws Exception {
    Name name = new Name();
    name.setKanji("猫");
    name.setId(5L);
    when(nameService.get(new String[]{"猫"})).thenReturn(Collections.singletonList(name));

    MockHttpServletResponse response = mvc
        .perform(get("/api/dictionary/names?searchItem=猫").accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString().indexOf("kanji\":\"猫")).isGreaterThan(0);
  }
}
