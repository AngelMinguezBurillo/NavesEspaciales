package com.navesespaciales.application;

import com.navesespaciales.domain.NavesEspacialesRepository;
import com.navesespaciales.domain.NavesEspacialesUseCases;
import com.navesespaciales.domain.model.NaveEspacialEntity;
import com.navesespaciales.domain.model.NaveEspacialBusquedaResponse;
import com.navesespaciales.domain.model.NaveEspacialEliminarResponse;
import com.navesespaciales.domain.model.NaveEspacialRequest;
import com.navesespaciales.domain.model.NaveEspacialResponse;
import com.navesespaciales.domain.model.NavesEspacialesPaginaResponse;
import static com.navesespaciales.shared.constantes.Errores.ERROR_NAVE_NO_ENCONTRADA;
import static com.navesespaciales.shared.constantes.Errores.ERROR_NAVE_YA_EXISTE;
import com.navesespaciales.shared.domain.exc.ElementoNoEncontradoException;
import com.navesespaciales.shared.domain.exc.ElementoYaExisteException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NavesEspacialesUseCasesImpl implements NavesEspacialesUseCases {

    private final NavesEspacialesRepository navesEspacialesRepository;

    @Override
    public NaveEspacialResponse insertarNaveEspacial(final NaveEspacialRequest naveEspacialRequest) {

        try {
            final var naveEspacial = NaveEspacialEntity.builder()
                    .nombre(naveEspacialRequest.getNombre()).build();

            navesEspacialesRepository.save(naveEspacial);

            return NaveEspacialResponse.builder()
                    .idNave(naveEspacial.getIdNave())
                    .nombre(naveEspacial.getNombre()).build();
        } catch (DataIntegrityViolationException exc) {
            throw new ElementoYaExisteException(ERROR_NAVE_YA_EXISTE);
        }

    }

    @Override
    public NavesEspacialesPaginaResponse obtenerTodasNavesEspaciales(final Integer pagina, final Integer elementos) {
        final var pageable = PageRequest.of(pagina, elementos);
        final var paginaActual = navesEspacialesRepository.findAll(pageable);
        return NavesEspacialesPaginaResponse.builder().pagina(paginaActual).build();
    }

    @Override
    public NaveEspacialResponse obtenerNaveEspacial(final Long idNave) {
        final var naveEspacial = navesEspacialesRepository.findById(idNave);
        if (naveEspacial.isPresent()) {
            return NaveEspacialResponse.builder()
                    .idNave(naveEspacial.get().getIdNave())
                    .nombre(naveEspacial.get().getNombre()).build();
        } else {
            throw new ElementoNoEncontradoException(ERROR_NAVE_NO_ENCONTRADA);
        }
    }

    @Override
    public NaveEspacialBusquedaResponse obtenerNaveEspacialPorNombre(final String nombre) {
        final var resultadoBusqueda = navesEspacialesRepository.findByNombreIgnoreCaseContaining(nombre);
        return NaveEspacialBusquedaResponse.builder()
                .resultado(resultadoBusqueda.stream()
                        .map(n -> NaveEspacialResponse.builder()
                        .idNave(n.getIdNave())
                        .nombre(n.getNombre()).build())
                        .collect(Collectors.toList())).build();
    }

    @Override
    public NaveEspacialResponse editarNaveEspacial(final Long idNave, final NaveEspacialRequest naveEspacialRequest) {
        try {
            if (navesEspacialesRepository.existsById(idNave)) {
                final var naveEspacial = NaveEspacialEntity.builder()
                        .idNave(idNave)
                        .nombre(naveEspacialRequest.getNombre()).build();

                navesEspacialesRepository.save(naveEspacial);

                return NaveEspacialResponse.builder()
                        .idNave(naveEspacial.getIdNave())
                        .nombre(naveEspacial.getNombre()).build();
            } else {
                throw new ElementoNoEncontradoException(ERROR_NAVE_NO_ENCONTRADA);
            }
        } catch (DataIntegrityViolationException exc) {
            throw new ElementoYaExisteException(ERROR_NAVE_YA_EXISTE);
        }

    }

    @Override
    public NaveEspacialEliminarResponse eliminarNaveEspacial(final Long idNave) {
        if (navesEspacialesRepository.existsById(idNave)) {
            navesEspacialesRepository.deleteById(idNave);
        } else {
            throw new ElementoNoEncontradoException(ERROR_NAVE_NO_ENCONTRADA);
        }

        return NaveEspacialEliminarResponse.builder().idNave(idNave).build();

    }

}
