package dev.yudin.enums;

public enum CatColor {
    BLACK("black"),
    WHITE("white"),
    BLACK_AND_WHITE("black & white"),
    RED("red"),
    RED_AND_WHITE("red & white"),
    RED_AND_BLACK_AND_WHITE("red & black & white"),
    UNKNOWN("UNKNOWN")
    ;

    private String color;

    CatColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
