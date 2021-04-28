package com.tfg.pmh.repositories;

import com.tfg.pmh.models.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    @Query("SELECT S FROM Solicitud S WHERE S.solicitante.id = ?1")
    List<Solicitud> findSolicitudesBySolicitante(Long id);

    @Query("SELECT COUNT(S) FROM Solicitud S WHERE S.estado = ?1")
    Long solicitudesPorEstado(String estado);

    @Query("SELECT COUNT(S) FROM Solicitud S")
    Long contadorSolicitudes();

    @Query("SELECT S.fecha, COUNT(S) FROM Solicitud S GROUP BY MONTH(S.fecha)")
    Object[] solicitudesPorFecha();
}
