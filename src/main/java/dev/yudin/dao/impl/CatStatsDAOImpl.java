package dev.yudin.dao.impl;

import dev.yudin.dao.CatStatsDAO;
import dev.yudin.entities.CatStatsDTO;
import dev.yudin.exceptions.DAOException;
import lombok.extern.log4j.Log4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

@Log4j
@Repository("catStatsDAOImpl")
public class CatStatsDAOImpl implements CatStatsDAO {

	public static final String ERROR_MESSAGE_SAVE_METHOD = "Error during save";
	public static final String ERROR_MESSAGE_READ_METHOD = "Error during read";
	public static final String ERROR_MESSAGE_IS_EMPTY_METHOD = "Error during isEmpty";
	private SessionFactory factory;

	@Autowired
	public CatStatsDAOImpl(SessionFactory factory) {
		this.factory = factory;
	}

	@Override
	@Transactional
	public void save(CatStatsDTO dto) {
		var session = factory.getCurrentSession();
		try {
			session.save(dto);
		} catch (DataAccessException ex) {
			log.error(ERROR_MESSAGE_SAVE_METHOD, ex);
			throw new DAOException(ERROR_MESSAGE_SAVE_METHOD, ex);
		}
	}

	@Override
	@Transactional
	public CatStatsDTO read() {
		var session = factory.getCurrentSession();
		try {
			return session.createQuery("FROM CatStatsDTO", CatStatsDTO.class).getSingleResult();
		} catch (DataAccessException | NoResultException ex) {
			log.error(ERROR_MESSAGE_READ_METHOD, ex);
			throw new DAOException(ERROR_MESSAGE_READ_METHOD, ex);
		}
	}

	@Override
	@Transactional
	public boolean isEmpty() {
		var session = factory.getCurrentSession();

		boolean result = false;
		try {
			session.createQuery("FROM CatStatsDTO", CatStatsDTO.class).getSingleResult();
		} catch (NoResultException ex) {
			result = true;
		} catch (DataAccessException ex) {
			log.error(ERROR_MESSAGE_IS_EMPTY_METHOD, ex);
			throw new DAOException(ERROR_MESSAGE_IS_EMPTY_METHOD, ex);
		}
		return result;
	}
}
