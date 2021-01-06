package com.yomimashou.text.dictionary;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NameDictionaryTest {

  @InjectMocks
  private NameDictionary nameDictionary;

  @BeforeEach
  void setUp() {
    nameDictionary.setReader(new StringReader(
        "のりみや|納又滝|はねあな|のりむね|スタンフォード|陽夏乃|なわだみなみ|しんうしごめ|ナルバンジャン|"
            + "のりむら|はっとりケイ|ストケシア|戸田ゴルフ場|はねいさ|はねいし|しのざきふみのり|" + "シールズフィールド|はねいち|のりもち"));
  }

  @Test
  void isNameInDictionaryShouldReturnTrueIfNameIsInFile() {
    assertTrue(nameDictionary.isNameInDictionary("のりみや"));
  }

  @Test
  void isNameInDictionaryShouldReturnFalseIfNameIsNotInFile() {
    assertFalse(nameDictionary.isNameInDictionary("竜飛人"));
  }
}
