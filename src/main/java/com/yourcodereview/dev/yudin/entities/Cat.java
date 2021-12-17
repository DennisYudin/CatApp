package com.yourcodereview.dev.yudin.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cat {

    private String name;
    private String color;
    private int tailLength;
    private int whiskersLength;

    @Override
    public String toString() {
        return "Cat{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", tailLength=" + tailLength +
                ", whiskersLength=" + whiskersLength +
                '}';
    }
}
