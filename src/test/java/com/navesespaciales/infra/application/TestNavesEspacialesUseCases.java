package com.navesespaciales.infra.application;

import com.navesespaciales.application.NavesEspacialesUseCasesImpl;
import com.navesespaciales.domain.NavesEspacialesRepository;
import com.navesespaciales.domain.NavesEspacialesUseCases;
import com.navesespaciales.domain.model.NaveEspacialBusquedaResponse;
import com.navesespaciales.domain.model.NaveEspacialEliminarResponse;
import com.navesespaciales.domain.model.NaveEspacialEntity;
import com.navesespaciales.domain.model.NaveEspacialRequest;
import com.navesespaciales.domain.model.NaveEspacialResponse;
import com.navesespaciales.domain.model.NavesEspacialesPaginaResponse;
import com.navesespaciales.shared.domain.exc.ElementoNoEncontradoException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hibernate.exception.ConstraintViolationException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DataJpaTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Import({NavesEspacialesUseCasesImpl.class})
public class TestNavesEspacialesUseCases {

    @Autowired
    private NavesEspacialesUseCases navesEspacialesUseCases;

    @Autowired
    private NavesEspacialesRepository navesEspacialesRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private NaveEspacialEntity naveEspacialEntity;

    @BeforeEach
    public void prepararDatosPrueba() {
        naveEspacialEntity = NaveEspacialEntity.builder().nombre("X-Wing").build();
        navesEspacialesRepository.save(naveEspacialEntity);
        testEntityManager.flush();
        testEntityManager.clear();
    }

    @Test
    void testCrearBorradorCampania_ok() {

        NaveEspacialRequest naveEspacialRequest = NaveEspacialRequest.builder()
                .nombre("Halcon Milenario").build();

        assertThat(navesEspacialesUseCases.insertarNaveEspacial(naveEspacialRequest), isA(NaveEspacialResponse.class));

    }

    @Test
    void testCrearBorradorCampania_errorYaExiste() {

        NaveEspacialRequest naveEspacialRequest = NaveEspacialRequest.builder()
                .nombre("X-Wing").build();

        assertThrows(ConstraintViolationException.class, () -> {
            navesEspacialesUseCases.insertarNaveEspacial(naveEspacialRequest);
            testEntityManager.flush();
        });
    }

    @Test
    void testObtenerTodasNavesEspaciales_ok() {

        NavesEspacialesPaginaResponse navesEspacialesPaginaResponse = navesEspacialesUseCases.obtenerTodasNavesEspaciales(0, 10);

        assertEquals(navesEspacialesPaginaResponse.getPagina().getTotalElements(), 1);
    }

    @Test
    void testObtenerNaveEspacial_ok() {

        NaveEspacialResponse navesEspacialesResponse = navesEspacialesUseCases.obtenerNaveEspacial(1L);

        assertEquals(navesEspacialesResponse.getNombre(), "X-Wing");
    }

    @Test
    void testObtenerNaveEspacialPorNombre_ok() {

        NaveEspacialBusquedaResponse naveEspacialBusquedaResponse = navesEspacialesUseCases.obtenerNaveEspacialPorNombre("X-");

        assertEquals(naveEspacialBusquedaResponse.getResultado().size(), 1);
    }

    @Test
    void testEditarNaveEspacial_ok() {

        NaveEspacialRequest naveEspacialRequest = NaveEspacialRequest.builder()
                .nombre("X-Wing Editado").build();

        NaveEspacialResponse naveEspacialResponse = navesEspacialesUseCases.editarNaveEspacial(1L, naveEspacialRequest);

        assertEquals(naveEspacialResponse.getIdNave(), 1L);
    }

    @Test
    void testEditarNaveEspacial_errorIdNaveNoExiste() {

        NaveEspacialRequest naveEspacialRequest = NaveEspacialRequest.builder()
                .nombre("X-Wing Editado").build();

        assertThrows(ElementoNoEncontradoException.class, () -> {
            navesEspacialesUseCases.editarNaveEspacial(55L, naveEspacialRequest);
            testEntityManager.flush();
        });
    }

    @Test
    void testEditarNaveEspacial_errorIdNaveYaExiste() {

        naveEspacialEntity = NaveEspacialEntity.builder()
                .idNave(2L)
                .nombre("Halcon Milenario").build();
        
        navesEspacialesRepository.save(naveEspacialEntity);
        testEntityManager.flush();

        NaveEspacialRequest naveEspacialRequest = NaveEspacialRequest.builder()
                .nombre("X-Wing").build();

        assertThrows(ConstraintViolationException.class, () -> {
            navesEspacialesUseCases.editarNaveEspacial(2L, naveEspacialRequest);
            testEntityManager.flush();
        });
    }

    @Test
    void testEliminarNaveEspacial_ok() {

        NaveEspacialEliminarResponse naveEspacialEliminarResponse = navesEspacialesUseCases.eliminarNaveEspacial(1L);

        assertEquals(naveEspacialEliminarResponse.getIdNave(), 1L);

        assertEquals(navesEspacialesUseCases.obtenerTodasNavesEspaciales(0, 10).getPagina().getTotalElements(), 0);
    }

    @Test
    void testEliminarNaveEspacial_errorIdNaveNoExiste() {

        assertThrows(ElementoNoEncontradoException.class, () -> {
            navesEspacialesUseCases.eliminarNaveEspacial(55L);
            testEntityManager.flush();
        });

        assertEquals(navesEspacialesUseCases.obtenerTodasNavesEspaciales(0, 10).getPagina().getTotalElements(), 1);
    }

}
