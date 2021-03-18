package com.tfg.pmh.controllers;

import com.tfg.pmh.models.Documento;
import com.tfg.pmh.models.Respuesta;
import com.tfg.pmh.models.Solicitud;
import com.tfg.pmh.services.DocumentoService;
import com.tfg.pmh.services.HabitanteService;
import com.tfg.pmh.services.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
    private DocumentoService documentService;
    // Métodos para los habitantes

    // Documentos: https://www.youtube.com/watch?v=znjhY71F-8I
    @PostMapping("/habitante/new")
    public Respuesta nuevaSolicitud(@RequestBody Solicitud solicitud){

        System.out.println(solicitud.getEstado());

        System.out.println(solicitud.getFecha());

        this.service.save(solicitud);

        return new Respuesta(200, null);
    }

    @PostMapping(value= "/document", consumes = {"multipart/form-data"})
    public ResponseEntity<List<Documento>> uploadFile(@RequestParam("file") MultipartFile[] file) {
        try {
            List<Documento> documentoList = new ArrayList<>();
            Documento document;
            for(int i = 0; i < file.length; i++) {
                document = new Documento();

                document.setName(file[i].getOriginalFilename());
                document.setData(file[i].getBytes());
                this.documentService.save(document);
                documentoList.add(document);
            }
            return new ResponseEntity<>(documentoList, HttpStatus.OK);
        } catch (IOException e) {
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value= "/document")
    public ResponseEntity<byte[]> downloadFile(Long id) {
        Documento doc = this.documentService.findById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getName() + "\"")
                .body(doc.getData());

    }

    // Métodos para los administradores
}
