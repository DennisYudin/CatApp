package dev.yudin.controllers;

import dev.yudin.entities.Cat;
import dev.yudin.entities.CatStatsDTO;
import dev.yudin.entities.ColorCountDTO;
import dev.yudin.exceptions.ControllerException;
import dev.yudin.exceptions.ServiceException;
import dev.yudin.services.CatService;
import dev.yudin.validator.Validator;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j
@RestController
@RequestMapping("/")
public class CatController {
	private static final String ASC_DIRECTION = "asc";
	private static final String DESC_DIRECTION = "desc";
	private static final String ERROR_MESSAGE_GET_SHOW_CATS = "Error during call showCats";
	private static final String ERROR_MESSAGE_SAVE_CATS = "Error during call saveCat";
	public static final String ERROR_MESSAGE_GET_BY_NAME = "Error during call getByName";
	public static final String ERROR_MESSAGE_UPDATE_CAT = "Error during updateCat";
	public static final String ERROR_MESSAGE_DELETE_CAT = "Error during deleteCat";
	public static final String ERROR_MESSAGE_SHOW_STATS = "Error during showStats";
	public static final String ERROR_MESSAGE_SHOW_COLOR_COUNT = "Error during showColorCount";

	private CatService catService;
	private Validator validator;

	@Autowired
	public CatController(CatService catService, Validator validator) {
		this.catService = catService;
		this.validator = validator;
	}

	@GetMapping("/cats")
	public List<Cat> showCats(
			@RequestParam(value = "offset", required = false, defaultValue = "1") Integer currentPage,
			@RequestParam(value = "limit", required = false) Integer amountElemOnPage,
			@RequestParam(value = "attribute", required = false) String attribute,
			@RequestParam(value = "order", required = false) String sortOrder) {

		validator.validate(attribute, sortOrder, amountElemOnPage);

		Pageable pageable = getPageable(currentPage, amountElemOnPage, attribute, sortOrder);

		try {
			return catService.findAll(pageable);
		} catch (ServiceException ex) {
			log.error(ERROR_MESSAGE_GET_SHOW_CATS, ex);
			throw new ControllerException(ERROR_MESSAGE_GET_SHOW_CATS, ex);
		}
	}

	private Pageable getPageable(Integer page, Integer size, String sortName, String direction) {
		if (size == null) {
			size = catService.getMaxSize();
		}
		Pageable pageable;
		if (ASC_DIRECTION.equals(direction)) {
			pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sortName));
		} else if (DESC_DIRECTION.equals(direction)) {
			pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, sortName));
		} else {
			pageable = PageRequest.of(page - 1, size);
		}
		return pageable;
	}

	@PostMapping("/cat")
	public String saveCat(@RequestBody Cat cat) {

		validator.validate(cat);

		try {
			catService.save(cat);
		} catch (ServiceException ex) {
			log.error(ERROR_MESSAGE_SAVE_CATS, ex);
			throw new ControllerException(ERROR_MESSAGE_SAVE_CATS, ex);
		}
		return  String.format(
				"%1$s successfully saved", cat);
	}

	@GetMapping("/cat/{name}")
	public Cat getByName(@PathVariable String name) {

		validator.validate(name);

		try {
			return catService.getBy(name);
		} catch (ServiceException ex) {
			log.error(ERROR_MESSAGE_GET_BY_NAME, ex);
			throw new ControllerException(ERROR_MESSAGE_GET_BY_NAME, ex);
		}
	}

	@PutMapping("/cat")
	public String updateCat(@RequestBody Cat cat) {

		validator.validate(cat.getName());

		try {
			catService.update(cat);
		} catch (ServiceException ex) {
			log.error(ERROR_MESSAGE_UPDATE_CAT, ex);
			throw new ControllerException(ERROR_MESSAGE_UPDATE_CAT, ex);
		}
		return String.format(
				"%1$s was updated successfully", cat.getName());
	}

	@DeleteMapping("/cat/{name}")
	public String deleteCat(@PathVariable String name) {

		validator.validate(name);

		try {
			catService.delete(name);
		} catch (ServiceException ex) {
			log.error(ERROR_MESSAGE_DELETE_CAT, ex);
			throw new ControllerException(ERROR_MESSAGE_DELETE_CAT, ex);
		}
		return String.format(
				"%1$s was deleted successfully", name);
	}

	@GetMapping("/cats/stats")
	public CatStatsDTO showStats() {
		try {
			return catService.showStats();
		} catch (ServiceException ex) {
			log.error(ERROR_MESSAGE_SHOW_STATS, ex);
			throw new ControllerException(ERROR_MESSAGE_SHOW_STATS, ex);
		}
	}

	@GetMapping("/cats/color")
	public List<ColorCountDTO> showColorCount() {
		try {
			return catService.groupColorInfo();
		} catch (ServiceException ex) {
			log.error(ERROR_MESSAGE_SHOW_COLOR_COUNT, ex);
			throw new ControllerException(ERROR_MESSAGE_SHOW_COLOR_COUNT, ex);
		}
	}
}

