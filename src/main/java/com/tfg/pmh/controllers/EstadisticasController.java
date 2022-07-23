package com.tfg.pmh.controllers;

import com.tfg.pmh.forms.GrupoSolicitudes;
import com.tfg.pmh.models.Respuesta;
import com.tfg.pmh.repositories.CountHabitantes;
import com.tfg.pmh.services.CalleService;
import com.tfg.pmh.services.HabitanteService;
import com.tfg.pmh.services.OperacionService;
import com.tfg.pmh.services.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

    @Autowired
    private HabitanteService habitanteService;

    @GetMapping("/fluctuacion")
    public Respuesta fluctuaciones() {
        Respuesta res = new Respuesta();
        try {
            res.setStatus(200);
            res.setObject(this.solicitudService.solicitudesPorEstado());
        } catch (Exception e) {
            res.setStatus(404);
            res.setObject(null);
        }
        return res;
    }

    @GetMapping("/fluctuacion/filter")
    public Respuesta fluctuacionesFiltro(@RequestParam("fechaDesde") String fechaDesde, @RequestParam("fechaHasta") String fechaHasta){
        Respuesta res = new Respuesta();
        try {
            Date desde = parseaFecha(fechaDesde);
            Date hasta = parseaFecha(fechaHasta);
            res.setStatus(200);
            res.setObject(this.solicitudService.solicitudesPorEstadoFiltro(desde, hasta));
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
            res.setObject(this.solicitudService.solicitudesPorSemana());
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

    @GetMapping("/countSexoHabitantes")
    public Respuesta contadorSexoHabitantes() {
        Respuesta res = new Respuesta();
        try {
            res.setStatus(200);
            List<CountHabitantes> ls = this.habitanteService.contadorSexoHabitantes();
            res.setObject(ls);
        } catch(Exception e) {
            res.setStatus(404);
            res.setObject(null);
        }

        return res;
    }

    private Date parseaFecha(String fecha) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
    }
}
