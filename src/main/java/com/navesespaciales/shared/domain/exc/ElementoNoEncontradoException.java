package com.navesespaciales.shared.domain.exc;

import com.navesespaciales.shared.domain.model.ApiError;
import org.springframework.http.HttpStatus;

public class ElementoNoEncontradoException extends ApiException {

    private static final long serialVersionUID = 134567893414L;

    public ElementoNoEncontradoException(final String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public ElementoNoEncontradoException(final String message, final Throwable cause) {
        super(HttpStatus.NOT_FOUND, message, cause);
    }

    public ElementoNoEncontradoException(final ApiError apiError) {
        super(HttpStatus.NOT_FOUND, apiError);
    }

    public ElementoNoEncontradoException(final ApiError apiError, final Throwable cause) {
        super(HttpStatus.NOT_FOUND, apiError, cause);
    }

}
