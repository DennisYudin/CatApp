package com.yourcodereview.dev.yudin.mappers;


import com.yourcodereview.dev.yudin.entities.ColorCountEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ColorCountEntityRowMapper implements RowMapper<ColorCountEntity> {

    @Override
    public ColorCountEntity mapRow(ResultSet resultSet, int rowNumber) throws SQLException {

        ColorCountEntity colorCountEntity = new ColorCountEntity();

        colorCountEntity.setColor(resultSet.getString("color"));
        colorCountEntity.setCount(resultSet.getInt("count"));

        return colorCountEntity;
    }
}
