package com.tfg.pmh.services;

import com.tfg.pmh.models.Identificacion;
import com.tfg.pmh.repositories.IdentificadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentificadorService {

    @Autowired
    private IdentificadorRepository repository;

    public void save(Identificacion identificador) {
        this.repository.save(identificador);
    }

    public Identificacion findById(Long id) {
        return this.repository.findById(id).orElse(null);
    }
}
