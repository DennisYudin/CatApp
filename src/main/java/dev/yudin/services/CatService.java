package dev.yudin.services;

import dev.yudin.entities.Cat;
import dev.yudin.entities.CatStatsDTO;
import dev.yudin.entities.ColorCountDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CatService {
    Cat getBy(String name);

    List<Cat> findAll(Pageable pageable);

    void save(Cat cat);

    void update(Cat cat);

    void delete(String name);

    int getMaxSize();

    CatStatsDTO showStats();

    List<ColorCountDTO> groupColorInfo();
}
