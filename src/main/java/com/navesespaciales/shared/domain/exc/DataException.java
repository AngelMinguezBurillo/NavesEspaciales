package com.navesespaciales.shared.domain.exc;

import com.navesespaciales.shared.domain.model.ApiError;
import org.springframework.http.HttpStatus;

public class DataException extends ApiException {

    private static final long serialVersionUID = 1345678964L;

    public DataException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public DataException(final String message, final Throwable cause) {
        super(HttpStatus.BAD_REQUEST, message, cause);
    }

    public DataException(final ApiError apiError) {
        super(HttpStatus.BAD_REQUEST, apiError);
    }

    public DataException(final ApiError apiError, final Throwable cause) {
        super(HttpStatus.BAD_REQUEST, apiError, cause);
    }

}
