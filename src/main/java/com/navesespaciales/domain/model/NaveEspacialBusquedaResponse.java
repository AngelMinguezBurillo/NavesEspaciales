package com.navesespaciales.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NaveEspacialBusquedaResponse {

    @Schema(description = "Listado de naves que corresponden a la busqueda realizada", name = "idNave", type="object", example = "23")
    private List<NaveEspacialResponse> resultado;


}
