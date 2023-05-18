package dev.yudin.entities;

import dev.yudin.enumTypes.EnumTypePostgreSql;
import dev.yudin.enums.CatColor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@TypeDef(name = "enum_postgressql", typeClass = EnumTypePostgreSql.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cat_colors_info")
public class ColorCountDTO {
	@Id
	@Type(type = "enum_postgressql")
	@Enumerated(EnumType.STRING)
	@Column(name = "color")
	private CatColor color;
	@Column(name = "count")
	private int count;

	public String getColor() {
		return color.getColor();
	}

	@Override
	public String toString() {
		return "ColorCountDTO{" +
				"color=" + color +
				", count=" + count +
				'}';
	}
}
