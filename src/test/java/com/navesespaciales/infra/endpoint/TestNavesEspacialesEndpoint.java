package com.navesespaciales.infra.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navesespaciales.domain.NavesEspacialesUseCases;
import com.navesespaciales.domain.model.NaveEspacialBusquedaResponse;
import com.navesespaciales.domain.model.NaveEspacialEliminarResponse;
import com.navesespaciales.domain.model.NaveEspacialRequest;
import com.navesespaciales.domain.model.NaveEspacialResponse;
import com.navesespaciales.domain.model.NavesEspacialesPaginaResponse;
import com.navesespaciales.shared.domain.model.ApiError;
import com.navesespaciales.shared.infra.spring.security.ApiKeyExtractor;
import com.navesespaciales.shared.infra.spring.security.ApiKeyFilter;
import com.navesespaciales.shared.infra.spring.MessagesResourceService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.isA;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.security.apikey=12345")
@Import({ApiKeyFilter.class, ApiKeyExtractor.class, MessagesResourceService.class})
class TestNavesEspacialesEndpoint {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NavesEspacialesUseCases navesEspacialesUseCases;

    @Test
    void insertarNavesEspaciales_ok() throws Exception {

        final var request = NaveEspacialRequest.builder().nombre("Halcon Milenario").build();

        doReturn(NaveEspacialResponse.builder().build()).when(navesEspacialesUseCases).insertarNaveEspacial(request);

        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/navesespaciales")
                .header("ApiKey", "12345")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        final var respuesta = objectMapper.readValue(result.getResponse().getContentAsString(), NaveEspacialResponse.class);

        assertThat(respuesta, isA(NaveEspacialResponse.class));
    }

    @Test
    void insertarNavesEspaciales_errorApiKeyNoAutorizado() throws Exception {

        final var request = NaveEspacialRequest.builder().nombre("Halcon Milenario").build();

        mvc.perform(MockMvcRequestBuilders
                .post("/navesespaciales")
                .header("ApiKey", "xxxxx")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andReturn();
    }

    @Test
    void insertarNavesEspaciales_errorNombreRequiredBadRequest() throws Exception {

        final var request = NaveEspacialRequest.builder().build();

        doReturn(NaveEspacialResponse.builder().build()).when(navesEspacialesUseCases).insertarNaveEspacial(request);

        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/navesespaciales")
                .header("ApiKey", "12345")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();

        final var respuesta = objectMapper.readValue(result.getResponse().getContentAsString(), ApiError.class);

        assertEquals(respuesta.getFieldsErrors().size(), 1);
        assertEquals(respuesta.getFieldsErrors().get(0).getField(), "nombre");
        assertEquals(respuesta.getFieldsErrors().get(0).getMessage(), "El nombre de la nave esapacial es obligatorio");
    }

    @Test
    void obtenerTodasNavesEspaciales_ok() throws Exception {

        final Integer pagina = 0;
        final Integer elementos = 10;

        doReturn(NavesEspacialesPaginaResponse.builder().build()).when(navesEspacialesUseCases).obtenerTodasNavesEspaciales(pagina, elementos);

        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/navesespaciales/todas")
                .header("ApiKey", "12345")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        final var respuesta = objectMapper.readValue(result.getResponse().getContentAsString(), NavesEspacialesPaginaResponse.class);

        assertThat(respuesta, isA(NavesEspacialesPaginaResponse.class));
    }

    @Test
    void obtenerTodasNavesEspaciales_errorApiKeyNoAutorizado() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .get("/navesespaciales/todas")
                .header("ApiKey", "xxxxx")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andReturn();
    }

    @Test
    void obtenerTodasNavesEspaciales_errorPaginaNoValida() throws Exception {

        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/navesespaciales/todas?pagina=-1")
                .header("ApiKey", "12345")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        final var respuesta = objectMapper.readValue(result.getResponse().getContentAsString(), ApiError.class);

        assertEquals(respuesta.getFieldsErrors().size(), 1);
        assertEquals(respuesta.getFieldsErrors().get(0).getField(), "obtenerTodasNavesEspaciales.pagina");
        assertEquals(respuesta.getFieldsErrors().get(0).getMessage(), "El numero de pagina no es valido");
    }

    @Test
    void obtenerTodasNavesEspaciales_errorElementosNoValidos_limiteInferior() throws Exception {

        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/navesespaciales/todas?pagina=0&elementos=0")
                .header("ApiKey", "12345")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        final var respuesta = objectMapper.readValue(result.getResponse().getContentAsString(), ApiError.class);

        assertEquals(respuesta.getFieldsErrors().size(), 1);
        assertEquals(respuesta.getFieldsErrors().get(0).getField(), "obtenerTodasNavesEspaciales.elementos");
        assertEquals(respuesta.getFieldsErrors().get(0).getMessage(), "El numero de elementos por pagina no es valido, debe ser estar entre 1 y 100");
    }

    @Test
    void obtenerTodasNavesEspaciales_errorElementosNoValidos_limiteSuperior() throws Exception {

        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/navesespaciales/todas?pagina=0&elementos=150")
                .header("ApiKey", "12345")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        final var respuesta = objectMapper.readValue(result.getResponse().getContentAsString(), ApiError.class);

        assertEquals(respuesta.getFieldsErrors().size(), 1);
        assertEquals(respuesta.getFieldsErrors().get(0).getField(), "obtenerTodasNavesEspaciales.elementos");
        assertEquals(respuesta.getFieldsErrors().get(0).getMessage(), "El numero de elementos por pagina no es valido, debe ser estar entre 1 y 100");
    }

    @Test
    void obtenerNaveEspacial_ok() throws Exception {

        doReturn(NaveEspacialResponse.builder().build()).when(navesEspacialesUseCases).obtenerNaveEspacial(1L);

        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/navesespaciales/1")
                .header("ApiKey", "12345")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        final var respuesta = objectMapper.readValue(result.getResponse().getContentAsString(), NaveEspacialResponse.class);

        assertThat(respuesta, isA(NaveEspacialResponse.class));
    }

    @Test
    void obtenerNaveEspacial_errorApiKeyNoAutorizado() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .get("/navesespaciales/1")
                .header("ApiKey", "xxxxx")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andReturn();
    }

    @Test
    void obtenerNaveEspacial_errorIdNaveNoValido() throws Exception {

        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/navesespaciales/-1")
                .header("ApiKey", "12345")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        final var respuesta = objectMapper.readValue(result.getResponse().getContentAsString(), ApiError.class);

        assertEquals(respuesta.getFieldsErrors().size(), 1);
        assertEquals(respuesta.getFieldsErrors().get(0).getField(), "obtenerNaveEspacial.idNave");
        assertEquals(respuesta.getFieldsErrors().get(0).getMessage(), "El identificador de la nave es invalido");
    }

    @Test
    void obtenerNaveEspacialPorNombre_ok() throws Exception {

        doReturn(NaveEspacialBusquedaResponse.builder().build()).when(navesEspacialesUseCases).obtenerNaveEspacialPorNombre("Wing");

        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/navesespaciales/nombre?nombre=Wing")
                .header("ApiKey", "12345")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        final var respuesta = objectMapper.readValue(result.getResponse().getContentAsString(), NaveEspacialBusquedaResponse.class);

        assertThat(respuesta, isA(NaveEspacialBusquedaResponse.class));
    }
    
    @Test
    void obtenerNaveEspacialPorNombre_errorApiKeyNoAutorizado() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .get("/navesespaciales/nombre?nombre=Wing")
                .header("ApiKey", "xxxxx")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }
    
    @Test
    void obtenerNaveEspacialPorNombre_errorElementosNoValidos_limiteInferior() throws Exception {

        doReturn(NaveEspacialBusquedaResponse.builder().build()).when(navesEspacialesUseCases).obtenerNaveEspacialPorNombre("");

        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/navesespaciales/nombre?nombre=")
                .header("ApiKey", "12345")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        final var respuesta = objectMapper.readValue(result.getResponse().getContentAsString(), ApiError.class);

        assertEquals(respuesta.getFieldsErrors().size(), 1);
        assertEquals(respuesta.getFieldsErrors().get(0).getField(), "obtenerNaveEspacialPorNombre.nombre");
        assertEquals(respuesta.getFieldsErrors().get(0).getMessage(), "El nombre de la nave esapacial no es valido. Debe estar entre 1 y 255 caracteres");
    }
    
    @Test
    void obtenerNaveEspacialPorNombre_errorElementosNoValidos_limiteSuperior() throws Exception {
        
        final String parametroBusqueda = StringUtils.leftPad("Wing", 300, '-');

        doReturn(NaveEspacialBusquedaResponse.builder().build()).when(navesEspacialesUseCases).obtenerNaveEspacialPorNombre(parametroBusqueda);

        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/navesespaciales/nombre?nombre=" + parametroBusqueda)
                .header("ApiKey", "12345")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        final var respuesta = objectMapper.readValue(result.getResponse().getContentAsString(), ApiError.class);

        assertEquals(respuesta.getFieldsErrors().size(), 1);
        assertEquals(respuesta.getFieldsErrors().get(0).getField(), "obtenerNaveEspacialPorNombre.nombre");
        assertEquals(respuesta.getFieldsErrors().get(0).getMessage(), "El nombre de la nave esapacial no es valido. Debe estar entre 1 y 255 caracteres");
    }
    
    @Test
    void editarNaveEspacial_ok() throws Exception {

        final Long idNave = 1L;
        final var request = NaveEspacialRequest.builder().nombre("Halcon Milenario").build();

        doReturn(NaveEspacialResponse.builder().build()).when(navesEspacialesUseCases).editarNaveEspacial(idNave, request);

        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .put("/navesespaciales/1")
                .header("ApiKey", "12345")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        final var respuesta = objectMapper.readValue(result.getResponse().getContentAsString(), NaveEspacialResponse.class);

        assertThat(respuesta, isA(NaveEspacialResponse.class));
    }
    
    @Test
    void editarNaveEspacial_errorApiKeyNoAutorizado() throws Exception {

        final var request = NaveEspacialRequest.builder().nombre("Halcon Milenario").build();

        mvc.perform(MockMvcRequestBuilders
                .put("/navesespaciales/1")
                .header("ApiKey", "xxxxx")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }
    
    @Test
    void editarNavesEspaciales_errorNombreRequiredBadRequest() throws Exception {

        final Long idNave = 1L;
        final var request = NaveEspacialRequest.builder().build();

        doReturn(NaveEspacialResponse.builder().build()).when(navesEspacialesUseCases).editarNaveEspacial(idNave, request);

        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .put("/navesespaciales/1")
                .header("ApiKey", "12345")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();

        final var respuesta = objectMapper.readValue(result.getResponse().getContentAsString(), ApiError.class);

        assertEquals(respuesta.getFieldsErrors().size(), 1);
        assertEquals(respuesta.getFieldsErrors().get(0).getField(), "nombre");
        assertEquals(respuesta.getFieldsErrors().get(0).getMessage(), "El nombre de la nave esapacial es obligatorio");
    }
    
    @Test
    void eliminarNaveEspacial_ok() throws Exception {

        doReturn(NaveEspacialEliminarResponse.builder().build()).when(navesEspacialesUseCases).eliminarNaveEspacial(1L);

        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .delete("/navesespaciales/1")
                .header("ApiKey", "12345")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        final var respuesta = objectMapper.readValue(result.getResponse().getContentAsString(), NaveEspacialEliminarResponse.class);

        assertThat(respuesta, isA(NaveEspacialEliminarResponse.class));
    }
    
    @Test
    void eliminarNaveEspacial_errorApiKeyNoAutorizado() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .delete("/navesespaciales/1")
                .header("ApiKey", "xxxxx")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();

    }

}
