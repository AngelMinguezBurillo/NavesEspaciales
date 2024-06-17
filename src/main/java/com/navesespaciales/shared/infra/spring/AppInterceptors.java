package com.navesespaciales.shared.infra.spring;

import com.navesespaciales.shared.infra.intercerptor.WsRequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AppInterceptors implements WebMvcConfigurer {

    private final WsRequestInterceptor requestInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor);
    }
}
