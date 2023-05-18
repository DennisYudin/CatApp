package dev.yudin.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import dev.yudin.configs.AppConfigTest;
import dev.yudin.dao.CatStatsDAO;
import dev.yudin.entities.CatStatsDTO;
import dev.yudin.exceptions.DAOException;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfigTest.class)
@Sql(scripts = {
		"file:src/test/resources/scripts/createTables.sql",
		"file:src/test/resources/scripts/populateTables.sql"},
		executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "file:src/test/resources/scripts/cleanUpTables.sql",
		executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WebAppConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CatStatsDAOImplTest {
	@Autowired
	private CatStatsDAO statsDAO;

	@Test
	@Order(0)
	void isEmpty_ShouldReturnTrue_WhenTableIsEmpty() {
		var actual = statsDAO.isEmpty();

		assertTrue(actual);
	}

	@Test
	@Order(3)
	void read_ShouldThrowDAOException_WhenThereIsNoValueInTable() {
		assertThrows(DAOException.class, () -> statsDAO.read());
	}

	@Test
	@Order(1)
	void save_ShouldSaveStatsObjectIntoTable_WhenInputIsObject() {
		CatStatsDTO statsDTO = new CatStatsDTO(
				14.0, 14.0, new int[]{13, 15},
				11.5, 11.5, new int[]{11, 12});

		assumeTrue(statsDAO.isEmpty());

		statsDAO.save(statsDTO);

		assertFalse(statsDAO.isEmpty());
	}
}
