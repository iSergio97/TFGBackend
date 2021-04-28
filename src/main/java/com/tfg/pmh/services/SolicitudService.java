package com.tfg.pmh.services;

import com.tfg.pmh.forms.SolicitudForm;
import com.tfg.pmh.models.Solicitud;
import com.tfg.pmh.models.Vivienda;
import com.tfg.pmh.repositories.SolicitudRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository repository;

    @Autowired
    private ViviendaService viviendaService;

    @Autowired
    private IdentificacionService identificacionService;

    public void save(Solicitud solicitud) {
        assert solicitud != null;

        this.repository.save(solicitud);
    }

    public Solicitud findById(Long id) {
        Solicitud solicitud = this.repository.findById(id).orElse(null);
        if(solicitud.getTipoIdentificacion() == null) {
            solicitud.setVivienda(this.viviendaService.findById(solicitud.getVivienda().getId()));
        }
        if(solicitud.getVivienda() == null) {
            solicitud.setTipoIdentificacion(this.identificacionService.findByid(solicitud.getTipoIdentificacion().getId()));
        }

        return solicitud;
    }

    public List<Solicitud> findBySolicitante(Long id) {
        return this.repository.findSolicitudesBySolicitante(id);
    }

    public List<Solicitud> findAll() { return this.repository.findAll(); }

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

    public Object[] solicitudesPorMes() {
        return this.repository.solicitudesPorFecha();
    }
}

