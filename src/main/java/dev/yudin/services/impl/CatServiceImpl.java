package dev.yudin.services.impl;

import static java.util.stream.Collectors.toList;

import dev.yudin.dao.CatColorInfoDAO;
import dev.yudin.dao.CatDAO;
import dev.yudin.dao.CatStatsDAO;
import dev.yudin.entities.Cat;
import dev.yudin.entities.CatStatsDTO;
import dev.yudin.entities.ColorCountDTO;
import dev.yudin.enums.CatColorResolver;
import dev.yudin.exceptions.DAOException;
import dev.yudin.exceptions.ServiceException;
import dev.yudin.services.CatService;
import dev.yudin.stats_calculator.Calculator;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j
@Service
public class CatServiceImpl implements CatService {
	private static final String ERROR_MESSAGE_FOR_FIND_ALL_METHOD = "Error during call the method findAll()";
	private static final String ERROR_MESSAGE_FOR_SAVE_METHOD = "Error during call the method save()";
	public static final int MAX_SIZE = 27;
	public static final String ERROR_MESSAGE_GET_MAX_SIZE_METHOD = "Error during getMaxSize";
	public static final String ERROR_MESSAGE_SHOW_STATS_METHOD = "Error during showStats";
	public static final String ERROR_MESSAGE_GROUP_COLOR_INFO_METHOD = "Error during groupColorInfo";
	public static final String ERROR_MESSAGE_UPDATE_METHOD = "Error during update";
	public static final String ERROR_MESSAGE_DELETE_METHOD = "Error during delete";

	private CatDAO catDAO;
	private CatStatsDAO catStatsDAO;
	private CatColorInfoDAO catColorInfoDAO;
	private Calculator calculator;

	@Autowired
	public CatServiceImpl(CatDAO catDAO,
						  CatStatsDAO catStatsDAO,
						  CatColorInfoDAO catColorInfoDAO,
						  Calculator calculator) {
		this.catDAO = catDAO;
		this.catStatsDAO = catStatsDAO;
		this.catColorInfoDAO = catColorInfoDAO;
		this.calculator = calculator;
	}

	@Override
	public int getMaxSize() {
		try {
			return catDAO.countAllRows();
		} catch (DAOException ex) {
			log.error(ERROR_MESSAGE_GET_MAX_SIZE_METHOD, ex);
			throw new ServiceException(ERROR_MESSAGE_GET_MAX_SIZE_METHOD, ex);
		}
	}

	@Override
	public CatStatsDTO showStats() {
		var allCats = catDAO.findAll(PageRequest.of(0, MAX_SIZE));

		CatStatsDTO statsDTO = calculateStats(allCats);
		try {
			if (catStatsDAO.isEmpty()) {
				catStatsDAO.save(statsDTO);
			}
			return catStatsDAO.read();
		} catch (DAOException ex) {
			log.error(ERROR_MESSAGE_SHOW_STATS_METHOD, ex);
			throw new ServiceException(ERROR_MESSAGE_SHOW_STATS_METHOD, ex);
		}
	}

	private CatStatsDTO calculateStats(List<Cat> list) {
		CatStatsDTO statsDTO = new CatStatsDTO();

		var tailLengthData = list.stream()
				.map(Cat::getTailLength)
				.collect(toList());
		statsDTO.setTailLengthMean(calculator.calculateMean(tailLengthData));
		statsDTO.setTailLengthMode(calculator.calculateMode(tailLengthData));
		statsDTO.setTailLengthMedian(calculator.calculateMedian(tailLengthData));

		var whiskersLengthData = list.stream()
				.map(Cat::getWhiskersLength)
				.collect(toList());
		statsDTO.setWhiskersLengthMean(calculator.calculateMean(whiskersLengthData));
		statsDTO.setWhiskersLengthMode(calculator.calculateMode(whiskersLengthData));
		statsDTO.setWhiskersLengthMedian(calculator.calculateMedian(whiskersLengthData));

		return statsDTO;
	}

	@Override
	public List<ColorCountDTO> groupColorInfo() {
		List<Cat> cats = catDAO.findAll(PageRequest.of(0, MAX_SIZE));
		try {
			if (tableIsEmpty()) {
				Map<String, Long> colorFrequency = cats.stream()
						.collect(Collectors.groupingBy(Cat::getColor, Collectors.counting()));

				catColorInfoDAO.save(mapToColorCountDTOs(colorFrequency));
			}
			return catColorInfoDAO.getResult();
		} catch (DAOException ex) {
			log.error(ERROR_MESSAGE_GROUP_COLOR_INFO_METHOD, ex);
			throw new ServiceException(ERROR_MESSAGE_SHOW_STATS_METHOD, ex);
		}
	}

	private boolean tableIsEmpty() {
		return catColorInfoDAO.getResult().isEmpty();
	}

	private List<ColorCountDTO> mapToColorCountDTOs(Map<String, Long> colorCountMap) {
		return colorCountMap.entrySet().stream()
				.map(entry -> new ColorCountDTO(
						CatColorResolver.convertToEntityAttribute(entry.getKey()), Math.toIntExact(entry.getValue())))
				.collect(toList());
	}

	@Override
	public Cat getBy(String name) {
		return catDAO.getBy(name);
	}

	@Override
	public List<Cat> findAll(Pageable pageable) {
		try {
			return catDAO.findAll(pageable);
		} catch (DAOException ex) {
			log.error(ERROR_MESSAGE_FOR_FIND_ALL_METHOD, ex);
			throw new ServiceException(ERROR_MESSAGE_FOR_FIND_ALL_METHOD, ex);
		}
	}

	@Override
	public void save(Cat cat) {
		try {
			catDAO.save(cat);
		} catch (DAOException ex) {
			log.error(ERROR_MESSAGE_FOR_SAVE_METHOD, ex);
			throw new ServiceException(ERROR_MESSAGE_FOR_SAVE_METHOD, ex);
		}
	}

	@Override
	public void update(Cat cat) {
		try {
			catDAO.update(cat);
		} catch (DAOException ex) {
			log.error(ERROR_MESSAGE_UPDATE_METHOD, ex);
			throw new ServiceException(ERROR_MESSAGE_UPDATE_METHOD, ex);
		}
	}

	@Override
	public void delete(String name) {
		try {
			catDAO.delete(name);
		} catch (DAOException ex) {
			log.error(ERROR_MESSAGE_DELETE_METHOD, ex);
			throw new ServiceException(ERROR_MESSAGE_DELETE_METHOD, ex);
		}
	}
}
