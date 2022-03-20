package com.tfg.pmh.controllers;

import com.tfg.pmh.models.Respuesta;
import com.tfg.pmh.services.CalleService;
import com.tfg.pmh.services.OperacionService;
import com.tfg.pmh.services.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequestMapping("/estadisticas")
@RestController
@CrossOrigin(origins = {"*"})
public class EstadisticasController {

    @Autowired
    private OperacionService operacionService;

    @Autowired
    private SolicitudService solicitudService;

    @Autowired
    private CalleService calleService;

    @GetMapping("/fluctuacion")
    public Respuesta fluctuaciones() {
        Respuesta res = new Respuesta();
        try {
            Long aceptadas = this.solicitudService.solicitudesAceptadas();
            Long rechazadas = this.solicitudService.solicitudesRechazadas();
            Long pendientes = this.solicitudService.solicitudesPendiente();
            Long canceladas = this.solicitudService.solicitudesCanceladas();
            res.setStatus(200);
            res.setObject(Arrays.asList(aceptadas, rechazadas, pendientes, canceladas));
        } catch (Exception e) {
            res.setStatus(404);
            res.setObject(null);
        }
        return res;
    }

    @GetMapping("/ratio")
    public Respuesta ratio() {
        Respuesta res = new Respuesta();
        try {
            Double ratio = (double) (this.operacionService.contadorOperaciones() / this.solicitudService.contadorSolicitudes());
            res.setStatus(200);
            res.setObject(ratio);
        } catch (Exception e) {
            res.setObject(null);
            res.setStatus(404);
        }
        return res;
    }

    @GetMapping("/solicitudes-fecha")
    public Respuesta solicitudesPorFecha() {
        Respuesta res = new Respuesta();
        try {
            res.setStatus(200);
            res.setObject(this.solicitudService.solicitudesPorMes());
        } catch (Exception e) {
            res.setStatus(404);
            res.setObject(null);
        }
        return res;
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

    @GetMapping("/heatmap")
    public Respuesta mapaDeCalor() {
        Respuesta res = new Respuesta();
        try {
            res.setStatus(200);
            res.setObject(this.calleService.mapaDeCalor());
        } catch (Exception e) {
            res.setStatus(404);
            res.setObject(null);
        }
        return res;

    }
}
