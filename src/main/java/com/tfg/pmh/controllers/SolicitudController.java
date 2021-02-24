package com.tfg.pmh.controllers;

import com.tfg.pmh.forms.SolicitudForm;
import com.tfg.pmh.models.Document;
import com.tfg.pmh.models.Habitante;
import com.tfg.pmh.models.Respuesta;
import com.tfg.pmh.models.Solicitud;
import com.tfg.pmh.services.DocumentService;
import com.tfg.pmh.services.HabitanteService;
import com.tfg.pmh.services.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/solicitud")
@CrossOrigin(origins = {"*"})
public class SolicitudController {

    @Autowired
    private HabitanteService habitanteService;

    @Autowired
    private SolicitudService service;

    @Autowired
    private DocumentService documentService;
    // Métodos para los habitantes

    // Documentos: https://www.youtube.com/watch?v=znjhY71F-8I
    @PostMapping("/habitante/new")
    public Respuesta prueba(@RequestBody SolicitudForm solicitud) {
        Respuesta respuesta = new Respuesta();
        // Revisar qué status pondremos para BadRequest en el parámetro
        respuesta.setStatus(350);
        try {
            Solicitud solicitudBD = this.service.deconstruct(solicitud);

            // Error a la hora de guardar. Revisar mañana
            this.service.save(solicitudBD);
            respuesta.setObject(solicitudBD);
            respuesta.setStatus(200);
            return respuesta;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println(e.getCause());
            return respuesta;
        }
    }

    @GetMapping("/h")
    public SolicitudForm test() {
        return new SolicitudForm();
    }

    @PostMapping(value= "/document", consumes = {"multipart/form-data"})
    public ResponseEntity<Document> uploadFile(@RequestParam("file") MultipartFile file, String name) {
        try {
            Document document = new Document();

            document.setName(name);
            document.setData(file.getBytes());
            System.out.println(name);
            this.documentService.save(document);
            return new ResponseEntity<>(document, HttpStatus.OK);
        } catch (IOException e) {
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value= "/document")
    public ResponseEntity<byte[]> downloadFile(Long id) {
        Document doc = this.documentService.findById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getName() + "\"")
                .body(doc.getData());

    }

    // Métodos para los administradores
}
