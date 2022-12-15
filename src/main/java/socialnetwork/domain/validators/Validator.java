package socialnetwork.domain.validators;

import socialnetwork.domain.validators.exceptions.ValidationException;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
