package com.github.dianamaftei.yomimashou.dictionary.kanji;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KanjiServiceTest {

  @InjectMocks
  private KanjiService kanjiService;

  @Mock
  private KanjiRepository kanjiRepository;

  @BeforeEach
  void setUp() throws Exception {
    kanjiService = new KanjiService(kanjiRepository);
  }

  @Test
  void get() {
    String searchItem = "searchItem";
    Kanji kanjiEntry = new Kanji();
    kanjiEntry.setCharacter("kanji");
    when(kanjiRepository.findByCharacter(searchItem)).thenReturn(kanjiEntry);

    Kanji result = kanjiService.get(searchItem);

    verify(kanjiRepository, times(1)).findByCharacter(searchItem);
    assertEquals("kanji", result.getCharacter());
  }

  @Test
  void getKanjiSVG() {
    // TODO test this method
  }
}
