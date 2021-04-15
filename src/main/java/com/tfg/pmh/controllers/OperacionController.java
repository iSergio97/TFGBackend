package com.tfg.pmh.controllers;

import com.tfg.pmh.models.Habitante;
import com.tfg.pmh.models.Operacion;
import com.tfg.pmh.models.Respuesta;
import com.tfg.pmh.services.HabitanteService;
import com.tfg.pmh.services.OperacionService;
import com.tfg.pmh.services.ViviendaService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("list")
    public Respuesta listaOperaciones() {
        Respuesta res;
        try {
            List<Operacion> lista = this.operacionService.findAll();
            res = new Respuesta(200, lista);
        } catch (Exception e) {
            // EXCEPCIÓN NO CONTROLADA
            res = new Respuesta(400, null);
        }
        return res;
    }

    @GetMapping("/{id}")
    public Respuesta operacionById(@PathVariable Long id) {
        Respuesta res;
        try {
            Operacion operacion = this.operacionService.findById(id);
            res = new Respuesta(200, operacion);
        } catch (Exception e) {
            res = new Respuesta(200, null);
        }
        return res;
    }
}
