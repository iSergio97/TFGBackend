package com.tfg.pmh.controllers;

import com.tfg.pmh.services.OperacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RequestMapping("/estadisticas")
@RestController
@CrossOrigin(origins = {"*"})
public class EstadisticasController {

    @Autowired
    private OperacionService operacionService;

    @GetMapping("/fluctuacion")
    public Map<String, Date> fluctuaciones() {
        return null;
    }

    @GetMapping("/filter/alta")
    public List<Integer> habitantesConOPsAlta() {
        return this.operacionService.estadisticasHabsAlta();
    }

    @GetMapping("/filter/baja")
    public List<Integer> habitantesConOPsBaja() {
        return this.operacionService.estadisticasHabsBaja();
    }

    @GetMapping("/filter/modificacion")
    public List<Integer> habitantesConOPsModificacion() {
        return this.operacionService.estadisticasHabsModificacion();
    }
}
