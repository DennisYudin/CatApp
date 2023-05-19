package dev.yudin.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import dev.yudin.configs.AppConfigTest;
import dev.yudin.dao.CatDAO;
import dev.yudin.entities.Cat;
import dev.yudin.enums.CatColor;
import dev.yudin.exceptions.DAOException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

@Sql(scripts = {
		"file:src/test/resources/scripts/createTables.sql",
		"file:src/test/resources/scripts/populateTables.sql"},
		executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "file:src/test/resources/scripts/cleanUpTables.sql",
		executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfigTest.class)
@WebAppConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CatDAOImplTest {
	@Autowired
	private CatDAO catDAO;

	@Test
	@Order(0)
	void isExist_ShouldReturnTrue_WhenInputIsExistNameInTable() {
		boolean actual = catDAO.isExist("Amur");

		assertTrue(actual);
	}

	@Test
	@Order(1)
	void isExist_ShouldReturnTrue_WhenInputIsDoesNotExistName() {
		boolean actual = catDAO.isExist("doesNotExistName");

		assertFalse(actual);
	}

	@Test
	void update_ShouldUpdateExistObject_WhenInputIsUpdatedObject() {
		var updatedCat = new Cat("Amur", CatColor.WHITE, 5, 5);

		assumeTrue(catDAO.isExist("Amur"));

		catDAO.update(updatedCat);

		var actual = catDAO.getBy("Amur");

		assertEquals(updatedCat, actual);
	}

	@Test
	void update_ShouldThrowDAOException_WhenTryUpdateCatWithNotValidAttribute() {
		var invalidCat = new Cat("Amur", CatColor.UNKNOWN, 5, 5);

		assertThrows(DAOException.class, () -> catDAO.update(invalidCat));
	}

	@Test
	void update_ShouldThrowExcp2() {
		var doesNotExistCat = new Cat("doesNotExistCat", CatColor.WHITE, 5, 5);

		assertThrows(HibernateOptimisticLockingFailureException.class, () -> catDAO.update(doesNotExistCat));
	}

	@Test
	@Order(4)
	void delete_ShouldDeleteObject_WhenInputIsCatName() {
		assumeTrue(catDAO.isExist("Amur"));

		catDAO.delete("Amur");

		assertFalse(catDAO.isExist("Amur"));
	}

	@Test
	@Order(2)
	void getByName_ShouldReturnCat_WhenInputIsName() {
		var actual = catDAO.getBy("Amur");
		var expected = new Cat("Amur", CatColor.BLACK_AND_WHITE, 20, 11);

		assertEquals(expected, actual);
	}

	@Test
	@Order(3)
	void getByName_ShouldReturnNull_WhenInputIsDoesNotExistName() {
		var actual = catDAO.getBy("xxx");

		assertNull(actual);
	}

	@Test
	void countAllRows_ShouldCountAllElementsInTable_WhenCallMethod() {
		int actual = catDAO.countAllRows();
		int expected = 27;

		assertEquals(expected, actual);
	}

	@Test
	void findAll_ShouldReturnAllCatsSortedByName_WhenInputIsPageRequestWithoutSort() {

		Pageable sortedByName = PageRequest.of(0, 27);

		List<Cat> actualCats = catDAO.findAll(sortedByName);
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

		assertIterableEquals(expectedCats, actualCats);
	}

	@Test
	void findAll_ShouldReturnFiveCatsSortedByName_WhenInputIsOffsetAndLimit() {

		Pageable sortedByName = PageRequest.of(1, 5);

		List<Cat> actualCats = catDAO.findAll(sortedByName);
		List<Cat> expectedCats = new ArrayList<>();

		expectedCats.add(new Cat("Dina", CatColor.BLACK_AND_WHITE, 17, 12));
		expectedCats.add(new Cat("Flora", CatColor.BLACK_AND_WHITE, 12, 15));
		expectedCats.add(new Cat("Foma", CatColor.BLACK, 15, 18));
		expectedCats.add(new Cat("Gass", CatColor.RED_AND_WHITE, 15, 13));
		expectedCats.add(new Cat("Hustav", CatColor.RED_AND_WHITE, 12, 12));

		assertIterableEquals(expectedCats, actualCats);
	}

	@Test
	void findAll_ShouldReturnFiveCatsSortedByName_WhenInputIsPageRequestWithSizeFiveWithoutSort() {

		Pageable sortedByName = PageRequest.of(0, 5);

		List<Cat> actualCats = catDAO.findAll(sortedByName);
		List<Cat> expectedCats = new ArrayList<>();

		expectedCats.add(new Cat("Amur", CatColor.BLACK_AND_WHITE, 20, 11));
		expectedCats.add(new Cat("Asya", CatColor.BLACK, 10, 10));
		expectedCats.add(new Cat("Cesar", CatColor.BLACK_AND_WHITE, 18, 14));
		expectedCats.add(new Cat("Chlo", CatColor.BLACK, 14, 13));
		expectedCats.add(new Cat("Clod", CatColor.RED_AND_WHITE, 12, 15));

		assertIterableEquals(expectedCats, actualCats);
	}

	@Test
	void findAll_ShouldReturnThreeCatsSortedByTailLength_WhenInputIsPageRequestWithSortValue() {

		Pageable sortedByTailLength = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "tail_length"));

		List<Cat> actual = catDAO.findAll(sortedByTailLength);
		List<Cat> expected = new ArrayList<>();

		expected.add(new Cat("Kelly", CatColor.RED_AND_WHITE, 26, 11));
		expected.add(new Cat("Amur", CatColor.BLACK_AND_WHITE, 20, 11));

		assertIterableEquals(expected, actual);
	}

	@Test
	void findAll_ShouldReturnAllCatsSortedByName_WhenPageIsNull() {

		Pageable page = null;

		List<Cat> actualCats = catDAO.findAll(page);
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

		assertIterableEquals(expectedCats, actualCats);
	}

	@Test
	@Order(5)
	void save_ShouldSaveNewCat_WhenInputIsNewCat() {
		Cat newCat = new Cat();
		newCat.setName("DennisYudin");
		newCat.setColor("black");
		newCat.setTailLength(18);
		newCat.setWhiskersLength(8);

		assumeFalse(catDAO.isExist("DennisYudin"));

		catDAO.save(newCat);

		assertTrue(catDAO.isExist("DennisYudin"));
	}

	@Test
	@Order(6)
	void save_ShouldThrowDAOException_WhenInputIsCatWithNotValidData() {
		Cat badCat = new Cat("Amur", CatColor.UNKNOWN, 20, 11);

		assertThrows(DAOException.class, () -> catDAO.save(badCat));
	}
}

