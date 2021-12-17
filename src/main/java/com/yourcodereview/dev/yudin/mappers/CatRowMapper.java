package com.yourcodereview.dev.yudin.mappers;

import com.yourcodereview.dev.yudin.entities.Cat;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CatRowMapper implements RowMapper<Cat> {

    @Override
    public Cat mapRow(ResultSet resultSet, int rowNumber) throws SQLException {

        Cat cat = new Cat();

        cat.setName(resultSet.getString("name"));
        cat.setColor(resultSet.getString("color"));
        cat.setTailLength(resultSet.getInt("tail_length"));
        cat.setWhiskersLength(resultSet.getInt("whiskers_length"));

        return cat;
    }
}
