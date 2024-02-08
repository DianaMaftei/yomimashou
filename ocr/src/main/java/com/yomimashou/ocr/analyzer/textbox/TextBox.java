package com.yomimashou.ocr.analyzer.textbox;

public class TextBox {
    private String text;
    private Box box;

    public TextBox(String text, Box box) {
        this.text = text;
        this.box = box;
    }

    public TextBox() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }
}
