package dev.yudin.stats_calculator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.yudin.entities.Cat;
import dev.yudin.enums.CatColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class StatsCalculatorTest {
	private final StatsCalculator calculator = new StatsCalculator();
	private final List<Cat> cats = new ArrayList<>();

	@BeforeEach
	public void setUp() {
		cats.add(new Cat("Amur", CatColor.BLACK_AND_WHITE, 20, 11));
		cats.add(new Cat("Asya", CatColor.BLACK, 10, 10));
		cats.add(new Cat("Cesar", CatColor.BLACK_AND_WHITE, 18, 14));
		cats.add(new Cat("Chlo", CatColor.BLACK, 14, 13));
		cats.add(new Cat("Clod", CatColor.RED_AND_WHITE, 12, 15));
		cats.add(new Cat("Dina", CatColor.BLACK_AND_WHITE, 17, 12));
		cats.add(new Cat("Flora", CatColor.BLACK_AND_WHITE, 12, 15));
		cats.add(new Cat("Foma", CatColor.BLACK, 15, 18));
		cats.add(new Cat("Gass", CatColor.RED_AND_WHITE, 15, 13));
		cats.add(new Cat("Hustav", CatColor.RED_AND_WHITE, 12, 12));
		cats.add(new Cat("Kelly", CatColor.RED_AND_WHITE, 26, 11));
		cats.add(new Cat("Lesya", CatColor.BLACK_AND_WHITE, 12, 15));
		cats.add(new Cat("Marfa", CatColor.BLACK_AND_WHITE, 13, 11));
		cats.add(new Cat("Nemo", CatColor.RED_AND_WHITE, 17, 13));
		cats.add(new Cat("Neo", CatColor.RED, 11, 13));
		cats.add(new Cat("Nord", CatColor.RED_AND_BLACK_AND_WHITE, 19, 12));
		cats.add(new Cat("Odett", CatColor.RED_AND_WHITE, 17, 13));
		cats.add(new Cat("Ost", CatColor.WHITE, 14, 12));
		cats.add(new Cat("Sam", CatColor.BLACK_AND_WHITE, 15, 15));
		cats.add(new Cat("Shurik", CatColor.RED_AND_WHITE, 17, 13));
		cats.add(new Cat("Snow", CatColor.WHITE, 19, 14));
		cats.add(new Cat("Tara", CatColor.RED_AND_WHITE, 17, 12));
		cats.add(new Cat("Tayson", CatColor.RED_AND_WHITE, 18, 13));
		cats.add(new Cat("Tihon", CatColor.RED_AND_WHITE, 15, 12));
		cats.add(new Cat("Ula", CatColor.RED_AND_WHITE, 16, 14));
		cats.add(new Cat("Vika", CatColor.BLACK, 14, 10));
		cats.add(new Cat("Yasha", CatColor.RED_AND_WHITE, 18, 12));
	}

	@Test
	void calculateMean_ShouldCalculateMean_WhenInputIsListOfNumbers() {
		List<Integer> tailLengths = cats.stream()
				.map(Cat::getTailLength)
				.collect(Collectors.toList());

		var actual = calculator.calculateMean(tailLengths);
		double expected = 15.666666666666666;

		assertEquals(expected, actual);
	}

	@Test
	void calculateMode_ShouldFindMostFrequentNumberInTheList_WhenInputIsListOfNumbers() {
		List<Integer> input = cats.stream()
				.map(Cat::getTailLength)
				.collect(Collectors.toList());

		var actual = calculator.calculateMode(input);
		var expected = new int[]{17};

		assertArrayEquals(expected, actual);
	}

	@Test
	void calculateMode_ShouldAllFindMostFrequentNumberInTheList_WhenInputIsListOfNumbers() {
		List<Integer> input = List.of(1, 1, 2, 3, 5, 5);

		var actual = calculator.calculateMode(input);
		var expected = new int[] {1, 5};

		assertArrayEquals(expected, actual);
	}

	@Test
	void calculateMedian_ShouldFindMedian_WhenInputIsUnsortedListWithNotEvenNumberOfElements() {
		List<Integer> input = new ArrayList<>(List.of(3, 1, 2));

		var actual = calculator.calculateMedian(input);
		double expected = 2.0;

		assertEquals(expected, actual);
	}
	@Test
	void calculateMedian_ShouldFindMedian_WhenInputIsUnsortedListWithEvenNumberOfElements() {
		List<Integer> input = new ArrayList<>(List.of(4, 10, 5, 2));

		var actual = calculator.calculateMedian(input);
		double expected = 4.5;

		assertEquals(expected, actual);
	}
}
