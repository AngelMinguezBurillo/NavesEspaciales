package com.navesespaciales.shared.infra.spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import static com.navesespaciales.shared.constantes.Errores.ERROR_ENTRADA_NOVALIDO;
import static com.navesespaciales.shared.constantes.Errores.ERROR_GENERAL;
import static com.navesespaciales.shared.constantes.Errores.ERROR_PARAMETRO_NOVALIDO;
import com.navesespaciales.shared.defaults.LogSupport;
import com.navesespaciales.shared.domain.exc.ApiException;
import com.navesespaciales.shared.domain.model.ApiError;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@RequiredArgsConstructor
public class AppExceptionHandler implements LogSupport {

    private static final String HA_OCURRIDO_UN_ERROR = "Ha ocurrido un error: {}";

    private final MessagesResourceService messagesResourceService;

    @ExceptionHandler(EmptyResultDataAccessException.class)
    protected ResponseEntity<ApiError> handleEmptyResultDataAccessException(final EmptyResultDataAccessException exc) {
        logWarning(exc.getClass().getName(), exc.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(messagesResourceService.getResourceString("exc.EmptyResultDataAccessException")));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ApiError> handleIllegalArgumentException(final IllegalArgumentException exc) {
        logWarning(exc.getClass().getName(), exc.getMessage());

        if (exc.getMessage() != null) {
            return new ResponseEntity<>(new ApiError(exc.getMessage()), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(new ApiError("Ha ocurrido un problema al realizar la operacion"), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ApiError> handleAccessDeniedException(final AccessDeniedException exc) {
        logWarning(exc.getClass().getName(), exc.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiError(messagesResourceService.getResourceString(exc.getMessage())));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<ApiError> handleMissingRequestHeaderException(final MissingRequestHeaderException exc) {
        logWarning(exc.getClass().getName(), exc.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiError(messagesResourceService.getResourceString("exc.MissingRequestHeaderException")));
    }

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<ApiError> handleApiException(final ApiException exc) {
        logError(exc);
        return ResponseEntity.status(exc.getHttpSatus()).body(new ApiError(messagesResourceService.getResourceString(exc.getMessage())));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ApiError> handleMissingServletRequestParameterException(final MissingServletRequestParameterException exc) {
        logWarning(HA_OCURRIDO_UN_ERROR, exc.getMessage());

        final ApiError apiError = new ApiError(messagesResourceService.getResourceString(ERROR_ENTRADA_NOVALIDO));

        apiError.addFieldError(exc.getParameterName(), messagesResourceService.getResourceString(ERROR_PARAMETRO_NOVALIDO));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiError> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exc) {
        logWarning(HA_OCURRIDO_UN_ERROR, exc.getMessage());

        final ApiError apiError = new ApiError(messagesResourceService.getResourceString(ERROR_ENTRADA_NOVALIDO));

        exc.getBindingResult()
                .getFieldErrors()
                .stream().forEach(err -> apiError.addFieldError(err.getField(), messagesResourceService.getResourceString(err.getDefaultMessage())));

        exc.getBindingResult()
                .getGlobalErrors()
                .stream().forEach(err -> apiError.addError(err.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ApiError> handleConstraintViolationException(final ConstraintViolationException exc) {
        logWarning(HA_OCURRIDO_UN_ERROR, exc.getMessage());

        final ApiError apiError = new ApiError(messagesResourceService.getResourceString(ERROR_ENTRADA_NOVALIDO));

        exc.getConstraintViolations().stream().forEach(
                violation -> apiError.addFieldError(violation.getPropertyPath().toString(), messagesResourceService.getResourceString(violation.getMessage()))
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(JsonMappingException.class)
    protected ResponseEntity<ApiError> handleJsonMappingException(final JsonMappingException exc) {
        logWarning("JsonMappingException: {}", exc.getMessage());

        final var apiError = new ApiError(messagesResourceService.getResourceString(ERROR_ENTRADA_NOVALIDO));

        apiError.addFieldError(
                exc.getPath().get(0).getFieldName(),
                messagesResourceService.getResourceString("error." + exc.getPath().get(0).getFieldName() + ".novalido"));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ApiError> handleHttpMessageNotReadableException(final HttpMessageNotReadableException exc) {
        logWarning("Ha ocurrido un error al leer el json de entrada: {}, {}", exc.getCause().getClass().getName(), exc.getMessage());

        final var cause = exc.getCause();
        switch (cause) {
            case JsonMappingException iexc -> {
                return handleJsonMappingException(iexc);
            }
            case JsonProcessingException iexc -> {
                return handleException(iexc);
            }
            default -> {
                return handleException(exc);
            }
        }
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<ApiError> handleValidationException(final ValidationException exc) {
        logWarning(HA_OCURRIDO_UN_ERROR, exc.getMessage());

        switch (exc.getCause()) {
            case ApiException wsYaGanasteException -> {
                return ResponseEntity
                        .status(wsYaGanasteException.getHttpSatus())
                        .body(new ApiError(messagesResourceService.getResourceString(exc.getCause().getMessage())));
            }
            default -> {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ApiError(messagesResourceService.getResourceString(ERROR_ENTRADA_NOVALIDO)));
            }
        }
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiError> handleException(final Exception exc) {
        logError(exc, HA_OCURRIDO_UN_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(messagesResourceService.getResourceString(ERROR_GENERAL)));
    }
}
