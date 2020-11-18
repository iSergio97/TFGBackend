package com.tfg.pmh.repositories;

import com.tfg.pmh.models.Vivienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface ViviendaRepository extends JpaRepository<Vivienda, Long> {
    //TODO: Query para buscar vivienda por ciertos campos
    // TODO: Si se pasan todos los parámetros, emplear query con varios AND
    // TODO: En caso contrario, plantear queries por trozos

    @Query("SELECT V FROM Vivienda V WHERE (V.distrito = ?1 OR V.calle = ?2) AND V.municipio = 'ECIJA'")
    Collection<Vivienda> findViviendasByDistritoOrCalle(String distrito, String calle);

    @Query("SELECT V FROM Vivienda V WHERE V.calle = ?1 and V.municipio = 'ECIJA'")
    Collection<Vivienda> findViviendasByCalle(String calle);
}
