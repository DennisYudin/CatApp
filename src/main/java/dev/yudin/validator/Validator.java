package dev.yudin.validator;

import dev.yudin.entities.Cat;

public interface Validator {

	void validate(String attr, String sortOrder, Integer size);

	void validate(Cat cat);

	void validate(String name);
}
