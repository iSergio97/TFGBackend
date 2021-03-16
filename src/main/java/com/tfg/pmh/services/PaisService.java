package com.tfg.pmh.services;

import com.tfg.pmh.models.Pais;
import com.tfg.pmh.repositories.PaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaisService {

    @Autowired
    private PaisRepository repository;

    public void save(Pais pais) {
        this.repository.save(pais);
    }

    public Pais findOne(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    public List<Pais> findAll() {
        return this.repository.findAll();
    }
}
