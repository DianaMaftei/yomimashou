package com.github.dianamaftei.yomimashou.text.dictionary;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WordDictionaryTest {

  private WordDictionary wordDictionary;

  @BeforeEach
  void setUp() {
    WordDictionary.setReader(new StringReader(
        "じゆうみんしゅとう|いたバイク|ぬかずきむし|ちきゅうがいせいめい|せんたいしょう|じゅんまいしゅ|" + "ガリアじん|おにおこぜ|スタンフォード|主眼をおく|"));
    wordDictionary = WordDictionary.getInstance("");
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
