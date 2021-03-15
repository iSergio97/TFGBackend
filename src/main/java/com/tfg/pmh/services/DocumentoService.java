package com.tfg.pmh.services;

import com.tfg.pmh.models.Documento;
import com.tfg.pmh.repositories.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository repository;

    public Documento save(Documento doc) {
        return this.repository.save(doc);
    }

    public Documento findById(Long id) {
        return this.repository.findById(id).orElse(null);
    }
}
