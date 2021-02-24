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

        solicitud.setTipo(solicitudForm.getTipo());
        solicitud.setSubtipo(solicitudForm.getSubtipo());
        solicitud.setFecha(new Date());
        solicitud.setSolicitante(this.habitanteService.findById(solicitudForm.getSolicitanteID()));
        solicitud.setSolicitaPor(this.habitanteService.findById(solicitudForm.getSolicitaPorID()));
        solicitud.setIdentificacion(solicitudForm.getIdentificacion()); // TODO: Realizar modificación para indicar nuevo tipo de identificación en la operación
        solicitud.setNombre(solicitudForm.getNombre());
        solicitud.setPrimerApellido(solicitudForm.getPrimerApellido());
        solicitud.setSegundoApellido(solicitudForm.getSegundoApellido());
        Vivienda vivienda;
        if("A".equals(solicitudForm.getTipo()) || "MD".equals(solicitudForm.getSubtipo())) {
            vivienda = viviendaService.findById(solicitudForm.getViviendaId());
        } else {
            vivienda = new Vivienda();
        }

        vivienda.setPais(solicitudForm.getPais());
        vivienda.setProvincia(solicitudForm.getProvincia());
        vivienda.setMunicipio(solicitudForm.getMunicipio());
        vivienda.setCalle(solicitudForm.getCalle());
        vivienda.setNumero(solicitudForm.getNumero());

        solicitud.setViviendaNueva(vivienda);
        int anho = solicitudForm.getFechaNacimiento().getYear();
        int month = solicitudForm.getFechaNacimiento().getMonth();
        int day = solicitudForm.getFechaNacimiento().getDate();

        LocalDate birthDay = LocalDate.of(anho, month, day);
        Boolean aceptadoPorSolicitado = Period.between(birthDay, LocalDate.now()).getYears() > 18;
        solicitud.setAceptadoPorSolicitado(aceptadoPorSolicitado);
        solicitud.setFechaNacimiento(solicitudForm.getFechaNacimiento());

        return solicitud;
    }
}

