package com.yomimashou.text;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.yomimashou.appscommon.service.KanjiCategoriesService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.yomimashou.appscommon.model.Text;
import com.yomimashou.dictionary.kanji.KanjiService;
import com.yomimashou.dictionary.word.WordService;
import com.yomimashou.analyzer.TokenizerService;
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
  private KanjiCategoriesService kanjiCategories;

  @Mock
  private TokenizerService tokenizerService;

  @Mock
  private WordService wordService;

  @Mock
  private KanjiService kanjiService;

  private TextService textService;

  private Text text;

  @BeforeEach
  void setUp() {
    textService = new TextService(textRepository, kanjiCategories, tokenizerService, wordService, kanjiService);

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

//    when(tokenizerService.tokenize(text.getContent())).thenReturn(Collections.singletonList(Token.builder().baseForm("Lorem").surface("Lorem").build()));
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

    final List<Text> allTexts = textService.getAll();

    assertAll("Should return a list with only the text element",
        () -> assertEquals(1, allTexts.size()),
        () -> assertEquals(text.getTitle(), allTexts.get(0).getTitle()));
  }

  @Test
  void getByIdShouldReturnCorrespondingText() {
    text.setId(42L);
    when(textRepository.findById(42L)).thenReturn(Optional.of(text));

    final Optional<Text> textById = textService.getById(42L);

    assertEquals(text.getId(), textById.get().getId());
  }
}
