package com.tfg.pmh.services;

import com.tfg.pmh.models.Provincia;
import com.tfg.pmh.repositories.ProvinciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class ProvinciaService {

    @Autowired
    private ProvinciaRepository repository;

    public void save(Provincia provincia) {
        this.repository.save(provincia);
    }

    public Provincia findOne(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    public List<Provincia> findAll() {
        return this.repository.findAll();
    }

    public List<Provincia> findProvinciasByPaisId(Long id) {
        return this.repository.findProvinciaByPaisId(id);
    }
}
