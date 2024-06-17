package com.navesespaciales.shared.domain.exc;

import com.navesespaciales.shared.domain.model.ApiError;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class AccesoDenegadoException extends ApiException {

    private static final long serialVersionUID = 1345678964L;

    public AccesoDenegadoException(final String message) {
        super(HttpStatus.FORBIDDEN, message);
    }

    public AccesoDenegadoException(final String message, final Throwable cause) {
        super(HttpStatus.FORBIDDEN, message, cause);
    }

    public AccesoDenegadoException(final ApiError apiError) {
        super(HttpStatus.FORBIDDEN, apiError);
    }

    public AccesoDenegadoException(final ApiError apiError, final Throwable cause) {
        super(HttpStatus.FORBIDDEN, apiError, cause);
    }

}
