package com.navesespaciales.infra.endpoint;

import com.navesespaciales.domain.NavesEspacialesUseCases;
import com.navesespaciales.domain.model.NaveEspacialBusquedaResponse;
import com.navesespaciales.domain.model.NaveEspacialEliminarResponse;
import com.navesespaciales.domain.model.NaveEspacialRequest;
import com.navesespaciales.domain.model.NaveEspacialResponse;
import com.navesespaciales.domain.model.NavesEspacialesPaginaResponse;
import static com.navesespaciales.shared.constantes.ConstantesAuth.AUTH_SCHEMA_APIKEY;
import static com.navesespaciales.shared.constantes.ConstantesSwagger.ACCESO_NO_PERMTIDO;
import static com.navesespaciales.shared.constantes.Errores.ERROR_ELEMENTOS_INVALID;
import static com.navesespaciales.shared.constantes.Errores.ERROR_IDNAVE_INVALID;
import static com.navesespaciales.shared.constantes.Errores.ERROR_IDNAVE_REQUIRED;
import static com.navesespaciales.shared.constantes.Errores.ERROR_NOMBRE_INVALID;
import static com.navesespaciales.shared.constantes.Errores.ERROR_PAGINA_INVALID;
import static com.navesespaciales.shared.constantes.HttpMethods.DELETE_METHOD;
import static com.navesespaciales.shared.constantes.HttpMethods.GET_METHOD;
import static com.navesespaciales.shared.constantes.HttpMethods.POST_METHOD;
import static com.navesespaciales.shared.constantes.HttpMethods.PUT_METHOD;
import static com.navesespaciales.shared.constantes.ResponseCodes.HTTP_CODE_BAD_REQUEST;
import static com.navesespaciales.shared.constantes.ResponseCodes.HTTP_CODE_CONFLICT;
import static com.navesespaciales.shared.constantes.ResponseCodes.HTTP_CODE_CREATED;
import static com.navesespaciales.shared.constantes.ResponseCodes.HTTP_CODE_FORBIDDEN;
import static com.navesespaciales.shared.constantes.ResponseCodes.HTTP_CODE_NOT_FOUND;
import static com.navesespaciales.shared.constantes.ResponseCodes.HTTP_CODE_OK;
import static com.navesespaciales.shared.constantes.ResponseCodes.HTTP_CODE_UNPROCESSABLE;
import com.navesespaciales.shared.domain.model.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Naves Espaciales", description = "Operaciones CRUD sobre Naves Espaciales")
@RestController
@RequiredArgsConstructor
@Validated
public class NavesEspacialesEndpoint {

    private final NavesEspacialesUseCases navesEspacialesUseCases;

    @Operation(
            method = GET_METHOD,
            security = {
                @SecurityRequirement(name = AUTH_SCHEMA_APIKEY)},
            description = "Obtiene el listado completo de naves espaciales"
    )
    @ApiResponse(responseCode = HTTP_CODE_OK, description = "Pagina de naves espaciales", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = NavesEspacialesPaginaResponse.class)))
    @ApiResponse(responseCode = HTTP_CODE_BAD_REQUEST, description = "Parametros no validos", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = HTTP_CODE_FORBIDDEN, description = ACCESO_NO_PERMTIDO, content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = HTTP_CODE_UNPROCESSABLE, description = "Reglas no permitidas", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @GetMapping(value = "/navesespaciales/todas", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NavesEspacialesPaginaResponse> obtenerTodasNavesEspaciales(
            final HttpServletRequest request,
            @Schema(description = "Numero de la pagina a buscar. Si no se indica pagina se devuelve la primera", name = "pagina", example = "2")
            @Min(value = 0, message = ERROR_PAGINA_INVALID)
            @RequestParam(name = "pagina", defaultValue = "0") Integer pagina,
            @Schema(description = "Numero de elementos por pagina. Valor por defecto 10", name = "elementos", example = "25")
            @Min(value = 1, message = ERROR_ELEMENTOS_INVALID)
            @Max(value = 100, message = ERROR_ELEMENTOS_INVALID)
            @RequestParam(name = "elementos", defaultValue = "10") Integer elementos) {

        return ResponseEntity.ok(navesEspacialesUseCases.obtenerTodasNavesEspaciales(pagina, elementos));
    }
    
    @Operation(
            method = GET_METHOD,
            security = {
                @SecurityRequirement(name = AUTH_SCHEMA_APIKEY)},
            description = "Obtiene la nave espacial con el id indicado"
    )
    @ApiResponse(responseCode = HTTP_CODE_OK, description = "Nave espacial seleccionada con el id indicado", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = NaveEspacialBusquedaResponse.class)))
    @ApiResponse(responseCode = HTTP_CODE_BAD_REQUEST, description = "Parametros no validos", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = HTTP_CODE_NOT_FOUND, description = "Elemento no encontrado", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = HTTP_CODE_FORBIDDEN, description = ACCESO_NO_PERMTIDO, content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = HTTP_CODE_UNPROCESSABLE, description = "Reglas no permitidas", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @GetMapping(value = "/navesespaciales/{idNave}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NaveEspacialResponse> obtenerNaveEspacial(
            final HttpServletRequest request,
            @Parameter(name = "idNave", description = "Identificador de la nave espacial", required = true, example = "1")
            @NotNull(message = ERROR_IDNAVE_REQUIRED)
            @Positive(message = ERROR_IDNAVE_INVALID)
            @PathVariable final Long idNave) {

        return ResponseEntity.ok(navesEspacialesUseCases.obtenerNaveEspacial(idNave));
    }
    
    @Operation(
            method = GET_METHOD,
            security = {
                @SecurityRequirement(name = AUTH_SCHEMA_APIKEY)},
            description = "Obtiene la nave espacial con el nombre indicado"
    )
    @ApiResponse(responseCode = HTTP_CODE_OK, description = "Nave espacial seleccionada con el nombre indicado", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = NaveEspacialBusquedaResponse.class)))
    @ApiResponse(responseCode = HTTP_CODE_BAD_REQUEST, description = "Parametros no validos", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = HTTP_CODE_FORBIDDEN, description = ACCESO_NO_PERMTIDO, content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = HTTP_CODE_UNPROCESSABLE, description = "Reglas no permitidas", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @GetMapping(value = "/navesespaciales/nombre", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NaveEspacialBusquedaResponse> obtenerNaveEspacialPorNombre(
            final HttpServletRequest request,
            @Schema(description = "Nombre completo o parcial de la nave a buscar", name = "nombre", example = "Wing")
            @Size(min = 1, max = 255, message = ERROR_NOMBRE_INVALID)
            @RequestParam(name = "nombre") String nombre) {

        return ResponseEntity.ok(navesEspacialesUseCases.obtenerNaveEspacialPorNombre(nombre));
    }

    @Operation(
            method = POST_METHOD,
            security = {
                @SecurityRequirement(name = AUTH_SCHEMA_APIKEY)},
            description = "Inserta una nave espacial"
    )
    @ApiResponse(responseCode = HTTP_CODE_CREATED, description = "Insercion correcta", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = NaveEspacialResponse.class)))
    @ApiResponse(responseCode = HTTP_CODE_BAD_REQUEST, description = "Parametros no validos", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = HTTP_CODE_CONFLICT, description = "Elemento ya existe", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = HTTP_CODE_FORBIDDEN, description = ACCESO_NO_PERMTIDO, content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = HTTP_CODE_UNPROCESSABLE, description = "Reglas no permitidas", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @PostMapping(value = "/navesespaciales", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NaveEspacialResponse> insertarNaveEspacial(
            final HttpServletRequest request,
            @Valid @RequestBody final NaveEspacialRequest naveEspacialRequest) {

        return new ResponseEntity<>(navesEspacialesUseCases.insertarNaveEspacial(naveEspacialRequest), HttpStatus.CREATED);
    }
    
    @Operation(
            method = PUT_METHOD,
            security = {
                @SecurityRequirement(name = AUTH_SCHEMA_APIKEY)},
            description = "Edita una nave espacial"
    )
    @ApiResponse(responseCode = HTTP_CODE_OK, description = "Insercion correcta", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = NaveEspacialResponse.class)))
    @ApiResponse(responseCode = HTTP_CODE_BAD_REQUEST, description = "Parametros no validos", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = HTTP_CODE_NOT_FOUND, description = "Elemento no encontrado", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = HTTP_CODE_FORBIDDEN, description = ACCESO_NO_PERMTIDO, content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = HTTP_CODE_UNPROCESSABLE, description = "Reglas no permitidas", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @PutMapping(value = "/navesespaciales/{idNave}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NaveEspacialResponse> editarNaveEspacial(
            final HttpServletRequest request,
            @Parameter(name = "idNave", description = "Identificador de la nave espacial", required = true, example = "1")
            @NotNull(message = ERROR_IDNAVE_REQUIRED)
            @Positive(message = ERROR_IDNAVE_INVALID)
            @PathVariable final Long idNave,
            @Valid @RequestBody final NaveEspacialRequest naveEspacialRequest) {

        return ResponseEntity.ok(navesEspacialesUseCases.editarNaveEspacial(idNave, naveEspacialRequest));
    }
    
    @Operation(
            method = DELETE_METHOD,
            security = {
                @SecurityRequirement(name = AUTH_SCHEMA_APIKEY)},
            description = "Elimina una nave espacial"
    )
    @ApiResponse(responseCode = HTTP_CODE_OK, description = "Insercion correcta", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = NaveEspacialEliminarResponse.class)))
    @ApiResponse(responseCode = HTTP_CODE_BAD_REQUEST, description = "Parametros no validos", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = HTTP_CODE_NOT_FOUND, description = "Elemento no encontrado", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = HTTP_CODE_FORBIDDEN, description = ACCESO_NO_PERMTIDO, content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = HTTP_CODE_UNPROCESSABLE, description = "Reglas no permitidas", content = @Content(mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiError.class)))
    @DeleteMapping(value = "/navesespaciales/{idNave}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NaveEspacialEliminarResponse> eliminarNaveEspacial(
            final HttpServletRequest request,
            @Parameter(name = "idNave", description = "Identificador de la nave espacial", required = true, example = "1")
            @NotNull(message = ERROR_IDNAVE_REQUIRED)
            @Positive(message = ERROR_IDNAVE_INVALID)
            @PathVariable final Long idNave) {

        return ResponseEntity.ok(navesEspacialesUseCases.eliminarNaveEspacial(idNave));
    }

}
