package dev.yudin.dao.impl;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.yudin.configs.AppConfigTest;
import dev.yudin.dao.CatColorInfoDAO;
import dev.yudin.entities.ColorCountDTO;
import dev.yudin.enums.CatColor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfigTest.class)
@Sql(scripts = {
		"file:src/test/resources/scripts/createTables.sql",
		"file:src/test/resources/scripts/populateTables.sql"},
		executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "file:src/test/resources/scripts/cleanUpTables.sql",
		executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebAppConfiguration
class CatColorInfoDAOImplTest {
	@Autowired
	private CatColorInfoDAO catColorInfoDAO;

	@Test
	@Order(0)
	void save_ShouldSaveListOfObjectsIntoTable_WhenInputIsListOfData() {
		List<ColorCountDTO> expected = List.of(
				new ColorCountDTO(CatColor.RED, 10),
				new ColorCountDTO(CatColor.RED_AND_WHITE, 13),
				new ColorCountDTO(CatColor.WHITE, 3),
				new ColorCountDTO(CatColor.BLACK, 5)
		);
		assertTrue(catColorInfoDAO.getResult().isEmpty());

		catColorInfoDAO.save(expected);

		var actual = catColorInfoDAO.getResult();

		assertIterableEquals(expected, actual);
	}
}
