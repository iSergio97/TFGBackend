package com.tfg.pmh.repositories;

import com.tfg.pmh.models.Vivienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

import org.springframework.stereotype.Repository;

@Repository
public interface ViviendaRepository extends JpaRepository<Vivienda, Long> {

    @Query("SELECT V FROM Vivienda V WHERE V.calle = ?1 and V.municipio = 'ECIJA' and V.pais = 'ESPAÑA' and V.provincia = 'SEVILLA'")
    Collection<Vivienda> findViviendasByCalle(String calle);

    @Query("SELECT V FROM Vivienda V WHERE V.calle = ?1 and V.numero = ?2 and V.municipio = 'ECIJA' and V.pais = 'ESPAÑA' and V.provincia = 'SEVILLA'")
    Vivienda findViviendaByCalleYNumero(String calle, Integer numero);
}
