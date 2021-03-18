package com.tfg.pmh.services;

import com.tfg.pmh.models.Vivienda;
import com.tfg.pmh.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ViviendaService {

    @Autowired
    private ViviendaRepository repository;

    public void save(Vivienda vivienda) {
        assert vivienda != null;

        this.repository.save(vivienda);
    }

    public Vivienda findById(Long id) {
        return this.repository.findById(id).orElse(null);
    }
}
