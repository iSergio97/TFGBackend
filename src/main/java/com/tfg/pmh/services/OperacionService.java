package com.tfg.pmh.services;

import com.tfg.pmh.models.Operacion;
import com.tfg.pmh.repositories.OperacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

}
