package com.tfg.pmh.services;

import com.tfg.pmh.forms.SolicitudesPorFecha;
import com.tfg.pmh.models.Solicitud;
import com.tfg.pmh.repositories.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository repository;

    public void save(Solicitud solicitud) {
        assert solicitud != null;

        this.repository.save(solicitud);
    }

    public Solicitud findById(Long id) { return this.repository.findById(id).orElse(null); }

    public List<Solicitud> findBySolicitante(Long id) { return this.repository.findSolicitudesBySolicitante(id); }

    public List<Solicitud> findAll() { return this.repository.findAll(); }

    public List<Solicitud> findAllPendientes() { return this.repository.findAllPendientes(); }

    public List<Solicitud> findSolicitudesPorFiltro(String estado, Date desde, Date hasta) {
        return this.repository.findSolicitudesPorFiltro(estado, desde, hasta);
    }

    public Long solicitudesAceptadas() {
        return this.repository.solicitudesPorEstado("A");
    }

    public Long solicitudesRechazadas() {
        return this.repository.solicitudesPorEstado("R");
    }

    public Long solicitudesPendiente() {
        return this.repository.solicitudesPorEstado("P");
    }

    public Long solicitudesCanceladas() {
        return this.repository.solicitudesPorEstado("C");
    }

    public Long contadorSolicitudes() {
        return this.repository.contadorSolicitudes();
    }

    public List<SolicitudesPorFecha> solicitudesPorSemana() {
        Calendar start = Calendar.getInstance();
        start.setTime(new Date());
        start.add(Calendar.DAY_OF_MONTH, -7);
        Date end = new Date();
        return this.repository.solicitudesPorFecha(start.getTime(), end);
    }
}

