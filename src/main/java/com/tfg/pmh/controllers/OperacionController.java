package com.tfg.pmh.controllers;

import com.tfg.pmh.models.Habitante;
import com.tfg.pmh.models.Operacion;
import com.tfg.pmh.services.HabitanteService;
import com.tfg.pmh.services.OperacionService;
import com.tfg.pmh.services.ViviendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author: Sergio Garrido Domíngue
 * @version: 1.0
 * @since: 18/11/2020
 */
@RestController
@RequestMapping("/operacion/administrador/")
@CrossOrigin(origins = "http://localhost:8081")
public class OperacionController {

    @Autowired
    private OperacionService operacionService;

    @Autowired
    private HabitanteService habitanteService;

    @Autowired
    private ViviendaService viviendaService;

    /**
     @param operacion
     @param cookieSession
     @return ResponseEntity
     */
    @PostMapping("new")
    public ResponseEntity<Operacion> newOperacion(@ModelAttribute("operacion") Operacion operacion, String cookieSession) {
        try {
            Habitante habitante = habitanteService.findByUsername(cookieSession);
            operacion.setFechaOperacion(new Date(System.currentTimeMillis() - 1));
            operacion.setHabitante(habitante);
            this.operacionService.save(operacion);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
