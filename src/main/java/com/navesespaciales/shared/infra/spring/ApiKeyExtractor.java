
package com.navesespaciales.shared.infra.spring;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;


@Component
public class ApiKeyExtractor {

    @Value("${spring.security.apikey}")
    private String apiKey;

    public Optional<Authentication> extract(HttpServletRequest request) {
        final String apikeyHeader = request.getHeader("ApiKey");
        if (apikeyHeader == null || !apikeyHeader.equals(apiKey))
            return Optional.empty();

        return Optional.of(new ApiKeyAuthenticationToken(apikeyHeader, AuthorityUtils.NO_AUTHORITIES));
    }

}
