package dev.yudin.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.vladmihalcea.hibernate.type.array.IntArrayType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@TypeDef(
		name = "int-array",
		typeClass = IntArrayType.class
)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "cats_stat")
public class CatStatsDTO {
	@Id
	@Column(name = "tail_length_mean")
	private double tailLengthMean;
	@Column(name = "tail_length_median")
	private double tailLengthMedian;
	@Type(type = "int-array")
	@Column(
			name = "tail_length_mode",
			columnDefinition = "integer[]"
	)
	private int[] tailLengthMode;

	@Column(name = "whiskers_length_mean")
	private double whiskersLengthMean;
	@Column(name = "whiskers_length_median")
	private double whiskersLengthMedian;
	@Type(type = "int-array")
	@Column(
			name = "whiskers_length_mode",
			columnDefinition = "integer[]"
	)
	private int[] whiskersLengthMode;

	@JsonGetter("tail_length_mean")
	public double getTailLengthMean() {
		return tailLengthMean;
	}
	@JsonGetter("tail_length_median")
	public double getTailLengthMedian() {
		return tailLengthMedian;
	}
	@JsonGetter("tail_length_mode")
	public int[] getTailLengthMode() {
		return tailLengthMode;
	}
	@JsonGetter("whiskers_length_mean")
	public double getWhiskersLengthMean() {
		return whiskersLengthMean;
	}
	@JsonGetter("whiskers_length_median")
	public double getWhiskersLengthMedian() {
		return whiskersLengthMedian;
	}
	@JsonGetter("whiskers_length_mode")
	public int[] getWhiskersLengthMode() {
		return whiskersLengthMode;
	}
}
