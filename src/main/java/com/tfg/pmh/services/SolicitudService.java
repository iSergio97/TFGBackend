package com.tfg.pmh.services;

import com.tfg.pmh.forms.SolicitudForm;
import com.tfg.pmh.models.Solicitud;
import com.tfg.pmh.models.Vivienda;
import com.tfg.pmh.repositories.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

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

        Solicitud solicitud = new Solicitud();
        solicitud.setFecha(new Date());
        solicitud.setSolicitante(this.habitanteService.findById(solicitudForm.getSolicitanteID()));
        solicitud.setSolicitaPor(this.habitanteService.findById(solicitudForm.getSolicitaPorID()));
        solicitud.setIdentificacion(solicitudForm.getIdentificacion());
        solicitud.setNombre(solicitudForm.getNombre());
        solicitud.setPrimerApellido(solicitudForm.getPrimerApellido());
        solicitud.setSegundoApellido(solicitudForm.getSegundoApellido());
        solicitud.setViviendaNueva(viviendaService.findViviendasByCalleYNumero(solicitudForm.getCalle(), solicitudForm.getNumero()));
        solicitud.setFecha(solicitudForm.getFechaNacimiento());

        return solicitud;
    }
}

