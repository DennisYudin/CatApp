package com.yourcodereview.dev.yudin.services;

import com.yourcodereview.dev.yudin.entities.Cat;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CatService {

    List<Cat> findAll(Pageable pageable);

    void save(Cat cat);
}
