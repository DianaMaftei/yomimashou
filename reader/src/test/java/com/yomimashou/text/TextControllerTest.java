package com.yomimashou.text;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.yomimashou.appscommon.model.Text;
import com.yomimashou.uploads.FileService;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

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
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class TextControllerTest {

  private MockMvc mvc;

  @Mock
  private TextService textService;

  @Mock
  private FileService fileService;

  @InjectMocks
  private TextController textController;

  private Text text;

  @Mock
  private MultipartFile multipartFile;

  @BeforeEach
  void setup() {
    mvc = MockMvcBuilders.standaloneSetup(textController).build();

    text = new Text();
    text.setContent(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris tristique, metus ac "
            + "convallis condimentum, nunc purus molestie quam, id rutrum risus metus vel lorem. "
            + "Sed porttitor tempus dui, non placerat ligula. Vivamus pulvinar enim justo, sit "
            + "amet congue risus dapibus ultricies. Sed eu imperdiet ipsum. Mauris luctus molestie "
            + "nisl, at auctor nunc vulputate ac. Sed eu nisi faucibus, elementum mi id, vestibulum "
            + "nunc. Ut viverra nisi est, ut lobortis quam vulputate eget. Mauris pellentesque "
            + "efficitur nisi, eu auctor magna gravida ac. Ut vehicula urna eu nisl convallis "
            + "malesuada. Integer tortor diam, eleifend sed ante vel, sollicitudin tempor enim. "
            + "Nullam sagittis sollicitudin ante vitae pellentesque.");
    text.setTitle("Lorem Ipsum");
  }

  @Test
  void addUploadsTheImageAndSavesTheTextWithTheFilename() throws Exception {
    when(fileService.upload(any(MultipartFile.class), anyString())).thenReturn("filename");
    when(textService.add(text)).thenReturn(text);

    Text textWithImage = textController.add(text, multipartFile);

    assertEquals("filename", textWithImage.getImageFileName());
    verify(textService, times(1)).add(text);
  }

  @Test
  void getAll() throws Exception {
    when(textService.getAll()).thenReturn(Collections.singletonList(text));

    MockHttpServletResponse response = mvc
        .perform(get("/api/text").accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString().indexOf("title\":\"Lorem Ipsum")).isGreaterThan(0);
  }

  @Test
  void getById() throws Exception {
    when(textService.getById(42L)).thenReturn(Optional.of(text));

    MockHttpServletResponse response = mvc
        .perform(get("/api/text/42").accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString().indexOf("title")).isGreaterThan(0);
  }

}
