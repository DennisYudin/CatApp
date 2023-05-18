package dev.yudin.dao.impl;

import dev.yudin.dao.CatDAO;
import dev.yudin.entities.Cat;
import dev.yudin.exceptions.DAOException;
import lombok.extern.log4j.Log4j;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import javax.transaction.Transactional;

@Log4j
@Repository("catDAO")
public class CatDAOImpl implements CatDAO {
	private static final String DEFAULT_SORT_BY_NAME = "name";
	private static final String SQL_SELECT_ALL_CATS_ORDER_BY_NAME = "FROM Cat ORDER BY name";
	private static final String SQL_SELECT_ALL_CATS_ORDER_BY = "FROM Cat ORDER BY";
	public static final String ERROR_MESSAGE_SAVE_METHOD = "Error during save";
	public static final String ERROR_MESSAGE_GET_BY_METHOD = "Error during getBy";
	public static final String ERROR_MESSAGE_COUNT_ALL_ROWS_METHOD = "Error during countAllRows";
	public static final String SQL_COUNT_ALL_ROWS = "SELECT COUNT(e) FROM Cat e";
	public static final String ERROR_MESSAGE_FIND_ALL_METHOD = "Error during findAll";
	public static final String ERROR_MESSAGE_UPDATE_METHOD = "Error during update";
	public static final String ERROR_MESSAGE_DELETE_METHOD = "Error during delete";
	public static final String SQL_DELETE_CAT = "DELETE FROM Cat WHERE name=:catName";
	public static final String ERROR_MESSAGE_IS_EXIST_METHOD = "Error during isExist";

	private SessionFactory factory;

	@Autowired
	public CatDAOImpl(SessionFactory factory) {
		this.factory = factory;
	}

	@Override
	@Transactional
	public Cat getBy(String name) {
		var session = factory.getCurrentSession();
		try {
			return session.get(Cat.class, name);
		} catch (DataAccessException ex) {
			log.error(ERROR_MESSAGE_GET_BY_METHOD, ex);
			throw new DAOException(ERROR_MESSAGE_GET_BY_METHOD, ex);
		}
	}

	@Override
	@Transactional
	public int countAllRows() {
		var session = factory.getCurrentSession();
		try {
			Long result = (Long) session.createQuery(SQL_COUNT_ALL_ROWS).getSingleResult();
			return result.intValue();
		} catch (DataAccessException ex) {
			log.error(ERROR_MESSAGE_COUNT_ALL_ROWS_METHOD, ex);
			throw new DAOException(ERROR_MESSAGE_COUNT_ALL_ROWS_METHOD, ex);
		}
	}

	@Override
	@Transactional
	public List<Cat> findAll(Pageable pageable) {
		var session = factory.getCurrentSession();

		Query<Cat> cats;
		try {
			if (pageable != null) {
				String sqlQuery = buildSqlQueryWithPageable(pageable);
				cats = session.createQuery(sqlQuery, Cat.class)
						.setMaxResults(pageable.getPageSize())
						.setFirstResult((int) pageable.getOffset());
			} else {
				cats = session.createQuery(SQL_SELECT_ALL_CATS_ORDER_BY_NAME, Cat.class);
			}
		} catch (DataAccessException ex) {
			log.error(ERROR_MESSAGE_FIND_ALL_METHOD, ex);
			throw new DAOException(ERROR_MESSAGE_FIND_ALL_METHOD, ex);
		}
		return cats.getResultList();
	}

	private String buildSqlQueryWithPageable(Pageable pageable) {
		Sort.Order order;
		if (pageable.getSort().isEmpty()) {
			order = Sort.Order.by(DEFAULT_SORT_BY_NAME);
		} else {
			order = pageable.getSort().iterator().next();
		}
		return collectSqlQuery(order);
	}

	private String collectSqlQuery(Sort.Order sort) {
		String sortAttribute = sort.getProperty();
		String sortDirection = sort.getDirection().name();

		return String.format(
				SQL_SELECT_ALL_CATS_ORDER_BY + " %1$s %2$s",
				sortAttribute, sortDirection);
	}

	@Override
	@Transactional
	public void save(Cat cat) {
		var session = factory.getCurrentSession();
		try {
			session.save(cat);
		} catch (DataAccessException ex) {
			log.error(ERROR_MESSAGE_SAVE_METHOD, ex);
			throw new DAOException(ERROR_MESSAGE_SAVE_METHOD, ex);
		}
	}

	@Override
	@Transactional
	public void update(Cat cat) {
		var session = factory.getCurrentSession();
		try {
			session.update(cat);
		} catch (DataAccessException ex) {
			log.error(ERROR_MESSAGE_UPDATE_METHOD, ex);
			throw new DAOException(ERROR_MESSAGE_UPDATE_METHOD, ex);
		}
	}

	@Override
	@Transactional
	public void delete(String name) {
		var session = factory.getCurrentSession();
		try {
			Query query = session.createQuery(SQL_DELETE_CAT);
			query.setParameter("catName", name);

			query.executeUpdate();
		} catch (DataAccessException ex) {
			log.error(ERROR_MESSAGE_DELETE_METHOD, ex);
			throw new DAOException(ERROR_MESSAGE_DELETE_METHOD, ex);
		}
	}

	@Override
	@Transactional
	public boolean isExist(String name) {
		var session = factory.getCurrentSession();
		try {
			return session.get(Cat.class, name) != null;
		} catch (DataAccessException ex) {
			log.error(ERROR_MESSAGE_IS_EXIST_METHOD, ex);
			throw new DAOException(ERROR_MESSAGE_IS_EXIST_METHOD, ex);
		}
	}
}
