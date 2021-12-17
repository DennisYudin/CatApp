package com.yourcodereview.dev.yudin.services;

import com.yourcodereview.dev.yudin.dao.CatDAO;
import com.yourcodereview.dev.yudin.entities.Cat;
import com.yourcodereview.dev.yudin.services.impl.CatServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CatServiceImplTest {

    @InjectMocks
    private CatServiceImpl catService;

    @Mock
    private CatDAO catDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findAll_ShouldReturnAllCatsSortedByName_WhenInputIsPageRequestWithoutSort() {

        Pageable sortedByName = PageRequest.of(0, 27);

        List<Cat> expectedCats = new ArrayList<>();

        expectedCats.add(new Cat("Amur", "black & white", 20, 11));
        expectedCats.add(new Cat("Asya", "black", 10, 10));
        expectedCats.add(new Cat("Cesar", "black & white", 18, 14));
        expectedCats.add(new Cat("Chlo", "black", 14, 13));
        expectedCats.add(new Cat("Clod", "red & white", 12, 15));
        expectedCats.add(new Cat("Dina", "black & white", 17, 12));
        expectedCats.add(new Cat("Flora", "black & white", 12, 15));
        expectedCats.add(new Cat("Foma", "black", 15, 18));
        expectedCats.add(new Cat("Gass", "red & white", 15, 13));
        expectedCats.add(new Cat("Hustav", "red & white", 12, 12));
        expectedCats.add(new Cat("Kelly", "red & white", 26, 11));
        expectedCats.add(new Cat("Lesya", "black & white", 12, 15));
        expectedCats.add(new Cat("Marfa", "black & white", 13, 11));
        expectedCats.add(new Cat("Nemo", "red & white", 17, 13));
        expectedCats.add(new Cat("Neo", "red", 11, 13));
        expectedCats.add(new Cat("Nord", "red & black & white", 19, 12));
        expectedCats.add(new Cat("Odett", "red & white", 17, 13));
        expectedCats.add(new Cat("Ost", "white", 14, 12));
        expectedCats.add(new Cat("Sam", "black & white", 15, 15));
        expectedCats.add(new Cat("Shurik", "red & white", 17, 13));
        expectedCats.add(new Cat("Snow", "white", 19, 14));
        expectedCats.add(new Cat("Tara", "red & white", 17, 12));
        expectedCats.add(new Cat("Tayson", "red & white", 18, 13));
        expectedCats.add(new Cat("Tihon", "red & white", 15, 12));
        expectedCats.add(new Cat("Ula", "red & white", 16, 14));
        expectedCats.add(new Cat("Vika", "black", 14, 10));
        expectedCats.add(new Cat("Yasha", "red & white", 18, 12));

        Mockito.when(catDAO.findAll(sortedByName)).thenReturn(expectedCats);

        List<Cat> actualCats = catService.findAll(sortedByName);

        assertTrue(expectedCats.containsAll(actualCats));
    }

    @Test
    void findAll_ShouldReturnFiveCatsSortedByName_WhenInputIsOffsetAndLimit() {

        Pageable sortedByName = PageRequest.of(1, 5);

        List<Cat> expectedCats = new ArrayList<>();

        expectedCats.add(new Cat("Dina", "black & white", 17, 12));
        expectedCats.add(new Cat("Flora", "black & white", 12, 15));
        expectedCats.add(new Cat("Foma", "black", 15, 18));
        expectedCats.add(new Cat("Gass", "red & white", 15, 13));
        expectedCats.add(new Cat("Hustav", "red & white", 12, 12));

        Mockito.when(catDAO.findAll(sortedByName)).thenReturn(expectedCats);

        List<Cat> actualCats = catService.findAll(sortedByName);

        assertTrue(expectedCats.containsAll(actualCats));
    }

    @Test
    void findAll_ShouldReturnFiveCatsSortedByName_WhenInputIsPageRequestWithSizeFiveWithoutSort() {

        Pageable sortedByName = PageRequest.of(0, 5);

        List<Cat> expectedCats = new ArrayList<>();

        expectedCats.add(new Cat("Amur", "black & white", 20, 11));
        expectedCats.add(new Cat("Asya", "black", 10, 10));
        expectedCats.add(new Cat("Cesar", "black & white", 18, 14));
        expectedCats.add(new Cat("Chlo", "black", 14, 13));
        expectedCats.add(new Cat("Clod", "red & white", 12, 15));

        Mockito.when(catDAO.findAll(sortedByName)).thenReturn(expectedCats);

        List<Cat> actualCats = catService.findAll(sortedByName);

        assertTrue(expectedCats.containsAll(actualCats));
    }

    @Test
    void findAll_ShouldReturnThreeCatsSortedByTailLength_WhenInputIsPageRequestWithSortValue() {

        Pageable sortedByTailLength = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "tail_length"));

        List<Cat> expectedCats = new ArrayList<>();

        expectedCats.add(new Cat("Kelly", "red & white", 26, 11));
        expectedCats.add(new Cat("Amur", "black & white", 20, 11));
        expectedCats.add(new Cat("Nord", "red & black & white", 19, 12));

        Mockito.when(catDAO.findAll(sortedByTailLength)).thenReturn(expectedCats);

        List<Cat> actualCats = catService.findAll(sortedByTailLength);

        assertTrue(expectedCats.containsAll(actualCats));
    }

    @Test
    void findAll_ShouldReturnAllCatsSortedByName_WhenPageIsNull() {

        Pageable page = null;

        List<Cat> expectedCats = new ArrayList<>();

        expectedCats.add(new Cat("Amur", "black & white", 20, 11));
        expectedCats.add(new Cat("Asya", "black", 10, 10));
        expectedCats.add(new Cat("Cesar", "black & white", 18, 14));
        expectedCats.add(new Cat("Chlo", "black", 14, 13));
        expectedCats.add(new Cat("Clod", "red & white", 12, 15));
        expectedCats.add(new Cat("Dina", "black & white", 17, 12));
        expectedCats.add(new Cat("Flora", "black & white", 12, 15));
        expectedCats.add(new Cat("Foma", "black", 15, 18));
        expectedCats.add(new Cat("Gass", "red & white", 15, 13));
        expectedCats.add(new Cat("Hustav", "red & white", 12, 12));
        expectedCats.add(new Cat("Kelly", "red & white", 26, 11));
        expectedCats.add(new Cat("Lesya", "black & white", 12, 15));
        expectedCats.add(new Cat("Marfa", "black & white", 13, 11));
        expectedCats.add(new Cat("Nemo", "red & white", 17, 13));
        expectedCats.add(new Cat("Neo", "red", 11, 13));
        expectedCats.add(new Cat("Nord", "red & black & white", 19, 12));
        expectedCats.add(new Cat("Odett", "red & white", 17, 13));
        expectedCats.add(new Cat("Ost", "white", 14, 12));
        expectedCats.add(new Cat("Sam", "black & white", 15, 15));
        expectedCats.add(new Cat("Shurik", "red & white", 17, 13));
        expectedCats.add(new Cat("Snow", "white", 19, 14));
        expectedCats.add(new Cat("Tara", "red & white", 17, 12));
        expectedCats.add(new Cat("Tayson", "red & white", 18, 13));
        expectedCats.add(new Cat("Tihon", "red & white", 15, 12));
        expectedCats.add(new Cat("Ula", "red & white", 16, 14));
        expectedCats.add(new Cat("Vika", "black", 14, 10));
        expectedCats.add(new Cat("Yasha", "red & white", 18, 12));

        Mockito.when(catDAO.findAll(page)).thenReturn(expectedCats);

        List<Cat> actualCats = catService.findAll(page);

        assertTrue(expectedCats.containsAll(actualCats));
    }

    @Test
    void save_ShouldSaveNewCatIntoTable_WhenInputIsNewCat() {

        Cat newCat = new Cat();
        newCat.setName("DennisYudin");
        newCat.setColor("black");
        newCat.setTailLength(18);
        newCat.setWhiskersLength(8);

        catDAO.save(newCat);

        Mockito.verify(catDAO, Mockito.times(1)).save(newCat);
    }

    @Test
    void save_ShouldUpdateExistedCatIntoTable_WhenInputIsExistedCat() {

        Cat updatedCat = new Cat();
        updatedCat.setName("Amur");
        updatedCat.setColor("black");
        updatedCat.setTailLength(10);
        updatedCat.setWhiskersLength(10);

        catDAO.save(updatedCat);

        Mockito.verify(catDAO, Mockito.times(1)).save(updatedCat);
    }
}
