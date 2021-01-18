package com.tfg.pmh.controllers;

import com.tfg.pmh.forms.SolicitudForm;
import com.tfg.pmh.models.Habitante;
import com.tfg.pmh.models.Respuesta;
import com.tfg.pmh.models.Solicitud;
import com.tfg.pmh.services.HabitanteService;
import com.tfg.pmh.services.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/solicitud")
public class SolicitudController {

    @Autowired
    private HabitanteService habitanteService;

    @Autowired
    private SolicitudService service;
    // Métodos para los habitantes

    //@RequestBody Habitante habitante
    @PostMapping("/habitante/new")
    public Respuesta prueba(@RequestBody SolicitudForm solicitud) {
        Respuesta respuesta = new Respuesta();
        // Revisar qué status pondremos para BadRequest en el parámetro
        respuesta.setStatus(350);
        try {
            Solicitud solicitudBD = this.service.deconstruct(solicitud);

            // Error a la hora de guardar. Revisar mañana
            this.service.save(solicitudBD);
            respuesta.setObject(solicitudBD);
            respuesta.setStatus(200);
            return respuesta;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println(e.getCause());
            return respuesta;
        }
    }

    @GetMapping("/h")
    public SolicitudForm test() {
        return new SolicitudForm();
    }

    // Métodos para los administradores
}
