package com.navesespaciales.shared.domain.exc;

public class ConfiguracionException extends RuntimeException {

    private static final long serialVersionUID = 445678967L;

    public ConfiguracionException(final String mensaje) {
        super(mensaje);
    }

    public ConfiguracionException(final String mensaje, final Throwable exc) {
        super(mensaje, exc);
    }
}
