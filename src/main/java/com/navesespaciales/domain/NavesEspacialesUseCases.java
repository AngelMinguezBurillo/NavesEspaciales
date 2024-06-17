package com.navesespaciales.domain;

import com.navesespaciales.domain.model.NaveEspacialBusquedaResponse;
import com.navesespaciales.domain.model.NaveEspacialEliminarResponse;
import com.navesespaciales.domain.model.NaveEspacialRequest;
import com.navesespaciales.domain.model.NaveEspacialResponse;
import com.navesespaciales.domain.model.NavesEspacialesPaginaResponse;

public interface NavesEspacialesUseCases {

    NaveEspacialResponse insertarNaveEspacial(NaveEspacialRequest naveEspacialRequest);
    
    NavesEspacialesPaginaResponse obtenerTodasNavesEspaciales(Integer pagina, Integer elementos);
    
    NaveEspacialResponse obtenerNaveEspacial(Long idNave);
    
    NaveEspacialBusquedaResponse obtenerNaveEspacialPorNombre(String nombre);
    
    NaveEspacialResponse editarNaveEspacial(Long iNnave,  NaveEspacialRequest naveEspacialRequest);
    
    NaveEspacialEliminarResponse eliminarNaveEspacial(Long idNave);

}
