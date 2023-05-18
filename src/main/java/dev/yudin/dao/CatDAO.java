package dev.yudin.dao;

import dev.yudin.entities.Cat;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CatDAO {
    List<Cat> findAll(Pageable pageable);

    Cat getBy(String name);

    int countAllRows();

    void save(Cat cat);

    void update(Cat cat);

    void delete(String name);

    boolean isExist(String name);
}
