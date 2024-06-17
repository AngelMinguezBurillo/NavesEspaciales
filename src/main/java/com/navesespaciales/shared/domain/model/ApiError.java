package com.navesespaciales.shared.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
public class ApiError implements Serializable {

    private static final long serialVersionUID = 4564878941L;

    private String message;
    private List<String> errors;
    private List<ApiFieldError> fieldsErrors;

    public ApiError() {
        errors = new ArrayList<>();
        fieldsErrors = new ArrayList<>();
    }

    public ApiError(final String message) {
        this.message = message;
        errors = new ArrayList<>();
        fieldsErrors = new ArrayList<>();
    }

    public void addError(final String error) {
        errors.add(error);
    }

    public void addFieldError(final String field, final String error) {
        fieldsErrors.add(new ApiFieldError(field, error));
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiFieldError implements Serializable {

        private static final long serialVersionUID = 3154687787L;

        private String field;
        private String message;
    }

}
