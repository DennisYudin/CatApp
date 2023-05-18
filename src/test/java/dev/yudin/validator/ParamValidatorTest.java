package dev.yudin.validator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import dev.yudin.entities.Cat;
import dev.yudin.enums.CatColor;
import dev.yudin.services.CatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ParamValidatorTest {
	@InjectMocks
	private ParamValidator validator;

	@Mock
	private CatService catService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void validate_ShouldThrowIAE_WhenNameIsIncorrect() {
		when(catService.getBy("notValidName")).thenThrow(IllegalArgumentException.class);

		assertThrows(IllegalArgumentException.class, () -> validator.validate("notValidName"));
	}

	@Test
	void validate_ShouldThrowIAE_WhenAttributeIsIncorrect() {
		String attr = "notValidAttribute";
		String order = null;
		Integer pageSize = null;

		assertThrows(IllegalArgumentException.class, () -> validator.validate(attr, order, pageSize));
	}

	@Test
	void validate_ShouldThrowIAE_WhenSortOrderIsIncorrect() {
		String attr = "name";
		String order = "notValidSortOrder";
		Integer pageSize = null;

		assertThrows(IllegalArgumentException.class, () -> validator.validate(attr, order, pageSize));
	}

	@Test
	void validate_ShouldThrowIAE_WhenSortOrderIsIncorrect2() {
		when(catService.getMaxSize()).thenReturn(27);

		String attr = "name";
		String order = "desc";
		Integer pageSize = 35;

		assertThrows(IllegalArgumentException.class, () -> validator.validate(attr, order, pageSize));
	}

	@Test
	void validate_ShouldThrowIAE_WhenNameIsNull() {
		Cat newCat = new Cat(null, CatColor.BLACK, 15, 15);

		assertThrows(IllegalArgumentException.class, () -> validator.validate(newCat));
	}

	@Test
	void validate_ShouldThrowIAE_WhenTailOrWhiskersLengthIsLessOrEqualsZero() {
		Cat cat = new Cat("Dennis", CatColor.BLACK, 0, 15);

		assertThrows(IllegalArgumentException.class, () -> validator.validate(cat));
	}

	@Test
	void validate_ShouldThrowIAE_WhenNameIsAlreadyExistInDB() {
		when(catService.getBy("Dennis")).thenReturn(new Cat("Dennis", CatColor.BLACK, 13, 18));

		Cat cat = new Cat("Dennis", CatColor.BLACK, 3, 15);

		assertThrows(IllegalArgumentException.class, () -> validator.validate(cat));
	}
}
