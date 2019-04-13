package com.github.dianamaftei.yomimashou.text.dictionary;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.StringReader;

@RunWith(MockitoJUnitRunner.class)
public class NameDictionaryTest {

  private NameDictionary nameDictionary;

  @Before
  public void setUp() {
    NameDictionary.setReader(new StringReader(
        "のりみや|納又滝|はねあな|のりむね|スタンフォード|陽夏乃|なわだみなみ|しんうしごめ|ナルバンジャン|"
            + "のりむら|はっとりケイ|ストケシア|戸田ゴルフ場|はねいさ|はねいし|しのざきふみのり|"
            + "シールズフィールド|はねいち|のりもち"));

    nameDictionary = NameDictionary.getInstance("");
  }

  @Test
  public void isNameInDictionaryShouldReturnTrueIfNameIsInFile() {
    assertTrue(nameDictionary.isNameInDictionary("のりみや"));
  }

  @Test
  public void isNameInDictionaryShouldReturnFalseIfNameIsNotInFile() {
    assertFalse(nameDictionary.isNameInDictionary("竜飛人"));
  }

}
