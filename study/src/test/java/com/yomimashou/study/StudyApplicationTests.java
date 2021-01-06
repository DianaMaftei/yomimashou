package com.yomimashou.study;

import static org.assertj.core.api.Assertions.assertThat;

import com.yomimashou.study.studydeck.DeckController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class StudyApplicationTests {

  @Autowired
  private DeckController deckController;

  @Test
  void contextLoads() {
    assertThat(deckController).isNotNull();
  }

}
