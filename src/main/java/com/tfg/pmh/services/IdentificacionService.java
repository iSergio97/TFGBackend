package com.tfg.pmh.services;

import com.tfg.pmh.models.Identificacion;
import com.tfg.pmh.repositories.IdentificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentificacionService {

    @Autowired
    private IdentificacionRepository repository;

    public Identificacion findByid(Long id) {
        return this.repository.findById(id).orElse(null);
    }
}
