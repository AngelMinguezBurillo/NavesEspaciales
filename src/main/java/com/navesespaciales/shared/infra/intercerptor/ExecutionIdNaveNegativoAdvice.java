package com.navesespaciales.shared.infra.intercerptor;

import com.navesespaciales.shared.defaults.LogSupport;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionIdNaveNegativoAdvice implements LogSupport {

    @Around("execution(* com.navesespaciales.infra.endpoint.NavesEspacialesEndpoint.obtenerNaveEspacial(..))")
    public Object executionObtenerNaveEspacial(final ProceedingJoinPoint point) throws Throwable {
        if ((Long) point.getArgs()[1] < 0) {
            logInfo("Se ha introducido un idNave negativo: {}", (Long) point.getArgs()[1]);
        }

        return point.proceed();
    }

}
