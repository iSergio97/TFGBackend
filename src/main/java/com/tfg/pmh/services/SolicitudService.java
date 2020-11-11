package com.tfg.pmh.services;

import com.tfg.pmh.models.Solicitud;
import com.tfg.pmh.repositories.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository repository;

    public void save(Solicitud solicitud) {
        assert solicitud != null;

        this.repository.save(solicitud);
    }

    public Solicitud findById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    public Collection<Solicitud> findBySolicitante(Long id) {
        return this.repository.findSolicitudesBySolicitante(id);
    }


}

