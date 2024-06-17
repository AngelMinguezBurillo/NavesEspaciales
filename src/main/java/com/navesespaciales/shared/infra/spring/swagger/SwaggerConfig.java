package com.navesespaciales.shared.infra.spring.swagger;

import static com.navesespaciales.shared.constantes.ConstantesAuth.AUTH_HEADER;
import static com.navesespaciales.shared.constantes.ConstantesAuth.AUTH_SCHEMA_APIKEY;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public OpenAPI customOpenAPI(
            final @Value("${spring.application.name}") String appTitle,
            final @Value("${spring.application.description}") String appDesciption,
            final @Value("${com.navesespaciales.swagger.url}") String url,
            final @Value("${spring.application.version}") String appVersion) {

        return new OpenAPI()
                .info(new Info()
                        .title(appTitle)
                        .version(appVersion)
                        .description(appDesciption))
                .servers(Arrays.asList(new Server().url(url)))
                .components(new Components()
                        .addSecuritySchemes(AUTH_SCHEMA_APIKEY,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name(AUTH_HEADER))
                );
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/META-INF/resources/",
                "classpath:/resources/",
                "classpath:/static/",
                "classpath:/public/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
    }
}
