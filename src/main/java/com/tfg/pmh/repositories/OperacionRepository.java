package com.tfg.pmh.repositories;


import com.tfg.pmh.models.Operacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import org.springframework.stereotype.Repository;

@Repository
public interface OperacionRepository extends JpaRepository<Operacion, Long> {

    @Query("SELECT O FROM Operacion O WHERE O.habitante = ?1")
    Collection<Operacion> findOpsByHabitanteId(Long id);

    @Query("SELECT COUNT(DISTINCT O.habitante) FROM Operacion O WHERE O.tipo = 'A' AND O.subtipo = ?1")
    Integer getHabsAltaPorSubtipo(String tipo);

    @Query("SELECT COUNT(DISTINCT O.habitante ) FROM Operacion O WHERE O.tipo = 'B' AND O.subtipo = ?1")
    Integer getHabsBajaPorSubtipo(String tipo);

    @Query("SELECT COUNT(DISTINCT O.habitante) FROM Operacion O WHERE O.tipo = 'M' AND O.subtipo = ?1")
    Integer getHabsModificacionPorSubtipo(String tipo);

    @Query("SELECT COUNT(O) FROM Operacion O")
    Integer ratioOperacionesPorSolicitud();
}
