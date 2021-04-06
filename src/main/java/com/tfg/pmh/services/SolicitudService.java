package com.tfg.pmh.services;

import com.tfg.pmh.forms.SolicitudForm;
import com.tfg.pmh.models.Solicitud;
import com.tfg.pmh.models.Vivienda;
import com.tfg.pmh.repositories.SolicitudRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository repository;

    @Autowired
    private ViviendaService viviendaService;

    @Autowired
    private HabitanteService habitanteService;

    public void save(Solicitud solicitud) {
        assert solicitud != null;

        this.repository.save(solicitud);
    }

    public Solicitud findById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    public List<Solicitud> findBySolicitante(Long id) {
        return this.repository.findSolicitudesBySolicitante(id);
    }
}

