package com.navesespaciales.domain.model;

import static com.navesespaciales.shared.constantes.Errores.ERROR_NOMBRE_INVALID;
import static com.navesespaciales.shared.constantes.Errores.ERROR_NOMBRE_REQUIRED;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NaveEspacialRequest {

    @Schema(description = "Nombre de la nave espacial", name = "nombre", example = "Halcon Milenario")
    @NotEmpty(message = ERROR_NOMBRE_REQUIRED)
    @Size(min = 1, max = 255, message = ERROR_NOMBRE_INVALID)
    private String nombre;
  

}
