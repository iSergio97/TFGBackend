package com.tfg.pmh.repositories;

import com.tfg.pmh.forms.GrupoSolicitudes;
import com.tfg.pmh.forms.SolicitudesPorFecha;
import com.tfg.pmh.models.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    @Query("SELECT S FROM Solicitud S WHERE S.solicitante.id = ?1 ORDER BY S.fecha DESC")
    List<Solicitud> findSolicitudesBySolicitante(Long id);

    @Query("SELECT S FROM Solicitud S WHERE S.solicitante.id = ?1 AND S.fecha BETWEEN ?2 AND ?3 ORDER BY S.fecha DESC")
    List<Solicitud> findSolicitudesBySolicitanteFiltro(Long id, Date fechaDesde, Date fechaHasta);

    @Query("SELECT S.estado as estado, COUNT(S) as cantidad FROM Solicitud S GROUP BY S.estado")
    List<GrupoSolicitudes> solicitudesPorEstado();

    @Query("SELECT S.estado as estado, COUNT(S) as cantidad FROM Solicitud S WHERE S.fecha BETWEEN ?1 AND ?2 GROUP BY S.estado")
    List<GrupoSolicitudes> solicitudesPorEstadoFiltro(Date fechaDesde, Date fechaHasta);

    @Query("SELECT COUNT(S) FROM Solicitud S")
    Long contadorSolicitudes();

    @Query("SELECT S.fecha as fechas, COUNT(S) as cantidades FROM Solicitud S WHERE S.fecha BETWEEN ?1 AND ?2 GROUP BY DAY(S.fecha)")
    List<SolicitudesPorFecha> solicitudesPorFecha(Date start, Date end);

    @Query("SELECT S FROM Solicitud S WHERE S.estado LIKE 'P' ORDER BY S.fecha DESC")
    List<Solicitud> findAllPendientes();

    @Query("SELECT S FROM Solicitud S WHERE S.estado LIKE ?1 AND S.fecha BETWEEN ?2 AND ?3 ORDER BY S.fecha DESC")
    List<Solicitud> findSolicitudesPorFiltro(String estado, Date desde, Date hasta);

    @Query("SELECT S FROM Solicitud S WHERE S.fecha BETWEEN ?1 AND ?2 ORDER BY S.fecha DESC")
    List<Solicitud> findSolicitudesPorFiltro(Date desde, Date hasta);
}
