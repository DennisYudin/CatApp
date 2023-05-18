package dev.yudin.dao;

import dev.yudin.entities.ColorCountDTO;

import java.util.List;

public interface CatColorInfoDAO {

	void save(List<ColorCountDTO> list);

	List<ColorCountDTO> getResult();
}
