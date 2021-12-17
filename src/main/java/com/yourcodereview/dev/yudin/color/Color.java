package com.yourcodereview.dev.yudin.color;

public enum Color {

    COLOR_BLACK("black"),
    COLOR_WHITE("white"),
    COLOR_BLACK_WHITE("black&white"),
    COLOR_RED("red"),
    COLOR_RED_WHITE("red&white"),
    COLOR_RED_BLACK_WHITE("red&black&white");

    private String color;

    public String getColor() {
        return color;
    }

    Color(String color) {
        this.color = color;
    }
}
