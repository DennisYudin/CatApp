package dev.yudin.enumTypes;

import dev.yudin.enums.CatColor;
import dev.yudin.enums.CatColorResolver;
import dev.yudin.exceptions.DAOException;
import lombok.extern.log4j.Log4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.EnumType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

@Log4j
public class EnumTypePostgreSql extends EnumType {

	@Override
	public Object nullSafeGet(
			ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
			throws SQLException {
		String color = rs.getString(names[0]);
		if (rs.wasNull()) {
			return null;
		}
		return CatColorResolver.convertToEntityAttribute(color);
	}

	@Override
	public void nullSafeSet(PreparedStatement statement, Object value, int index,
							SharedSessionContractImplementor session)
			throws HibernateException, SQLException {
		if (value == null) {
			statement.setNull(index, Types.OTHER);
		} else {
			String enumValue = value.toString();
			statement.setObject(index, convertToDatabaseType(enumValue), Types.OTHER);
		}
	}

	private String convertToDatabaseType(Object value) {
		String enumValueForDB = value.toString();

		switch (enumValueForDB) {
			case "BLACK":
				return CatColor.BLACK.getColor();
			case "WHITE":
				return CatColor.WHITE.getColor();
			case "BLACK_AND_WHITE":
				return CatColor.BLACK_AND_WHITE.getColor();
			case "RED":
				return CatColor.RED.getColor();
			case "RED_AND_BLACK_AND_WHITE":
				return CatColor.RED_AND_BLACK_AND_WHITE.getColor();
			case "RED_AND_WHITE":
				return CatColor.RED_AND_WHITE.getColor();
			default:
				log.warn("There is no correct color: " + enumValueForDB);
				throw new DAOException("There is no correct color: " + enumValueForDB);
		}
	}
}
