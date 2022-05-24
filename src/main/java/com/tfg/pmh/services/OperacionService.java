package com.tfg.pmh.services;

import com.tfg.pmh.models.Operacion;
import com.tfg.pmh.repositories.OperacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OperacionService {

    @Autowired
    private OperacionRepository repository;

    public void save(Operacion operacion) {
        assert operacion != null;

        this.repository.save(operacion);
    }

    public Operacion findById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    public List<Operacion> findAll() { return this.repository.findAll(); }

    public Collection<Operacion> findOpsByHabitanteId(Long id) {
        return null;
    }

    public Integer getHabsByAO() {
        return this.repository.getHabsAltaPorSubtipo("AO");
    }

    public Integer getHabsByAN() {
        return this.repository.getHabsAltaPorSubtipo("AN");
    }

    public Integer getHabsByACR() {
        return this.repository.getHabsAltaPorSubtipo("ACR");
    }

    public List<Integer> estadisticasHabsAlta() {
        List<Integer> ls = new ArrayList<>();
        ls.add(this.getHabsByAO());
        ls.add(this.getHabsByAN());
        ls.add(this.getHabsByACR());

        return ls;
    }

    public Integer getHabsByBD() {
        return this.repository.getHabsBajaPorSubtipo("BD");
    }

    public Integer getHabsByBCD() {
        return this.repository.getHabsBajaPorSubtipo("BCD");
    }

    public List<Integer> estadisticasHabsBaja() {
        List<Integer> ls = new ArrayList<>();
        ls.add(this.getHabsByBD());
        ls.add(this.getHabsByBCD());

        return ls;
    }

    public Integer getHabsByMV() {
        return this.repository.getHabsModificacionPorSubtipo("MV");
    }

    public Integer getHabsByMD() {
        return this.repository.getHabsModificacionPorSubtipo("MD");
    }

    public List<Integer> estadisticasHabsModificacion() {
        List<Integer> ls = new ArrayList<>();
        ls.add(this.getHabsByMD());
        ls.add(this.getHabsByMV());

        return ls;
    }

    public Integer contadorOperaciones() {
        return this.repository.ratioOperacionesPorSolicitud() > 0 ? this.repository.ratioOperacionesPorSolicitud() : 1;
    }

    public List<List<Operacion>> mapaDeCalor() {
        List<List<Operacion>> listaDeListas = new ArrayList<>();
        Date start = new Date();
        int actualYear = start.getYear();
        start.setDate(1);
        start.setMonth(Calendar.JANUARY);
        start.setYear(actualYear - 1);
        Date end = new Date();
        end.setDate(31);
        end.setMonth(Calendar.DECEMBER);
        end.setYear(actualYear - 1);

        List<Operacion> lastYear = this.repository.mapaDeCalor(start, end);
        start.setYear(actualYear - 2);
        end.setYear(actualYear - 2);
        List<Operacion> prevLastYear = this.repository.mapaDeCalor(start, end);
        start.setYear(actualYear - 3);
        end.setYear(actualYear - 3);
        List<Operacion> prevPrevLastYear = this.repository.mapaDeCalor(start, end);

        listaDeListas.add(lastYear);
        listaDeListas.add(prevLastYear);
        listaDeListas.add(prevPrevLastYear);

        return listaDeListas;
    }

}
