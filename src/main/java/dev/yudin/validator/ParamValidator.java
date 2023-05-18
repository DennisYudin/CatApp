package dev.yudin.validator;

import dev.yudin.entities.Cat;
import dev.yudin.enums.CatColorResolver;
import dev.yudin.services.CatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParamValidator implements Validator {
	private static final String ASC_DIRECTION = "asc";
	private static final String DESC_DIRECTION = "desc";
	private static final List<String> validAttributes = List.of(
			"name", "color", "tail_length", "whiskers_length"
	);
	@Autowired
	private CatService catService;

	@Override
	public void validate(String attr, String sortOrder, Integer pageSize) {
		validateAttribute(attr);
		validateSortOrder(sortOrder);
		validatePageSize(pageSize);
	}

	private void validateAttribute(String attr) {
		if (attr != null && (!validAttributes.contains(attr))) {
			throw new IllegalArgumentException("Not available attribute: " + attr);

		}
	}

	private void validateSortOrder(String name) {
		if (name != null && (!ASC_DIRECTION.equalsIgnoreCase(name) && !DESC_DIRECTION.equalsIgnoreCase(name))) {
			throw new IllegalArgumentException("Not available sort direction name: " + name);

		}
	}

	private void validatePageSize(Integer pageSize) {
		if (pageSize != null && (pageSize < 0 || pageSize > catService.getMaxSize())) {
			throw new IllegalArgumentException("size out of range min: 0 max: " + catService.getMaxSize() + " current size: " + pageSize);
		}
	}

	@Override
	public void validate(Cat cat) {
		validateName(cat.getName());
		validateColor(cat.getColor());
		validateTailAndWhiskers(cat.getTailLength(), cat.getWhiskersLength());

		checkIfCatAlreadyExistBy(cat.getName());
	}

	private void checkIfCatAlreadyExistBy(String name) {
		if (catService.getBy(name) != null) {
			throw new IllegalArgumentException("Cat with name: " + name + " already exist");
		}
	}

	private void validateTailAndWhiskers(int tail, int whiskers) {
		if (tail <= 0 || whiskers <= 0) {
			throw new IllegalArgumentException("Input can NOT be less or equal zero");
		}
	}

	private void validateName(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Name can NOT be empty or null");
		}
	}

	private void validateColor(String color) {
		CatColorResolver.convertToEntityAttribute(color);
	}

	@Override
	public void validate(String name) {
		if (catService.getBy(name) == null) {
			throw new IllegalArgumentException("There is no cat with name: " + name);
		}
	}
}
