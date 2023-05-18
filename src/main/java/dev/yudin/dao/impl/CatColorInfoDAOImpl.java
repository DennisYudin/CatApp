package dev.yudin.dao.impl;

import dev.yudin.dao.CatColorInfoDAO;
import dev.yudin.entities.ColorCountDTO;
import dev.yudin.exceptions.DAOException;
import lombok.extern.log4j.Log4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import javax.transaction.Transactional;

@Log4j
@Repository("catColorInfoDAO")
public class CatColorInfoDAOImpl implements CatColorInfoDAO {
	public static final String ERROR_MESSAGE_GET_RESULT_METHOD = "Error during getResult";
	public static final String ERROR_MESSAGE_SAVE_METHOD = "Error during save";
	private SessionFactory factory;

	@Autowired
	public CatColorInfoDAOImpl(SessionFactory factory) {
		this.factory = factory;
	}

	@Override
	@Transactional
	public List<ColorCountDTO> getResult() {
		var session = factory.getCurrentSession();
		try {
			return session.createQuery("From ColorCountDTO", ColorCountDTO.class).getResultList();
		} catch (DataAccessException ex) {
			log.error(ERROR_MESSAGE_GET_RESULT_METHOD, ex);
			throw new DAOException(ERROR_MESSAGE_GET_RESULT_METHOD, ex);
		}
	}

	@Override
	@Transactional
	public void save(List<ColorCountDTO> list) {
		var session = factory.getCurrentSession();
		try {
			list.forEach(session::saveOrUpdate);
		} catch (DataAccessException ex) {
			log.error(ERROR_MESSAGE_SAVE_METHOD, ex);
			throw new DAOException(ERROR_MESSAGE_SAVE_METHOD, ex);
		}
	}
}
