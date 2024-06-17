package com.navesespaciales.shared.infra.intercerptor;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navesespaciales.shared.defaults.LogSupport;
import java.io.IOException;
import java.lang.reflect.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;


@ControllerAdvice
@RequiredArgsConstructor
public class WsRequestBodyAdvice implements RequestBodyAdvice, LogSupport {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(
            final MethodParameter methodParameter,
            final Type mediaType,
            final Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object afterBodyRead(
            final Object body,
            final HttpInputMessage inputMessage,
            final MethodParameter parameter,
            final Type targetType,
            final Class<? extends HttpMessageConverter<?>> converterType) {

        try {
            logInfo("*** REST Request Body  body: {} ***", body != null ? objectMapper.writeValueAsString(body) : "");

        } catch (JsonProcessingException ex) {
            logInfo("*** REST Request Body  body: {} ***", body);
        }

        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(
            final HttpInputMessage inputMessage,
            final MethodParameter parameter,
            final Type type,
            final Class<? extends HttpMessageConverter<?>> type1) throws IOException {
        return inputMessage;
    }

    @Override
    public Object handleEmptyBody(
            final Object obj,
            final HttpInputMessage inputMessage,
            final MethodParameter parameter,
            final Type type,
            final Class<? extends HttpMessageConverter<?>> type1) {
        return obj;
    }

}
