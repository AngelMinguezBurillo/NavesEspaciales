package com.navesespaciales.shared.infra.intercerptor;

import com.navesespaciales.shared.defaults.LogSupport;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class WsRequestInterceptor implements HandlerInterceptor, LogSupport {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object object) throws IOException {
        this.logRequest(request);

        return true;
    }

    private void logRequest(final HttpServletRequest request) {
        logInfo("*** REST Request -> Request URL:{}   Remote address:{} HTTP method:{} Content type:{} Headers:{}",
                request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + getQueryParams(request.getQueryString()) : ""),
                request.getRemoteAddr(),
                request.getMethod(),
                request.getContentType(),
                Collections.list(request.getHeaderNames())
                        .stream()
                        .map(header -> header + "=" + request.getHeader(header))
                        .collect(Collectors.joining("; ")));
    }

}
