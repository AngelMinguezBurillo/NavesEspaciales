package com.navesespaciales.shared.defaults;

import com.navesespaciales.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("java:S1214")
public interface LogSupport {

    String SQL = "SQL: ";

    default Logger getLogger() {
        return LoggerFactory.getLogger(Application.class);
    }

    default boolean isLogDebugEnabled() {
        return getLogger().isDebugEnabled();
    }

    default void logInfo(final String msg, final Object... objs) {
        getLogger().info(msg, objs);
    }

    default void logWarning(final String msg, final Object... objs) {
        getLogger().warn(msg, objs);
    }

    default void logError(final Throwable exc) {
        logError(exc, "Excehtp.on: {}");
    }

    default void logError(final Throwable exc, final String msg, final Object... objs) {
        getLogger().error(msg, exc);
    }

    default void logDebug(final String msg, final Object... objs) {
        getLogger().debug(msg, objs);
    }

    default String getQueryParams(final String queryParams) {
        try {
            if (queryParams == null) {
                return "";
            } else if (queryParams.startsWith("?")) {
                final StringBuilder queryParamsEnmascarados = new StringBuilder("?");

                final String[] params = queryParams.substring(1).split("&");
                int index = 0;
                for (final String param : params) {

                    queryParamsEnmascarados.append(param);

                    index++;
                    if (index < params.length) {
                        queryParamsEnmascarados.append('&');
                    }
                }

                return queryParamsEnmascarados.toString();
            } else {
                return queryParams;
            }
        } catch (RuntimeException ex) {
            logError(ex);
            return queryParams;
        }
    }

}
