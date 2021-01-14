package com.tfg.pmh.services;

import com.tfg.pmh.forms.SolicitudForm;
import com.tfg.pmh.models.Solicitud;
import com.tfg.pmh.repositories.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Date;
import java.util.NoSuchElementException;

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

    public Collection<Solicitud> findBySolicitante(Long id) {
        return this.repository.findSolicitudesBySolicitante(id);
    }

    public Solicitud deconstruct(SolicitudForm solicitudForm) {

        // En la solicitud no es donde se cambian los datos, es en la operación. Ahí se debe comprobar si hay silicitadoPor.
        // Si lo hay, cambiar los datos del solicitadoPor
        Solicitud solicitud = new Solicitud();
        solicitud.setFecha(new Date());
        solicitud.setSolicitante(this.habitanteService.findById(solicitudForm.getSolicitanteID()));
        solicitud.setSolicitaPor(this.habitanteService.findById(solicitudForm.getSolicitaPorID()));
        solicitud.setDni(solicitudForm.getDni());
        solicitud.setNombre(solicitudForm.getNombre());
        solicitud.setApellidos(solicitudForm.getApellidos());
        solicitud.setViviendaNueva(viviendaService.findById(solicitudForm.getViviendaNuevaID()));
        // Revisar si fuera necesario un parse
        solicitud.setFecha(solicitudForm.getFechaNacimiento());
        return solicitud;
    }
}

