package com.tfg.pmh.services;

import com.tfg.pmh.models.Numeracion;
import com.tfg.pmh.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class NumeracionService {

    @Autowired
    private NumeracionRepository repository;

    public void save(Numeracion vivienda) {
        assert vivienda != null;

        this.repository.save(vivienda);
    }

    public Numeracion findById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    public List<Numeracion> findAll() {
        return this.repository.findAll();
    }
}
