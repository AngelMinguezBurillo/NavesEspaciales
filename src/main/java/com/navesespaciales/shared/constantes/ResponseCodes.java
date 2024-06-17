package com.navesespaciales.shared.constantes;

public final class ResponseCodes {

    public static final String HTTP_CODE_OK = "200";
    public static final String HTTP_CODE_CREATED = "201";
    public static final String HTTP_CODE_NO_CONTECT = "204";
    public static final String HTTP_CODE_RESET_CONTENT = "205";

    public static final String HTTP_CODE_BAD_REQUEST = "400";
    public static final String HTTP_CODE_UNAUTHORIZED = "401";
    public static final String HTTP_CODE_FORBIDDEN = "403";
    public static final String HTTP_CODE_NOT_FOUND = "404";
    public static final String HTTP_CODE_CONFLICT = "409";

    public static final String HTTP_CODE_UNPROCESSABLE = "422";

    public static final String HTTP_CODE_INTERNAL_SERVER_ERROR = "500";

    private ResponseCodes() {
    }

}
