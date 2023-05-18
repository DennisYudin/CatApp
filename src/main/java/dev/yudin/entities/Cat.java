package dev.yudin.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import dev.yudin.enumTypes.EnumTypePostgreSql;
import dev.yudin.enums.CatColor;
import dev.yudin.enums.CatColorResolver;
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
@Table(name = "cats")
public class Cat {
	@Id
	@Column(name = "name")
	private String name;
	@Type(type = "enum_postgressql")
	@Enumerated(EnumType.STRING)
	@Column(name = "color")
	private CatColor color;
	@Column(name = "tail_length")
	private int tailLength;
	@Column(name = "whiskers_length")
	private int whiskersLength;

	public String getColor() {
		return color.getColor();
	}

	public void setColor(String color) {
		this.color = CatColorResolver.convertToEntityAttribute(color);
	}

	@JsonGetter("tail_length")
	public int getTailLength() {
		return tailLength;
	}

	@JsonGetter("whiskers_length")
	public int getWhiskersLength() {
		return whiskersLength;
	}

	@Override
	public String toString() {
		return "Cat{" +
				"name='" + name + '\'' +
				", color='" + color + '\'' +
				", tailLength=" + tailLength +
				", whiskersLength=" + whiskersLength +
				'}';
	}
}
