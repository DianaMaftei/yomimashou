package com.github.dianamaftei.yomimashou.text;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TextServiceTest {

  @Mock
  private TextRepository textRepository;
  @Mock
  private KanjiCategories kanjiCategories;

  private TextService textService;

  @Before
  public void setUp() {
    textService = new TextService(textRepository, kanjiCategories);
  }

  @Test
  public void addShouldCreateATextExcerpt() {
    Text text = new Text();
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

    when(textRepository.save(text)).thenReturn(text);

    textService.add(text);

    assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
            + "Mauris tristique, metus ac convallis condimentum, nunc purus molestie quam, "
            + "id rutrum risus metus vel lorem. Sed porttitor tempus dui, non placerat ligula. "
            + "Vivamus pulvinar enim justo, sit amet congue risus dapibus ultricies.",
        text.getExcerpt());
  }
}