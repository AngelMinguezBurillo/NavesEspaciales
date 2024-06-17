package com.navesespaciales.domain.model;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Cacheable
@Table(name = "NAVES_ESPACIALES")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NaveEspacialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idNave;

    @Column(name = "NOMBRE", length = 255, nullable = false, unique = true)
    private String nombre;

}
