package com.yourcodereview.dev.yudin.dao;

import com.yourcodereview.dev.yudin.configs.AppConfigTest;
import com.yourcodereview.dev.yudin.entities.Cat;
import com.yourcodereview.dev.yudin.entities.ColorCountEntity;
import com.yourcodereview.dev.yudin.exceptions.DAOException;
import com.yourcodereview.dev.yudin.mappers.CatRowMapper;
import com.yourcodereview.dev.yudin.mappers.ColorCountEntityRowMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfigTest.class)
@Sql(scripts = {
        "file:src/test/resources/createTables.sql",
        "file:src/test/resources/populateTables.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "file:src/test/resources/cleanUpTables.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WebAppConfiguration
class CatDAOImplTest {
    private static final String SQL_SELECT_COLOR_AND_COUNT = "SELECT color, COUNT (color) FROM cats GROUP BY color";
    private static final String SQL_CHECK_IF_VALUE_EXIST_IN_CATS_STAT_TABLE = "" +
            "SELECT COUNT(*) " +
            "FROM cats_stat " +
            "WHERE " +
            "tail_length_mean = ? AND tail_length_median = ? AND tail_length_mode = ? " +
            "AND whiskers_length_mean = ? AND whiskers_length_median = ? AND whiskers_length_mode = ?";
    private static final String SQL_CHECK_IF_VALUE_EXIST_IN_CATS_TABLE = "" +
            "SELECT COUNT(*) " +
            "FROM cats " +
            "WHERE " +
            "name = ? AND color = CAST(? AS cat_color) AND tail_length = ? AND whiskers_length = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CatDAO catDAO;

    @Autowired
    private ColorCountEntityRowMapper colorCountEntityRowMapper;

    @Autowired
    private CatRowMapper catRowMapper;

    @Test
    void populateCatColorsInfoTable_ShouldPopulateCatColorsInfoTable_WhenInputIsSortedData() {

        List<ColorCountEntity> colorCountEntities = catDAO.getSortedList();

        catDAO.populateCatColorsInfoTable(colorCountEntities);

        List<ColorCountEntity> expectedData = colorCountEntities;
        List<ColorCountEntity> actualData = jdbcTemplate.query(
                SQL_SELECT_COLOR_AND_COUNT,
                colorCountEntityRowMapper
        );
        int expectedSize = colorCountEntities.size();
        int actualSize = actualData.size();

        assertEquals(expectedSize, actualSize);
        assertTrue(expectedData.containsAll(actualData));
    }

    @Test
    void findAll_ShouldReturnAllCatsSortedByName_WhenInputIsPageRequestWithoutSort() {

        Pageable sortedByName = PageRequest.of(0, 27);

        List<Cat> actualCats = catDAO.findAll(sortedByName);
        List<Cat> expectedCats = new ArrayList<>();

        expectedCats.add(new Cat("Amur", "black & white", 20, 11));
        expectedCats.add(new Cat("Asya", "black", 10, 10));
        expectedCats.add(new Cat("Cesar", "black & white", 18, 14));
        expectedCats.add(new Cat("Chlo", "black", 14, 13));
        expectedCats.add(new Cat("Clod", "red & white", 12, 15));
        expectedCats.add(new Cat("Dina", "black & white", 17, 12));
        expectedCats.add(new Cat("Flora", "black & white", 12, 15));
        expectedCats.add(new Cat("Foma", "black", 15, 18));
        expectedCats.add(new Cat("Gass", "red & white", 15, 13));
        expectedCats.add(new Cat("Hustav", "red & white", 12, 12));
        expectedCats.add(new Cat("Kelly", "red & white", 26, 11));
        expectedCats.add(new Cat("Lesya", "black & white", 12, 15));
        expectedCats.add(new Cat("Marfa", "black & white", 13, 11));
        expectedCats.add(new Cat("Nemo", "red & white", 17, 13));
        expectedCats.add(new Cat("Neo", "red", 11, 13));
        expectedCats.add(new Cat("Nord", "red & black & white", 19, 12));
        expectedCats.add(new Cat("Odett", "red & white", 17, 13));
        expectedCats.add(new Cat("Ost", "white", 14, 12));
        expectedCats.add(new Cat("Sam", "black & white", 15, 15));
        expectedCats.add(new Cat("Shurik", "red & white", 17, 13));
        expectedCats.add(new Cat("Snow", "white", 19, 14));
        expectedCats.add(new Cat("Tara", "red & white", 17, 12));
        expectedCats.add(new Cat("Tayson", "red & white", 18, 13));
        expectedCats.add(new Cat("Tihon", "red & white", 15, 12));
        expectedCats.add(new Cat("Ula", "red & white", 16, 14));
        expectedCats.add(new Cat("Vika", "black", 14, 10));
        expectedCats.add(new Cat("Yasha", "red & white", 18, 12));

        int expectedSize = 27;
        int actualSize = actualCats.size();

        assertEquals(expectedSize, actualSize);

        for (int i = 0; i < actualCats.size(); i++) {
            Cat expectedCat = expectedCats.get(i);
            Cat actualCat = actualCats.get(i);

            assertEquals(expectedCat, actualCat);
        }
    }

    @Test
    void findAll_ShouldReturnFiveCatsSortedByName_WhenInputIsOffsetAndLimit() {

        Pageable sortedByName = PageRequest.of(1, 5);

        List<Cat> actualCats = catDAO.findAll(sortedByName);
        List<Cat> expectedCats = new ArrayList<>();

        expectedCats.add(new Cat("Dina", "black & white", 17, 12));
        expectedCats.add(new Cat("Flora", "black & white", 12, 15));
        expectedCats.add(new Cat("Foma", "black", 15, 18));
        expectedCats.add(new Cat("Gass", "red & white", 15, 13));
        expectedCats.add(new Cat("Hustav", "red & white", 12, 12));

        int expectedSize = 5;
        int actualSize = actualCats.size();

        assertEquals(expectedSize, actualSize);

        for (int i = 0; i < actualCats.size(); i++) {
            Cat expectedCat = expectedCats.get(i);
            Cat actualCat = actualCats.get(i);

            assertEquals(expectedCat, actualCat);
        }
    }

    @Test
    void findAll_ShouldReturnFiveCatsSortedByName_WhenInputIsPageRequestWithSizeFiveWithoutSort() {

        Pageable sortedByName = PageRequest.of(0, 5);

        List<Cat> actualCats = catDAO.findAll(sortedByName);
        List<Cat> expectedCats = new ArrayList<>();

        expectedCats.add(new Cat("Amur", "black & white", 20, 11));
        expectedCats.add(new Cat("Asya", "black", 10, 10));
        expectedCats.add(new Cat("Cesar", "black & white", 18, 14));
        expectedCats.add(new Cat("Chlo", "black", 14, 13));
        expectedCats.add(new Cat("Clod", "red & white", 12, 15));

        int expectedSize = 5;
        int actualSize = actualCats.size();

        assertEquals(expectedSize, actualSize);

        for (int i = 0; i < actualCats.size(); i++) {
            Cat expectedCat = expectedCats.get(i);
            Cat actualCat = actualCats.get(i);

            assertEquals(expectedCat, actualCat);
        }
    }

    @Test
    void findAll_ShouldReturnThreeCatsSortedByTailLength_WhenInputIsPageRequestWithSortValue() {

        Pageable sortedByTailLength = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "tail_length"));

        List<Cat> actualCats = catDAO.findAll(sortedByTailLength);
        List<Cat> expectedCats = new ArrayList<>();

        expectedCats.add(new Cat("Kelly", "red & white", 26, 11));
        expectedCats.add(new Cat("Amur", "black & white", 20, 11));
        expectedCats.add(new Cat("Nord", "red & black & white", 19, 12));

        for (int i = 0; i < actualCats.size(); i++) {
            Cat expectedCat = expectedCats.get(i);
            Cat actualCat = actualCats.get(i);

            assertEquals(expectedCat, actualCat);
        }
    }

    @Test
    void findAll_ShouldReturnAllCatsSortedByName_WhenPageIsNull() {

        Pageable page = null;

        List<Cat> actualCats = catDAO.findAll(page);
        List<Cat> expectedCats = new ArrayList<>();

        expectedCats.add(new Cat("Amur", "black & white", 20, 11));
        expectedCats.add(new Cat("Asya", "black", 10, 10));
        expectedCats.add(new Cat("Cesar", "black & white", 18, 14));
        expectedCats.add(new Cat("Chlo", "black", 14, 13));
        expectedCats.add(new Cat("Clod", "red & white", 12, 15));
        expectedCats.add(new Cat("Dina", "black & white", 17, 12));
        expectedCats.add(new Cat("Flora", "black & white", 12, 15));
        expectedCats.add(new Cat("Foma", "black", 15, 18));
        expectedCats.add(new Cat("Gass", "red & white", 15, 13));
        expectedCats.add(new Cat("Hustav", "red & white", 12, 12));
        expectedCats.add(new Cat("Kelly", "red & white", 26, 11));
        expectedCats.add(new Cat("Lesya", "black & white", 12, 15));
        expectedCats.add(new Cat("Marfa", "black & white", 13, 11));
        expectedCats.add(new Cat("Nemo", "red & white", 17, 13));
        expectedCats.add(new Cat("Neo", "red", 11, 13));
        expectedCats.add(new Cat("Nord", "red & black & white", 19, 12));
        expectedCats.add(new Cat("Odett", "red & white", 17, 13));
        expectedCats.add(new Cat("Ost", "white", 14, 12));
        expectedCats.add(new Cat("Sam", "black & white", 15, 15));
        expectedCats.add(new Cat("Shurik", "red & white", 17, 13));
        expectedCats.add(new Cat("Snow", "white", 19, 14));
        expectedCats.add(new Cat("Tara", "red & white", 17, 12));
        expectedCats.add(new Cat("Tayson", "red & white", 18, 13));
        expectedCats.add(new Cat("Tihon", "red & white", 15, 12));
        expectedCats.add(new Cat("Ula", "red & white", 16, 14));
        expectedCats.add(new Cat("Vika", "black", 14, 10));
        expectedCats.add(new Cat("Yasha", "red & white", 18, 12));

        int expectedSize = 27;
        int actualSize = actualCats.size();

        assertEquals(expectedSize, actualSize);

        for (int i = 0; i < actualCats.size(); i++) {
            Cat expectedCat = expectedCats.get(i);
            Cat actualCat = actualCats.get(i);

            assertEquals(expectedCat, actualCat);
        }
    }

    @Test
    void populateCatsStatTable_ShouldSaveCatsStatIntoTable_WhenInputIs1() {

        catDAO.populateCatsStatTable();

        double tailLengthMean = 15.67;
        double tailLengthMedian = 15.0;
        List<Integer> tailLengthMode = new ArrayList<>();
        tailLengthMode.add(17);

        double whiskersLengthMean = 12.89;
        double whiskersLengthMedian = 13.0;
        List<Integer> whiskersLengthMode = new ArrayList<>();
        whiskersLengthMode.add(12);
        whiskersLengthMode.add(13);

        int expected = 1;
        int actual = jdbcTemplate.queryForObject(
                SQL_CHECK_IF_VALUE_EXIST_IN_CATS_STAT_TABLE,
                Integer.class,
                new Object[]{
                        tailLengthMean, tailLengthMedian, createSqlArray(tailLengthMode),
                        whiskersLengthMean, whiskersLengthMedian, createSqlArray(whiskersLengthMode)}
        );
        assertEquals(expected, actual);
    }

    private java.sql.Array createSqlArray(List<Integer> list) {
        java.sql.Array intArray;
        try {
            intArray = jdbcTemplate.getDataSource().getConnection()
                    .createArrayOf("integer", list.toArray());
        } catch (SQLException ex) {
            throw new DAOException("Error during createSqlArray()");
        }
        return intArray;
    }

    @Test
    void save_ShouldSaveNewCatIntoTable_WhenInputIsNewCat() {

        Cat newCat = new Cat();
        newCat.setName("DennisYudin");
        newCat.setColor("black");
        newCat.setTailLength(18);
        newCat.setWhiskersLength(8);

        catDAO.save(newCat);

        int expected = 1;
        int actual = jdbcTemplate.queryForObject(
                SQL_CHECK_IF_VALUE_EXIST_IN_CATS_TABLE,
                Integer.class,
                new Object[]{
                        newCat.getName(),
                        newCat.getColor(),
                        newCat.getTailLength(),
                        newCat.getWhiskersLength()}
        );
        assertEquals(expected, actual);
    }

    @Test
    void save_ShouldUpdateExistedCatIntoTable_WhenInputIsExistedCat() {

        Cat updatedCat = new Cat();
        updatedCat.setName("Amur");
        updatedCat.setColor("black");
        updatedCat.setTailLength(10);
        updatedCat.setWhiskersLength(10);

        catDAO.save(updatedCat);

        int expected = 1;
        int actual = jdbcTemplate.queryForObject(
                SQL_CHECK_IF_VALUE_EXIST_IN_CATS_TABLE,
                Integer.class,
                new Object[]{
                        updatedCat.getName(),
                        updatedCat.getColor(),
                        updatedCat.getTailLength(),
                        updatedCat.getWhiskersLength()}
        );
        assertEquals(expected, actual);
    }
}

