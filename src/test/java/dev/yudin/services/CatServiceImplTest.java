package dev.yudin.services;

import dev.yudin.dao.CatColorInfoDAO;
import dev.yudin.dao.CatDAO;
import dev.yudin.dao.CatStatsDAO;
import dev.yudin.entities.Cat;
import dev.yudin.entities.CatStatsDTO;
import dev.yudin.entities.ColorCountDTO;
import dev.yudin.enums.CatColor;
import dev.yudin.services.impl.CatServiceImpl;
import dev.yudin.stats_calculator.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CatServiceImplTest {
	@InjectMocks
	private CatServiceImpl catService;

	@Mock
	private CatDAO catDAO;
	@Mock
	private Calculator mockedCalculator;
	@Mock
	private CatStatsDAO catStatsDAO;
	@Mock
	private CatColorInfoDAO catColorInfoDAO;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void showStats_ShouldGetAllCatsAndCalculateStatsAndSaveAndReturnResult_WhenCallMethod() {
		List<Cat> expectedCats = new ArrayList<>();
		expectedCats.add(new Cat("Amur", CatColor.BLACK_AND_WHITE, 20, 11));
		expectedCats.add(new Cat("Asya", CatColor.BLACK, 10, 10));
		expectedCats.add(new Cat("Cesar", CatColor.BLACK_AND_WHITE, 18, 14));
		expectedCats.add(new Cat("Chlo", CatColor.BLACK, 14, 13));
		expectedCats.add(new Cat("Clod", CatColor.RED_AND_WHITE, 12, 15));
		expectedCats.add(new Cat("Dina", CatColor.BLACK_AND_WHITE, 17, 12));
		expectedCats.add(new Cat("Flora", CatColor.BLACK_AND_WHITE, 12, 15));
		expectedCats.add(new Cat("Foma", CatColor.BLACK, 15, 18));
		expectedCats.add(new Cat("Gass", CatColor.RED_AND_WHITE, 15, 13));
		expectedCats.add(new Cat("Hustav", CatColor.RED_AND_WHITE, 12, 12));
		expectedCats.add(new Cat("Kelly", CatColor.RED_AND_WHITE, 26, 11));
		expectedCats.add(new Cat("Lesya", CatColor.BLACK_AND_WHITE, 12, 15));
		expectedCats.add(new Cat("Marfa", CatColor.BLACK_AND_WHITE, 13, 11));
		expectedCats.add(new Cat("Nemo", CatColor.RED_AND_WHITE, 17, 13));
		expectedCats.add(new Cat("Neo", CatColor.RED, 11, 13));
		expectedCats.add(new Cat("Nord", CatColor.RED_AND_BLACK_AND_WHITE, 19, 12));
		expectedCats.add(new Cat("Odett", CatColor.RED_AND_WHITE, 17, 13));
		expectedCats.add(new Cat("Ost", CatColor.WHITE, 14, 12));
		expectedCats.add(new Cat("Sam", CatColor.BLACK_AND_WHITE, 15, 15));
		expectedCats.add(new Cat("Shurik", CatColor.RED_AND_WHITE, 17, 13));
		expectedCats.add(new Cat("Snow", CatColor.WHITE, 19, 14));
		expectedCats.add(new Cat("Tara", CatColor.RED_AND_WHITE, 17, 12));
		expectedCats.add(new Cat("Tayson", CatColor.RED_AND_WHITE, 18, 13));
		expectedCats.add(new Cat("Tihon", CatColor.RED_AND_WHITE, 15, 12));
		expectedCats.add(new Cat("Ula", CatColor.RED_AND_WHITE, 16, 14));
		expectedCats.add(new Cat("Vika", CatColor.BLACK, 14, 10));
		expectedCats.add(new Cat("Yasha", CatColor.RED_AND_WHITE, 18, 12));

		when(catDAO.findAll(PageRequest.of(0, 27))).thenReturn(expectedCats);

		when(mockedCalculator.calculateMean(isA(List.class))).thenReturn(15.6).thenReturn(12.8);
		when(mockedCalculator.calculateMode(isA(List.class))).thenReturn(new int[]{17}).thenReturn(new int[]{12, 13});
		when(mockedCalculator.calculateMedian(isA(List.class))).thenReturn(15.0).thenReturn(13.0);

		when(catStatsDAO.isEmpty()).thenReturn(true);

		var expected = new CatStatsDTO(
				15.6, 15.0, new int[]{17},
				12.8, 13.0, new int[]{12, 13});
		when(catStatsDAO.read()).thenReturn(expected);

		catService.showStats();

		var captor = ArgumentCaptor.forClass(CatStatsDTO.class);
		verify(catStatsDAO, times(1)).save(captor.capture());
		var actualCapturedValue = captor.getValue();

		assertEquals(expected, actualCapturedValue);
	}

	@Test
	void groupColorInfo_ShouldGroupColorAndCount_WhenCallMethod() {
		List<Cat> expectedCats = new ArrayList<>();
		expectedCats.add(new Cat("Amur", CatColor.BLACK_AND_WHITE, 20, 11));
		expectedCats.add(new Cat("Asya", CatColor.BLACK, 10, 10));
		expectedCats.add(new Cat("Cesar", CatColor.BLACK_AND_WHITE, 18, 14));
		expectedCats.add(new Cat("Chlo", CatColor.BLACK, 14, 13));
		expectedCats.add(new Cat("Clod", CatColor.RED_AND_WHITE, 12, 15));
		expectedCats.add(new Cat("Dina", CatColor.BLACK_AND_WHITE, 17, 12));
		expectedCats.add(new Cat("Flora", CatColor.BLACK_AND_WHITE, 12, 15));
		expectedCats.add(new Cat("Foma", CatColor.BLACK, 15, 18));
		expectedCats.add(new Cat("Gass", CatColor.RED_AND_WHITE, 15, 13));
		expectedCats.add(new Cat("Hustav", CatColor.RED_AND_WHITE, 12, 12));
		expectedCats.add(new Cat("Kelly", CatColor.RED_AND_WHITE, 26, 11));
		expectedCats.add(new Cat("Lesya", CatColor.BLACK_AND_WHITE, 12, 15));
		expectedCats.add(new Cat("Marfa", CatColor.BLACK_AND_WHITE, 13, 11));
		expectedCats.add(new Cat("Nemo", CatColor.RED_AND_WHITE, 17, 13));
		expectedCats.add(new Cat("Neo", CatColor.RED, 11, 13));
		expectedCats.add(new Cat("Nord", CatColor.RED_AND_BLACK_AND_WHITE, 19, 12));
		expectedCats.add(new Cat("Odett", CatColor.RED_AND_WHITE, 17, 13));
		expectedCats.add(new Cat("Ost", CatColor.WHITE, 14, 12));
		expectedCats.add(new Cat("Sam", CatColor.BLACK_AND_WHITE, 15, 15));
		expectedCats.add(new Cat("Shurik", CatColor.RED_AND_WHITE, 17, 13));
		expectedCats.add(new Cat("Snow", CatColor.WHITE, 19, 14));
		expectedCats.add(new Cat("Tara", CatColor.RED_AND_WHITE, 17, 12));
		expectedCats.add(new Cat("Tayson", CatColor.RED_AND_WHITE, 18, 13));
		expectedCats.add(new Cat("Tihon", CatColor.RED_AND_WHITE, 15, 12));
		expectedCats.add(new Cat("Ula", CatColor.RED_AND_WHITE, 16, 14));
		expectedCats.add(new Cat("Vika", CatColor.BLACK, 14, 10));
		expectedCats.add(new Cat("Yasha", CatColor.RED_AND_WHITE, 18, 12));

		when(catDAO.findAll(PageRequest.of(0, 27))).thenReturn(expectedCats);
		when(catColorInfoDAO.getResult()).thenReturn(Collections.emptyList());

		catService.groupColorInfo();

		ArgumentCaptor<List<ColorCountDTO>> captor = ArgumentCaptor.forClass(List.class);
		verify(catColorInfoDAO, times(1)).save(captor.capture());
		var actualCapturedValue = captor.getValue();

		var expected = List.of(
				new ColorCountDTO(CatColor.RED, 1),
				new ColorCountDTO(CatColor.BLACK_AND_WHITE, 7),
				new ColorCountDTO(CatColor.WHITE, 2),
				new ColorCountDTO(CatColor.RED_AND_WHITE, 12),
				new ColorCountDTO(CatColor.RED_AND_BLACK_AND_WHITE, 1),
				new ColorCountDTO(CatColor.BLACK, 4)
		);
		assertIterableEquals(expected, actualCapturedValue);
	}

	@Test
	void findAll_ShouldReturnFiveCatsSortedByName_WhenInputIsOffsetAndLimit() {

		Pageable sortedByName = PageRequest.of(1, 5);

		List<Cat> expectedCats = new ArrayList<>();
        expectedCats.add(new Cat("Dina", CatColor.BLACK_AND_WHITE, 17, 12));
        expectedCats.add(new Cat("Flora",  CatColor.BLACK_AND_WHITE, 12, 15));
        expectedCats.add(new Cat("Foma", CatColor.BLACK, 15, 18));
        expectedCats.add(new Cat("Gass", CatColor.RED_AND_WHITE, 15, 13));
        expectedCats.add(new Cat("Hustav", CatColor.RED_AND_WHITE, 12, 12));

		when(catDAO.findAll(sortedByName)).thenReturn(expectedCats);

		List<Cat> actualCats = catService.findAll(sortedByName);

		assertIterableEquals(expectedCats, actualCats);
	}


	@Test
	void save_ShouldSaveNewCatIntoTable_WhenInputIsNewCat() {
		Cat newCat = new Cat();
		newCat.setName("DennisYudin");
        newCat.setColor(CatColor.BLACK.getColor());
		newCat.setTailLength(18);
		newCat.setWhiskersLength(8);

		catService.save(newCat);

		verify(catDAO, times(1)).save(newCat);
	}

	@Test
	void update_ShouldUpdateExistedCatIntoTable_WhenInputIsExistedCat() {
		Cat updatedCat = new Cat();
		updatedCat.setName("Amur");
        updatedCat.setColor(CatColor.BLACK.getColor());
		updatedCat.setTailLength(10);
		updatedCat.setWhiskersLength(10);

		catService.save(updatedCat);

		verify(catDAO, times(1)).save(updatedCat);
	}

	@Test
	void delete_ShouldDeleteExistedCat_WhenInputIsCatName() {
		Cat existedCat = new Cat();
		existedCat.setName("Amur");
		existedCat.setColor(CatColor.BLACK_AND_WHITE.getColor());
		existedCat.setTailLength(20);
		existedCat.setWhiskersLength(11);

		catService.delete(existedCat.getName());

		verify(catDAO, times(1)).delete(existedCat.getName());
	}
}
