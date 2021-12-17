package com.yourcodereview.dev.yudin.dao.impl;

import com.yourcodereview.dev.yudin.dao.CatDAO;
import com.yourcodereview.dev.yudin.entities.Cat;
import com.yourcodereview.dev.yudin.entities.CatStatsDTO;
import com.yourcodereview.dev.yudin.entities.ColorCountEntity;
import com.yourcodereview.dev.yudin.exceptions.DAOException;
import com.yourcodereview.dev.yudin.mappers.CatRowMapper;
import com.yourcodereview.dev.yudin.mappers.ColorCountEntityRowMapper;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.*;

@Log4j
@Repository("catDAO")
public class CatDAOImpl implements CatDAO {
    private static final String SQL_SELECT_COLOR_AND_COUNT = "SELECT color, COUNT (color) FROM cats GROUP BY color";
    private static final String SQL_SAVE_COLOR_AND_COUNT = "" +
            "INSERT INTO " +
            "cat_colors_info (color, count) " +
            "VALUES(CAST(? AS cat_color), ?)";
    private static final String SQL_SAVE_CATS_STATS = "" +
            "INSERT INTO cats_stat " +
            "(tail_length_mean, tail_length_median, tail_length_mode, whiskers_length_mean, whiskers_length_median, whiskers_length_mode) " +
            "VALUES(?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL_CATS_ORDER_BY_NAME = "SELECT * FROM cats ORDER BY name";
    private static final String SQL_SELECT_ALL_CATS_ORDER_BY = "SELECT * FROM cats ORDER BY";
    private static final String SQL_SAVE_NEW_CAT = "" +
            "INSERT INTO " +
            "cats (name, color, tail_length, whiskers_length) " +
            "VALUES (?, CAST(? AS cat_color), ?, ?)";
    private static final String SQL_CHECK_IF_VALUE_EXIST_IN_CATS_TABLE = "" +
            "SELECT COUNT(*) " +
            "FROM cats " +
            "WHERE " +
            "name = ?";
    private static final String SQL_UPDATE_EXISTED_CAT = "" +
            "UPDATE cats " +
            "SET color = CAST(? AS cat_color), tail_length = ?, whiskers_length = ?" +
            "WHERE name = ?";

    private static final String DEFAULT_SORT_BY_COLUMN_NAME = "name";
    private static final String SORT_BY_COLUMN_TAIL_LENGTH = "tail_length";
    private static final String SORT_BY_COLUMN_WHISKERS_LENGTH = "whiskers_length";

    private static final String ERROR_MESSAGE_FOR_FIND_ALL_METHOD = "Error during call the method findAll()";
    private static final String ERROR_MESSAGE_FOR_POPULATE_METHOD = "Error during call the method populateCatColorsInfo()";
    private static final String ERROR_MESSAGE_FOR_GET_SORTED_LIST_METHOD = "Error during call the method getSortedList()";
    private static final String ERROR_MESSAGE_FOR_CREATE_SQL_ARRAY_METHOD = "Error during call the method createSqlArray()";
    private static final String ERROR_MESSAGE_FOR_SAVE_NEW_CAT_METHOD = "Error during call the method saveNewCat()";
    private static final String ERROR_MESSAGE_CHECK_IF_EXIST_METHOD = "Error during call the method checkIfExist()";
    private static final String ERROR_MESSAGE_FOR_UPDATE_CAT_METHOD = "Error during call the method updateCat()";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ColorCountEntityRowMapper colorCountEntityRowMapper;

    @Autowired
    private CatRowMapper catRowMapper;

    @Override
    public List<ColorCountEntity> getSortedList() {
        log.debug("Call method getSortedList()");

        try {
            List<ColorCountEntity> colorCountEntities = jdbcTemplate.query(
                    SQL_SELECT_COLOR_AND_COUNT,
                    colorCountEntityRowMapper
            );
            Collections.sort(colorCountEntities);

            if (log.isDebugEnabled()) {
                log.debug("ColorCountEntities are " + colorCountEntities);
            }
            return colorCountEntities;
        } catch (DataAccessException ex) {
            log.error(ERROR_MESSAGE_FOR_GET_SORTED_LIST_METHOD, ex);
            throw new DAOException(ERROR_MESSAGE_FOR_GET_SORTED_LIST_METHOD, ex);
        }
    }

    @Override
    public void populateCatColorsInfoTable(List<ColorCountEntity> input) {
        log.debug("Call method populateCatColorsInfoTable()");

        try {
            for (ColorCountEntity colorCountEntity : input) {
                String color = colorCountEntity.getColor();
                int count = colorCountEntity.getCount();

                jdbcTemplate.update(
                        SQL_SAVE_COLOR_AND_COUNT,
                        color, count
                );
            }
        } catch (DataAccessException ex) {
            log.error(ERROR_MESSAGE_FOR_POPULATE_METHOD, ex);
            throw new DAOException(ERROR_MESSAGE_FOR_POPULATE_METHOD, ex);
        }
    }

    @Override
    public List<Cat> findAll(Pageable page) {
        log.debug("Call method findAll()");

        String sqlQuery = buildSqlQuery(page);

        try {
            List<Cat> cats = jdbcTemplate.query(
                    sqlQuery,
                    catRowMapper
            );
            if (log.isDebugEnabled()) {
                log.debug("Cats: " + cats);
            }
            return cats;
        } catch (DataAccessException ex) {
            log.error(ERROR_MESSAGE_FOR_FIND_ALL_METHOD, ex);
            throw new DAOException(ERROR_MESSAGE_FOR_FIND_ALL_METHOD, ex);
        }
    }

    private String buildSqlQuery(Pageable pageable) {
        log.debug("Call method buildSqlQuery()");

        String query = SQL_SELECT_ALL_CATS_ORDER_BY_NAME;
        if (pageable != null) {
            query = buildSqlQueryWithPageable(pageable);
        }
        log.debug("SQL query is " + query);
        return query;
    }

    private String buildSqlQueryWithPageable(Pageable pageable) {
        log.debug("Call method buildSqlQueryWithPageable()");

        Sort.Order order;
        if (pageable.getSort().isEmpty()) {
            order = Sort.Order.by(DEFAULT_SORT_BY_COLUMN_NAME);
        } else {
            order = pageable.getSort().iterator().next();
        }
        String query = collectSqlQuery(pageable, order);

        return query;
    }

    private String collectSqlQuery(Pageable pageable, Sort.Order sort) {
        log.debug("Call method collectSqlQuery()");

        String sortProperty = sort.getProperty();
        String sortDirectionName = sort.getDirection().name();
        int pageSize = pageable.getPageSize();
        long pageOffset = pageable.getOffset();

        String result = String.format(
                SQL_SELECT_ALL_CATS_ORDER_BY + " %1$s %2$s LIMIT %3$d OFFSET %4$d",
                sortProperty, sortDirectionName, pageSize, pageOffset);

        return result;
    }

    @Override
    public void save(Cat cat) {
        log.debug("Call method save() for cat: " + cat.getName());
        if (log.isDebugEnabled()) {
            log.debug("Save cat:" + cat);
        }
        int isExist = checkIfExist(cat);
        if (isExist == 1) {
            updateCat(cat);
            if (log.isDebugEnabled()) {
                log.debug("Cat " + cat + " updated");
            }
        } else {
            saveNewCat(cat);
            if (log.isDebugEnabled()) {
                log.debug("Cat " + cat + " saved");
            }
        }
    }

    private void updateCat(Cat cat) {
        log.debug("Call method updateCat() for cat " + cat.getName());

        String name = cat.getName();
        String color = cat.getColor();
        int tailLength = cat.getTailLength();
        int whiskersLength = cat.getWhiskersLength();
        try {
            jdbcTemplate.update(
                    SQL_UPDATE_EXISTED_CAT,
                    color, tailLength, whiskersLength, name
            );
        } catch (DataAccessException ex) {
            log.error(ERROR_MESSAGE_FOR_UPDATE_CAT_METHOD, ex);
            throw new DAOException(ERROR_MESSAGE_FOR_UPDATE_CAT_METHOD, ex);
        }

    }

    private void saveNewCat(Cat cat) {
        log.debug("Call method saveNewCat() for cat: " + cat.getName());
        if (log.isDebugEnabled()) {
            log.debug("Cat: " + cat);
        }
        String name = cat.getName();
        String color = cat.getColor();
        int tailLength = cat.getTailLength();
        int whiskersLength = cat.getWhiskersLength();

        try {
            jdbcTemplate.update(
                    SQL_SAVE_NEW_CAT,
                    name, color, tailLength, whiskersLength
            );
        } catch (DataAccessException ex) {
            log.error(ERROR_MESSAGE_FOR_SAVE_NEW_CAT_METHOD, ex);
            throw new DAOException(ERROR_MESSAGE_FOR_SAVE_NEW_CAT_METHOD, ex);
        }
    }

    private int checkIfExist(Cat cat) {
        log.debug("Call method save() for cat:" + cat.getName());
        if (log.isDebugEnabled()) {
            log.debug("Cat: " + cat);
        }
        String name = cat.getName();
        try {
            int existValue = jdbcTemplate.queryForObject(
                    SQL_CHECK_IF_VALUE_EXIST_IN_CATS_TABLE,
                    Integer.class,
                    new Object[]{name}
            );
            return existValue;
        } catch (DataAccessException ex) {
            log.error(ERROR_MESSAGE_CHECK_IF_EXIST_METHOD, ex);
            throw new DAOException(ERROR_MESSAGE_CHECK_IF_EXIST_METHOD, ex);
        }
    }

    @Override
    public void populateCatsStatTable() {
        log.debug("Call method populateCatsStatTable()");

        CatStatsDTO catStatsDTO = calculateCatStats();

        try {
            jdbcTemplate.update(
                    SQL_SAVE_CATS_STATS,
                    catStatsDTO.getTailLengthMean(),
                    catStatsDTO.getTailLengthMedian(),
                    createSqlArray(catStatsDTO.getTailLengthMode()),
                    catStatsDTO.getWhiskersLengthMean(),
                    catStatsDTO.getWhiskersLengthMedian(),
                    createSqlArray(catStatsDTO.getWhiskersLengthMode())
            );
        } catch (DataAccessException ex) {
            log.error("Error collectSqlQuery", ex);
            throw new DAOException("Error collectSqlQuery", ex);
        }
    }

    private java.sql.Array createSqlArray(List<Integer> list) {
        log.debug("Call method createSqlArray()");
        java.sql.Array intArray;
        try {
            intArray = jdbcTemplate.getDataSource().getConnection()
                    .createArrayOf("integer", list.toArray());
        } catch (SQLException ex) {
            log.error(ERROR_MESSAGE_FOR_CREATE_SQL_ARRAY_METHOD, ex);
            throw new DAOException(ERROR_MESSAGE_FOR_CREATE_SQL_ARRAY_METHOD, ex);
        }
        return intArray;
    }

    private CatStatsDTO calculateCatStats() {
        log.debug("Call method calculateCatStats()");

        List<Integer> tailLengthData = getTailLengthData();
        List<Integer> whiskersLengthData = getWhiskersLengthData();

        if (log.isDebugEnabled()) {
            log.debug("tailLengthData: " + tailLengthData);
            log.debug("whiskersLengthData: " + whiskersLengthData);
        }
        CatStatsDTO catStatsDTO = new CatStatsDTO();

        double tailLengthMean = calculateMean(tailLengthData);
        log.debug("tailLengthMean: " + tailLengthMean);
        catStatsDTO.setTailLengthMean(tailLengthMean);

        double tailLengthMedian = calculateMedian(tailLengthData);
        log.debug("tailLengthMedian: " + tailLengthMedian);
        catStatsDTO.setTailLengthMedian(tailLengthMedian);

        List<Integer> tailLengthMode = calculateMode(tailLengthData);
        log.debug("tailLengthMode: " + tailLengthMode);
        catStatsDTO.setTailLengthMode(tailLengthMode);

        double whiskersLengthMean = calculateMean(whiskersLengthData);
        log.debug("whiskersLengthMean: " + whiskersLengthMean);
        catStatsDTO.setWhiskersLengthMean(whiskersLengthMean);

        double whiskersLengthMedian = calculateMedian(whiskersLengthData);
        log.debug("whiskersLengthMedian: " + whiskersLengthMedian);
        catStatsDTO.setWhiskersLengthMedian(whiskersLengthMedian);

        List<Integer> whiskersLengthMode = calculateMode(whiskersLengthData);
        log.debug("whiskersLengthMode: " + whiskersLengthMode);
        catStatsDTO.setWhiskersLengthMode(whiskersLengthMode);

        if (log.isDebugEnabled()) {
            log.debug("catStatsDTO: " + catStatsDTO);
        }
        return catStatsDTO;
    }

    private List<Integer> getTailLengthData() {
        log.debug("Call method prepareDataForCalculation()");

        List<Integer> result = new ArrayList<>();

        List<Cat> sortedCatsByTailLength = getSortedCats(SORT_BY_COLUMN_TAIL_LENGTH);
        if (log.isDebugEnabled()) {
            log.debug("sortedCatsByTailLength: " + sortedCatsByTailLength);
        }
        for (Cat cat : sortedCatsByTailLength) {
            int tailLength = cat.getTailLength();

            result.add(tailLength);
        }
        if (log.isDebugEnabled()) {
            log.debug("getTailLengthData: " + result);
        }
        return result;
    }

    private List<Integer> getWhiskersLengthData() {
        log.debug("Call method prepareDataForCalculation()");

        List<Integer> result = new ArrayList<>();

        List<Cat> sortedCatsByWhiskersLength = getSortedCats(SORT_BY_COLUMN_WHISKERS_LENGTH);
        if (log.isDebugEnabled()) {
            log.debug("sortedCatsByWhiskersLength: " + sortedCatsByWhiskersLength);
        }
        for (Cat cat : sortedCatsByWhiskersLength) {
            int whiskersLength = cat.getWhiskersLength();

            result.add(whiskersLength);
        }
        if (log.isDebugEnabled()) {
            log.debug("getWhiskersLengthData: " + result);
        }
        return result;
    }

    private List<Cat> getSortedCats(String sortByColumn) {
        log.debug("Call method getSortedData()");

        int allElements = findAll(null).size();

        Pageable pageableWithSort = PageRequest.of(0, allElements, Sort.by(Sort.Direction.ASC, sortByColumn));

        List<Cat> sortedData = findAll(pageableWithSort);

        if (log.isDebugEnabled()) {
            log.debug("sortedData: " + sortedData);
        }
        return sortedData;
    }


    private double calculateMean(List<Integer> input) {
        log.debug("Call method calculateMean()");

        double sum = 0;
        for (Integer number : input) {
            sum += (double) number;
        }
        int amountElements = input.size();

        double mean = round(sum / amountElements);

        log.debug("Mean: " + mean);
        return mean;
    }

    private double round(double input) {

        double result = Math.round(input * 100.0) / 100.0;

        return result;
    }

    private double calculateMedian(List<Integer> input) {
        log.debug("Call method calculateMedian()");

        double median;
        int amountElements = input.size();
        if (amountElements % 2 == 0) {
            int firstValueIndex = amountElements / 2 - 1;
            int secondValueIndex = amountElements / 2;

            double firstValue = input.get(firstValueIndex);
            double secondValues = input.get(secondValueIndex);

            median = (firstValue + secondValues) / 2;
        } else {
            int valueIndex = input.size() / 2;

            median = input.get(valueIndex);
        }
        log.debug("Median: " + median);
        return median;
    }

    private List<Integer> calculateMode(List<Integer> input) {
        log.debug("Call method calculateMode()");

        Map<Integer, Integer> countRepeatedNumber = new HashMap<>();

        input.forEach(number -> countRepeatedNumber.put(
                number, countRepeatedNumber.getOrDefault(number, 0) + 1));

        if (log.isDebugEnabled()) {
            log.debug("Count numbers: " + countRepeatedNumber);
        }
        int maxNumber = Collections.max(countRepeatedNumber.values());

        List<Integer> result = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : countRepeatedNumber.entrySet()) {
            if (entry.getValue() == maxNumber) {
                result.add(entry.getKey());
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Mode result: " + result);
        }
        return result;
    }
}
