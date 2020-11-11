package com.tfg.pmh.services;

import com.tfg.pmh.models.Operacion;
import com.tfg.pmh.repositories.OperacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

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

    public Collection<Operacion> findOpsByHabitanteId(Long id) {
        return null;
    }
}
