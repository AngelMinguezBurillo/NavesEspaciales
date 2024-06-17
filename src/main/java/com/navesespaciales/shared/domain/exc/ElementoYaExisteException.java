package com.navesespaciales.shared.domain.exc;

import com.navesespaciales.shared.domain.model.ApiError;
import org.springframework.http.HttpStatus;

public class ElementoYaExisteException extends ApiException {

    private static final long serialVersionUID = 134567893414L;

    public ElementoYaExisteException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public ElementoYaExisteException(final String message, final Throwable cause) {
        super(HttpStatus.BAD_REQUEST, message, cause);
    }

    public ElementoYaExisteException(final ApiError apiError) {
        super(HttpStatus.BAD_REQUEST, apiError);
    }

    public ElementoYaExisteException(final ApiError apiError, final Throwable cause) {
        super(HttpStatus.BAD_REQUEST, apiError, cause);
    }

}
