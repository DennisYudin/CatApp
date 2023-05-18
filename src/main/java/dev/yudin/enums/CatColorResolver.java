package dev.yudin.enums;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CatColorResolver {
    private static final String ERROR_MESSAGE = "There is no such color.";

    public static CatColor convertToEntityAttribute(String actualColor) {
        return Arrays.stream(CatColor.values())
                .filter(catColor -> catColor.getColor().equals(actualColor))
                .findFirst()
                .orElseThrow(()-> new IllegalArgumentException(" [" + actualColor + "] " + ERROR_MESSAGE));
    }
}
