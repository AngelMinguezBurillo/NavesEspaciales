package com.navesespaciales.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NaveEspacialResponse {

    @Schema(description = "Identificador de la nave espacial", name = "idNave", example = "23")
    private Long idNave;
    @Schema(description = "Nombre de la nave espacial", name = "nombre", example = "Enterprise")
    private String nombre;

}
