package com.tfg.pmh.repositories;

import com.tfg.pmh.models.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    @Query("SELECT S FROM Solicitud S WHERE S.solicitante = ?1 OR S.solicitaPor = ?1")
    Collection<Solicitud> findSolicitudesBySolicitante(Long id);
}
