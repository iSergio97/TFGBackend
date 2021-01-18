package com.tfg.pmh.services;

import com.tfg.pmh.forms.CuentaUsuarioForm;
import com.tfg.pmh.models.CuentaUsuario;
import com.tfg.pmh.models.Habitante;
import com.tfg.pmh.models.Vivienda;
import com.tfg.pmh.repositories.HabitanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

@Service
public class HabitanteService {

    @Autowired
    private HabitanteRepository repository;

    @Autowired
    private CuentaUsuarioService cuentaUsuarioService;

    public void save(Habitante habitante) {
        assert habitante != null;

        this.repository.save(habitante);
    }

    public Habitante findById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    // Cambiado a Collection para evitar la creacion de la clase familia
    // Ya que una familia son aquellas personas que viven en el mismo domicilio
    public Collection<Habitante> findByViviendaId(Long id) {
        return this.repository.findHabitanteByViviendaId(id);
    }

    public Habitante findByUsername(String username) {
        return this.repository.findByUsername(username);
    }

}
