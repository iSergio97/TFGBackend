package com.tfg.pmh.repositories;

import com.tfg.pmh.models.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    @Query("SELECT S FROM Solicitud S WHERE S.solicitante.id = ?1")
    List<Solicitud> findSolicitudesBySolicitante(Long id);
}
