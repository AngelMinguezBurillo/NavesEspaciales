package com.navesespaciales.shared.domain.exc;

import com.navesespaciales.shared.domain.model.ApiError;
import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 245678944L;

    private final HttpStatus httpSatus;
    private final ApiError apiError;

    public ApiException(final HttpStatus httpSatus, final String message) {
        super(message);
        this.httpSatus = httpSatus;
        this.apiError = new ApiError(message);
    }

    public ApiException(final HttpStatus httpSatus, final String message, final Throwable cause) {
        super(message, cause);
        this.httpSatus = httpSatus;
        this.apiError = new ApiError(message);
    }

    public ApiException(final HttpStatus httpSatus, final ApiError apiError) {
        super(apiError.getMessage());
        this.httpSatus = httpSatus;
        this.apiError = apiError;
    }

    public ApiException(final HttpStatus httpSatus, final ApiError apiError, final Throwable cause) {
        super(apiError.getMessage(), cause);
        this.httpSatus = httpSatus;
        this.apiError = apiError;
    }

    public HttpStatus getHttpSatus() {
        return httpSatus;
    }

    public ApiError getApiError() {
        return apiError;
    }
}
