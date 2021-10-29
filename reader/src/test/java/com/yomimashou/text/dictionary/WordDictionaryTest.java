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
class WordDictionaryTest {

  @InjectMocks
  private WordDictionary wordDictionary;

  @BeforeEach
  void setUp() {
    wordDictionary.setReader(new StringReader(
        "じゆうみんしゅとう|いたバイク|ぬかずきむし|ちきゅうがいせいめい|せんたいしょう|じゅんまいしゅ|" + "ガリアじん|おにおこぜ|スタンフォード|主眼をおく|"));
  }

  @Test
  void isWordInDictionaryShouldReturnTrueIfWordIsInFile() {
    assertTrue(wordDictionary.isWordInDictionary("じゅんまいしゅ"));
  }

  @Test
  void isWordInDictionaryShouldReturnFalseIfWordIsNotInFile() {
    assertFalse(wordDictionary.isWordInDictionary("竜飛人"));
  }
}
