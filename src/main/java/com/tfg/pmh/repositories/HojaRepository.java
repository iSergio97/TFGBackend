package com.tfg.pmh.repositories;

import com.tfg.pmh.models.Hoja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HojaRepository extends JpaRepository<Hoja, Long> {

    @Query("SELECT H FROM Hoja H WHERE H.numeracion.id = ?1")
    List<Hoja> findByNumeracion(Long id);

    @Query("SELECT MAX(H.hoja) FROM Hoja H WHERE H.numeracion.id = ?1")
    Integer maxHojaByNumeracion(Long id);
}
