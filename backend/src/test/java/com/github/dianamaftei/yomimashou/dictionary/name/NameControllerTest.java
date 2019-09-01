package com.github.dianamaftei.yomimashou.dictionary.name;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.github.dianamaftei.appscommon.model.Name;
import java.util.Collections;
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

@ExtendWith(MockitoExtension.class)
public class NameControllerTest {

  private MockMvc mvc;

  @Mock
  private NameService nameService;

  @InjectMocks
  private NameController nameController;

  private Pageable pageable;

  @BeforeEach
  void setup() {
    mvc = MockMvcBuilders.standaloneSetup(nameController)
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .build();

    pageable = new PageRequest(0, 10);
  }

  @Test
  void shouldGetANameEntryBasedOnASearchItem() throws Exception {
    final Name name = new Name();
    name.setKanji("猫");
    name.setId(5L);
    when(nameService.getByReadingElemOrKanjiElem(new String[]{"猫"}, pageable))
        .thenReturn(new PageImpl<>(Collections.singletonList(name), pageable, 1));

    final MockHttpServletResponse response = mvc
        .perform(get("/api/dictionary/names?searchItem=猫&page=0&size=10")
            .accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString().indexOf("kanji\":\"猫")).isGreaterThan(0);
  }
}
