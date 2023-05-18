package dev.yudin.stats_calculator;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Log4j
@Component
public class StatsCalculator implements Calculator {
	public static final double DEFAULT_VALUE_MEAN = -0.0;
	public static final int DEFAULT_VALUE_MODE = -1;

	@Override
	public double calculateMean(List<Integer> numbers) {
		OptionalDouble mean = numbers.stream()
				.mapToDouble(Integer::doubleValue)
				.average();
		return mean.isPresent() ? mean.getAsDouble() : DEFAULT_VALUE_MEAN;
	}

	@Override
	public double calculateMedian(List<Integer> numbers) {
		Collections.sort(numbers);

		double median;
		int amountElements = numbers.size();
		if (amountElements % 2 == 0) {
			int firstIndex = (amountElements / 2) - 1;
			int secondIndex = amountElements / 2;

			double firstValue = numbers.get(firstIndex);
			double secondValues = numbers.get(secondIndex);

			median = (firstValue + secondValues) / 2;
		} else {
			int middleIndex = amountElements / 2;
			median = numbers.get(middleIndex);
		}
		return median;
	}

	@Override
	public int[] calculateMode(List<Integer> numbers) {
		Map<Integer, Long> frequencies = numbers.stream()
				.collect(Collectors.groupingBy(Integer::intValue, Collectors.counting()));

		long maxNumber = frequencies.values().stream()
				.mapToLong(Long::longValue)
				.max()
				.orElse(DEFAULT_VALUE_MODE);

		return frequencies.entrySet().stream()
				.filter(elem -> elem.getValue() == maxNumber)
				.map(Map.Entry::getKey)
				.mapToInt(Integer::intValue)
				.toArray();
	}
}
