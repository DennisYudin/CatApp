package com.yourcodereview.dev.yudin.dao;

import com.yourcodereview.dev.yudin.entities.Cat;
import com.yourcodereview.dev.yudin.entities.ColorCountEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CatDAO {

    List<Cat> findAll(Pageable pageable);

    List<ColorCountEntity> getSortedList();

    void populateCatColorsInfoTable(List<ColorCountEntity> input);

    void populateCatsStatTable();

    void save(Cat cat);
}
