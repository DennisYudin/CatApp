package com.yourcodereview.dev.yudin.entities;

import lombok.Data;

import java.util.List;

@Data
public class CatStatsDTO {

    private double tailLengthMean;
    private double tailLengthMedian;
    private List<Integer> tailLengthMode;

    private double whiskersLengthMean;
    private double whiskersLengthMedian;
    private List<Integer> whiskersLengthMode;
}
