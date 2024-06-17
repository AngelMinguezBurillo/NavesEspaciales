package com.navesespaciales.shared.infra.intercerptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navesespaciales.shared.defaults.LogSupport;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class WsResponseBodyAdvice implements ResponseBodyAdvice<Object>, LogSupport {

    @Override
    public boolean supports(final MethodParameter methodParameter, final Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            final Object obj,
            final MethodParameter methodParameter,
            final MediaType mediaType,
            final Class<? extends HttpMessageConverter<?>> aClass,
            final ServerHttpRequest serverHttpRequest,
            final ServerHttpResponse serverHttpResponse) {

        final HttpServletResponse servletResponse = ((ServletServerHttpResponse) serverHttpResponse).getServletResponse();

        try {
            logInfo("*** REST Response Body -> Response code:{}  body: {}",
                    servletResponse.getStatus(),
                    new ObjectMapper().writeValueAsString(obj));

        } catch (JsonProcessingException ex) {
            logInfo("*** REST Response Body -> Response code:{}  body: {}",
                    servletResponse.getStatus(),
                    obj.toString());
        }

        return obj;
    }

}
