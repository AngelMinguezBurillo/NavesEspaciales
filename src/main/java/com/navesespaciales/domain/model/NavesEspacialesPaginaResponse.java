package com.navesespaciales.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NavesEspacialesPaginaResponse {

    @Schema(description = "Identificador de la nave espacial que ha sido insertada", name = "idNave", example = "23")
    private Page pagina;

}
