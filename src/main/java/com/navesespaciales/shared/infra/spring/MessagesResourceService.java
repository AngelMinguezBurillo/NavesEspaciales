package com.navesespaciales.shared.infra.spring;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

@Service
public class MessagesResourceService {

    private final MessageSourceAccessor accessor;

    public MessagesResourceService(final MessageSource messageSource) {
        accessor = new MessageSourceAccessor(messageSource);
    }

    public String getResourceString(final String key) {
        try {
            return accessor.getMessage(key);
        } catch (NoSuchMessageException exc) {
            return key;
        }
    }

}
