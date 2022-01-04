package com.tfg.pmh.services;

import com.tfg.pmh.models.Hoja;
import com.tfg.pmh.models.Numeracion;
import com.tfg.pmh.repositories.HojaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HojaService {

    @Autowired
    private HojaRepository repository;

    public void save(Hoja hoja) {
        this.repository.save(hoja);
    }

    public Hoja findById(Long id) { return this.repository.findById(id).orElse(null); }

    public List<Hoja> findAll() { return this.repository.findAll(); }

    public List<Hoja> findByNumeracion(Long id) {
        return this.repository.findByNumeracion(id);
    }
}
