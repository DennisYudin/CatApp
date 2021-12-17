package com.yourcodereview.dev.yudin.color;

import org.springframework.stereotype.Component;

@Component
public class ColorResolver implements Resolver {
    private static final String ERROR_MESSAGE = "There is no such color.";


    @Override
    public Color ifExist(String input) {

        for (Color value : Color.values()) {
            if (value.getColor().equals(input)) {

                return value;
            }
        }
        throw new IllegalArgumentException("[" + input + "] " + ERROR_MESSAGE);
    }
}
