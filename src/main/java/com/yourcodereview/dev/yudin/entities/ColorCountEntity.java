package com.yourcodereview.dev.yudin.entities;

import lombok.Data;

@Data
public class ColorCountEntity implements Comparable<ColorCountEntity>{

    private String color;
    private int count;

    @Override
    public int compareTo(ColorCountEntity o) {
        if (getCount() > o.getCount()) return 1;
        else if(getCount() < o.getCount()) return -1;
        else return 0;
    }
}
