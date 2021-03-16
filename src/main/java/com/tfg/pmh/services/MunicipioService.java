package com.tfg.pmh.services;

import com.tfg.pmh.models.Municipio;
import com.tfg.pmh.repositories.MunicipioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MunicipioService {

    @Autowired
    private MunicipioRepository repository;

    public void save(Municipio municipio) {
        this.repository.save(municipio);
    }

    public Municipio findById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    public List<Municipio> findAll() {
        return this.repository.findAll();
    }

    public List<Municipio> findMunicipiosByProvinciaId(Long id) {
        return this.repository.findMunicipioByPaisId(id);
    }
}
