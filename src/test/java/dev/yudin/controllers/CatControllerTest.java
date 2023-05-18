package dev.yudin.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dev.yudin.entities.Cat;
import dev.yudin.enums.CatColor;
import dev.yudin.exceptions.ControllerException;
import dev.yudin.exceptions.ServiceException;
import dev.yudin.services.CatService;
import dev.yudin.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

class CatControllerTest {
	@InjectMocks
	private CatController controller;

	@Mock
	private Validator validator;
	@Mock
	private CatService service;

	private static final String DESC_DIRECTION = "desc";

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void showCats_ShouldReturnAllCatsSortedByName_WhenCallMethodWithoutAnyArgs() {
		List<Cat> expected = new ArrayList<>();
		expected.add(new Cat("Amur", CatColor.BLACK_AND_WHITE, 20, 11));
		expected.add(new Cat("Asya", CatColor.BLACK, 10, 10));
		expected.add(new Cat("Cesar", CatColor.BLACK_AND_WHITE, 18, 14));
		expected.add(new Cat("Chlo", CatColor.BLACK, 14, 13));
		expected.add(new Cat("Clod", CatColor.RED_AND_WHITE, 12, 15));
		expected.add(new Cat("Dina", CatColor.BLACK_AND_WHITE, 17, 12));
		expected.add(new Cat("Flora", CatColor.BLACK_AND_WHITE, 12, 15));
		expected.add(new Cat("Foma", CatColor.BLACK, 15, 18));
		expected.add(new Cat("Gass", CatColor.RED_AND_WHITE, 15, 13));
		expected.add(new Cat("Hustav", CatColor.RED_AND_WHITE, 12, 12));
		expected.add(new Cat("Kelly", CatColor.RED_AND_WHITE, 26, 11));
		expected.add(new Cat("Lesya", CatColor.BLACK_AND_WHITE, 12, 15));
		expected.add(new Cat("Marfa", CatColor.BLACK_AND_WHITE, 13, 11));
		expected.add(new Cat("Nemo", CatColor.RED_AND_WHITE, 17, 13));
		expected.add(new Cat("Neo", CatColor.RED, 11, 13));
		expected.add(new Cat("Nord", CatColor.RED_AND_BLACK_AND_WHITE, 19, 12));
		expected.add(new Cat("Odett", CatColor.RED_AND_WHITE, 17, 13));
		expected.add(new Cat("Ost", CatColor.WHITE, 14, 12));
		expected.add(new Cat("Sam", CatColor.BLACK_AND_WHITE, 15, 15));
		expected.add(new Cat("Shurik", CatColor.RED_AND_WHITE, 17, 13));
		expected.add(new Cat("Snow", CatColor.WHITE, 19, 14));
		expected.add(new Cat("Tara", CatColor.RED_AND_WHITE, 17, 12));
		expected.add(new Cat("Tayson", CatColor.RED_AND_WHITE, 18, 13));
		expected.add(new Cat("Tihon", CatColor.RED_AND_WHITE, 15, 12));
		expected.add(new Cat("Ula", CatColor.RED_AND_WHITE, 16, 14));
		expected.add(new Cat("Vika", CatColor.BLACK, 14, 10));
		expected.add(new Cat("Yasha", CatColor.RED_AND_WHITE, 18, 12));

		when(service.findAll(isA(Pageable.class))).thenReturn(expected);
		when(service.getMaxSize()).thenReturn(27);

		Integer defaultPageValue = 1;
		Integer size = null;
		String attr = null;
		String order = null;

		var actual = controller.showCats(defaultPageValue, size, attr, order);

		assertIterableEquals(expected, actual);
	}

	@Test
	void showCats_ShouldReturnListCatsSortedByTailLengthWihDescendingOrder_WhenInputIsAttributeNameIsTailLengthAndSortOrderDescAndSizeIsThree() {
		List<Cat> expected = new ArrayList<>();
		expected.add(new Cat("Kelly", CatColor.RED_AND_WHITE, 26, 11));
		expected.add(new Cat("Amur", CatColor.BLACK_AND_WHITE, 20, 11));
		expected.add(new Cat("Nord", CatColor.RED_AND_BLACK_AND_WHITE, 19, 12));

		Integer page = 1;
		Integer size = 3;
		String attr = "tail_length";
		String order = DESC_DIRECTION;

		when(service.findAll(isA(Pageable.class))).thenReturn(expected);

		var actual = controller.showCats(page, size, attr, order);

		assertIterableEquals(expected, actual);
	}

	@Test
	void showCats_ShouldReturnListCatsSortedByName_WhenInputIsPageAndLimit() {
		List<Cat> expected = new ArrayList<>();
		expected.add(new Cat("Dina", CatColor.BLACK_AND_WHITE, 17, 12));
		expected.add(new Cat("Flora", CatColor.BLACK_AND_WHITE, 12, 15));
		expected.add(new Cat("Foma", CatColor.BLACK, 15, 18));
		expected.add(new Cat("Gass", CatColor.RED_AND_WHITE, 15, 13));
		expected.add(new Cat("Hustav", CatColor.RED_AND_WHITE, 12, 12));

		Integer page = 1;
		Integer size = 5;
		String attr = null;
		String order = null;

		doNothing().when(validator).validate("tail_length", DESC_DIRECTION, 3);
		when(service.findAll(isA(Pageable.class))).thenReturn(expected);

		var actual = controller.showCats(page, size, attr, order);

		assertIterableEquals(expected, actual);
	}

	@Test
	void showCats_ShouldThrowControllerException_WhenErrorHappenedDuringCallFindAllMethod() {
		when(service.findAll(isA(Pageable.class))).thenThrow(ServiceException.class);

		Integer page = 1;
		Integer size = 1;
		String attr = null;
		String order = null;

		assertThrows(ControllerException.class, () -> controller.showCats(page, size, attr, order));
	}
}

