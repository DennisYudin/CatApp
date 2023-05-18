package dev.yudin.stats_calculator;

import java.util.List;

public interface Calculator {

	double calculateMean(List<Integer> numbers);

	double calculateMedian(List<Integer> numbers);

	int[] calculateMode(List<Integer> numbers);
}
