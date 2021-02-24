package com.tfg.pmh.services;

import com.tfg.pmh.models.Document;
import com.tfg.pmh.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.Doc;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository repository;

    public Document save(Document doc) {
        return this.repository.save(doc);
    }

    public Document findById(Long id) {
        return this.repository.findById(id).orElse(null);
    }
}
