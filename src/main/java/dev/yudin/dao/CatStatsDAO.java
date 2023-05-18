package dev.yudin.dao;

import dev.yudin.entities.CatStatsDTO;

public interface CatStatsDAO {

	void save(CatStatsDTO dto);

	CatStatsDTO read();

	boolean isEmpty();
}
