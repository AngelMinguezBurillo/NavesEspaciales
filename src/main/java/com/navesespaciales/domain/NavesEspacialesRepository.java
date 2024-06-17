package com.navesespaciales.domain;

import com.navesespaciales.domain.model.NaveEspacialEntity;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NavesEspacialesRepository extends PagingAndSortingRepository<NaveEspacialEntity, Long>, CrudRepository<NaveEspacialEntity, Long> {
    
    List<NaveEspacialEntity> findByNombreIgnoreCaseContaining(String nombre);

}
