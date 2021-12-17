package com.yourcodereview.dev.yudin.services.impl;

import com.yourcodereview.dev.yudin.dao.CatDAO;
import com.yourcodereview.dev.yudin.entities.Cat;
import com.yourcodereview.dev.yudin.exceptions.ServiceException;
import com.yourcodereview.dev.yudin.services.CatService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j
@Service
public class CatServiceImpl implements CatService {
    private static final String ERROR_MESSAGE_FOR_FIND_ALL_METHOD = "Error during call the method findAll()";
    private static final String ERROR_MESSAGE_FOR_SAVE_METHOD = "Error during call the method save()";

    @Autowired
    private CatDAO catDAO;

    @Override
    public List<Cat> findAll(Pageable pageable) {
        log.debug("Call method findAll()");

        try {
            List<Cat> cats = catDAO.findAll(pageable);

            if (log.isDebugEnabled()) {
                log.debug("Cats: " + cats);
            }
            return cats;
        } catch (DataAccessException ex) {
            log.error(ERROR_MESSAGE_FOR_FIND_ALL_METHOD, ex);
            throw new ServiceException(ERROR_MESSAGE_FOR_FIND_ALL_METHOD, ex);
        }
    }

    @Override
    public void save(Cat cat) {
        log.debug("Call method findAll()");

        try {
            catDAO.save(cat);
        } catch (DataAccessException ex) {
            log.error(ERROR_MESSAGE_FOR_SAVE_METHOD, ex);
            throw new ServiceException(ERROR_MESSAGE_FOR_SAVE_METHOD, ex);
        }
    }
}
