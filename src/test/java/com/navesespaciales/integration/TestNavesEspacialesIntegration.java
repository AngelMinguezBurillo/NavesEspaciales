package com.navesespaciales.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navesespaciales.domain.model.NaveEspacialRequest;
import com.navesespaciales.domain.model.NaveEspacialResponse;
import static com.navesespaciales.shared.constantes.ConstantesAuth.AUTH_HEADER;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = "spring.security.apikey=12345")
class TestNavesEspacialesIntegration {

    @LocalServerPort
    private int port;
    
    @Value("${server.servlet.context-path}")
    private String contextPath;
    
    @Autowired
    private TestRestTemplate testRestTemplate;

    private static HttpHeaders headers;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void insertarNavesEspaciales_ok() throws Exception {
        
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(AUTH_HEADER, "12345");
        
        String peticion = objectMapper.writeValueAsString(NaveEspacialRequest.builder().nombre("Test Nombre").build());
        
        HttpEntity<String> entity = new HttpEntity<>(peticion, headers);
        ResponseEntity<NaveEspacialResponse> response = testRestTemplate.exchange(
            crearURL() + "/navesespaciales", HttpMethod.POST, entity, NaveEspacialResponse.class);
        
        NaveEspacialResponse naveEspacialResponse = response.getBody();
        
        assert naveEspacialResponse != null;
        
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals("Test Nombre", response.getBody().getNombre());
    }
    

    private String crearURL() {
        return "http://localhost:" + port + contextPath;
    }

}
