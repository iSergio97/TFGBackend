package com.tfg.pmh.services;

import com.tfg.pmh.forms.MapaCalor;
import com.tfg.pmh.models.Calle;
import com.tfg.pmh.repositories.CalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CalleService {

    @Autowired
    private CalleRepository repository;

    public void save(Calle calle) {
        this.repository.save(calle);
    }

    public Calle findOne(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    public List<Calle> findAll() {
        return this.repository.findAll();
    }

    public List<Calle> getCallesByMunicipioId(Long id) {
        return this.repository.getCalleByMunicipioId(id);
    }

    public List<Calle> getCallesByTipo(String calleTipo) { return this.repository.getCallesByTipo(calleTipo); }

    public List<MapaCalor> mapaDeCalor() { return this.repository.mapaDeCalor(); }
}
