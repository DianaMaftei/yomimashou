package com.github.dianamaftei.yomimashou.text;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class TextServiceTest {

  @Mock
  private TextRepository textRepository;

  @Mock
  private KanjiCategories kanjiCategories;

  private TextService textService;

  private Text text;

  @BeforeEach
  void setUp() {
    textService = new TextService(textRepository, kanjiCategories);

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
  void addShouldCreateATextExcerpt() {
    when(textRepository.save(text)).thenReturn(text);

    textService.add(text);

    assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
            + "Mauris tristique, metus ac convallis condimentum, nunc purus molestie quam, "
            + "id rutrum risus metus vel lorem. Sed porttitor tempus dui, non placerat ligula. "
            + "Vivamus pulvinar enim justo, sit amet congue risus dapibus ultricies.",
        text.getExcerpt());
  }

  @Test
  void getAllShouldReturnAllTexts() {
    when(textRepository.findAll(any(Sort.class))).thenReturn(Collections.singletonList(text));

    List<Text> allTexts = textService.getAll();

    assertAll("Should return a list with only the text element",
        () -> assertEquals(1, allTexts.size()),
        () -> assertEquals(text.getTitle(), allTexts.get(0).getTitle()));
  }

  @Test
  void getByIdShouldReturnCorrespondingText() {
    text.setId(42L);
    when(textRepository.getOne(42L)).thenReturn(text);

    Text textById = textService.getById(42L);

    assertEquals(text.getId(), textById.getId());
  }
}
