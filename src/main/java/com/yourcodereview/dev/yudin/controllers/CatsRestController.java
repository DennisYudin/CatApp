package com.yourcodereview.dev.yudin.controllers;

import com.yourcodereview.dev.yudin.color.Resolver;
import com.yourcodereview.dev.yudin.entities.Cat;
import com.yourcodereview.dev.yudin.exceptions.ControllerException;
import com.yourcodereview.dev.yudin.exceptions.ServiceException;
import com.yourcodereview.dev.yudin.services.CatService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Log4j
@RestController
@RequestMapping("/")
public class CatsRestController {
    private static final String ASC_DIRECTION = "asc";
    private static final String DESC_DIRECTION = "desc";
    private static final String ERROR_MESSAGE_GET_ALL_ELEMENTS_METHOD = "Error during call method getPageable()";
    private static final String ERROR_MESSAGE_GET_SHOW_CATS_CONTROLLER = "Error during call showCats Controller";
    private static final String ERROR_MESSAGE_SAVE_CATS_CONTROLLER = "Error during call saveCat Controller";

    @Autowired
    private CatService catService;

    @Autowired
    private Resolver colorResolver;

    @GetMapping("/ping")
    public ModelAndView welcome() {
        log.debug("Call controller welcome");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");

        return modelAndView;
    }

    @GetMapping("/cats")
    public List<Cat> showCats(
            @RequestParam(value = "offset", required = false, defaultValue = "1") Integer currentPage,
            @RequestParam(value = "limit", required = false) Integer pageSize,
            @RequestParam(value = "attribute", required = false) String attribute,
            @RequestParam(value = "order", required = false) String order) {

        log.debug("Call controller showCats");

        Pageable pageable = getPageable(currentPage, pageSize, attribute, order);

        try {
            List<Cat> cats = catService.findAll(pageable);

            if (log.isDebugEnabled()) {
                log.debug("Cats: " + cats);
            }
            return cats;
        } catch (ServiceException ex) {
            log.error(ERROR_MESSAGE_GET_SHOW_CATS_CONTROLLER, ex);
            throw new ControllerException(ERROR_MESSAGE_GET_SHOW_CATS_CONTROLLER, ex);
        }
    }

    private Pageable getPageable(Integer page, Integer size, String sortName, String direction) {
        log.debug("Call method getPageable()");

        List<Cat> allElements = getAllElements();

        if (size == null) {
            size = allElements.size();
        }

        Pageable pageable;
        if (ASC_DIRECTION.equals(direction)) {
            pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sortName));
        } else if (DESC_DIRECTION.equals(direction)) {
            pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, sortName));
        } else {
            pageable = PageRequest.of(page - 1, size);
        }
        log.debug("Pageable: " + pageable);
        return pageable;
    }

    private List<Cat> getAllElements() {
        log.debug("Call method getAllElements()");
        try {
            List<Cat> allElements = catService.findAll(null);

            if (log.isDebugEnabled()) {
                log.debug("All elements: " + allElements);
            }
            return allElements;
        } catch (ServiceException ex) {
            log.error(ERROR_MESSAGE_GET_ALL_ELEMENTS_METHOD, ex);
            throw new ControllerException(ERROR_MESSAGE_GET_ALL_ELEMENTS_METHOD, ex);
        }
    }

    @PostMapping("/cat")
    public Cat saveCat(@RequestBody Cat cat) {
        log.debug("Call controller saveCat");

        String name = cat.getName();
        String color = cat.getColor();
        int tailLength = cat.getTailLength();
        int whiskersLength = cat.getWhiskersLength();

        validate(name, color, tailLength, whiskersLength);

        try {
            catService.save(cat);
        } catch (ServiceException ex) {
            log.error(ERROR_MESSAGE_SAVE_CATS_CONTROLLER, ex);
            throw new ControllerException(ERROR_MESSAGE_SAVE_CATS_CONTROLLER, ex);
        }
        return cat;
    }

    private void validate(String name, String color, int tail, int whiskers) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name can NOT be empty or null");
        }
        colorResolver.ifExist(color);
        if (tail <= 0 || whiskers <= 0) {
            throw new IllegalArgumentException("Input can NOT be less or equal zero");
        }
    }
}

