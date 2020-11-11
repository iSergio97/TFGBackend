package com.tfg.pmh.repositories;

import com.tfg.pmh.models.Vivienda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViviendaRepository extends JpaRepository<Vivienda, Long> {
    //TODO: Query para buscar vivienda por ciertos campos
    // TODO: Si se pasan todos los parámetros, emplear query con varios AND
    // TODO: En caso contrario, plantear queries por trozos
}
