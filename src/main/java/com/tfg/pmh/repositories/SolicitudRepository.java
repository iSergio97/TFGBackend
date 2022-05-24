package com.tfg.pmh.repositories;

import com.tfg.pmh.forms.SolicitudesPorFecha;
import com.tfg.pmh.models.Hoja;
import com.tfg.pmh.models.Operacion;
import com.tfg.pmh.models.Solicitud;
import com.tfg.pmh.services.SolicitudService;
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

    @Query("SELECT S.fecha as fechas, COUNT(S) as cantidades FROM Solicitud S WHERE S.fecha BETWEEN ?1 AND ?2 GROUP BY DAY(S.fecha)")
    List<SolicitudesPorFecha> solicitudesPorFecha(Date start, Date end);
}
