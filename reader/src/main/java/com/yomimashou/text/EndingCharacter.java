package com.yomimashou.text;

public enum EndingCharacter {
  FULL_STOP_V1("。"),
  FULL_STOP_V2("."),
  COMMA_V1("、"),
  COMMA_V2(","),
  EXCLAMATION_MARK_V1("！"),
  EXCLAMATION_MARK_V2("!"),
  QUESTION_MARK_V1("？"),
  QUESTION_MARK_V2("?"),
  LEFT_PARAN_V1("（"),
  LEFT_PARAN_V2("("),
  RIGHT_PARAN_V2("("),
  RIGHT_PARAN_V1("）"),
  START_QUOTE("「"),
  END_QUOTE("」"),
  APOSTROPHE_V1("'"),
  APOSTROPHE_V2("’"),
  LEFT_SQUARE_BRACKET("["),
  RIGHT_SQUARE_BRACKET("]"),
  LEFT_CURLY_BRACE("{"),
  RIGHT_CURLY_BRACE("}"),
  LEFT_ANGLED_BRACKET("⟨"),
  RIGHT_ANGLED_BRACKET("⟩"),
  COLON(":"),
  SEMI_COLON(";");

  private final String symbol;

  EndingCharacter(String symbol) {
    this.symbol = symbol;
  }

  @Override
  public String toString() {
    return symbol;
  }
}
