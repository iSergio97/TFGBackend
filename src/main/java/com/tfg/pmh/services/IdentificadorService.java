package com.tfg.pmh.services;

import com.tfg.pmh.models.Identificador;
import com.tfg.pmh.repositories.IdentificadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentificadorService {

    @Autowired
    private IdentificadorRepository repository;

    public void save(Identificador identificador) {
        this.repository.save(identificador);
    }

    public Identificador findById(Long id) {
        return this.repository.findById(id).orElse(null);
    }
}
