package com.tfg.pmh.controllers;

import com.tfg.pmh.models.Documento;
import com.tfg.pmh.models.Respuesta;
import com.tfg.pmh.models.Solicitud;
import com.tfg.pmh.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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


    @Autowired
    private ViviendaService viviendaService;
    // Métodos para los habitantes

    // Documentos: https://www.youtube.com/watch?v=znjhY71F-8I
    @PostMapping("/habitante/new")
    public Respuesta nuevaSolicitud(@RequestBody Solicitud solicitud){
        boolean res;
        switch (solicitud.getTipo()){ // TODO: Añadir comprobación para las solicitudes de modificación
            case "A":
                res = validarSolicitudDatosPersonales(solicitud);
                break;
            case "M":
                if("MD".equals(solicitud.getSubtipo())) {
                    res = validarSolicitudDatosPersonales(solicitud);
                } else {
                    res = false;
                }
                break;
            default:
                res = false;
                break;
        }
        if(res) {
            this.service.save(solicitud);
            return new Respuesta(200, solicitud);
        } else {
            return new Respuesta(400, null);
        }
    }

    @GetMapping("/habitante/{id}")
    public Respuesta getSolicitud(@PathVariable Long id, @RequestParam("userId") Long userId) {
        Solicitud solicitud = this.service.findById(id);
        Respuesta res = new Respuesta();
        if(solicitud != null && solicitud.getSolicitante().getId().equals(userId)) {
            res.setObject(solicitud);
            res.setStatus(200);
        } else {
            res.setObject(null);
            res.setStatus(404);
        }
        return res;
    }

    @PostMapping(value= "/document", consumes = {"multipart/form-data"})
    public ResponseEntity<List<Documento>> uploadFile(@RequestParam("file") MultipartFile[] file) {
        try {
            List<Documento> documentoList = new ArrayList<>();
            Documento document;
            for (MultipartFile multipartFile : file) {
                document = new Documento();

                document.setName(multipartFile.getOriginalFilename());
                document.setData(multipartFile.getBytes());
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

    @GetMapping(value= "/document/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        Documento doc = this.documentService.findById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getName() + "\"")
                .body(doc.getData());

    }

    @GetMapping("/viviendas/all")
    public Respuesta getAllCalles() {
        Respuesta respuesta = new Respuesta();
        try {
            respuesta.setStatus(200);
            respuesta.setObject(this.viviendaService.findAll());
        } catch (Exception e) {
            respuesta.setStatus(404);
            respuesta.setObject(null);
        }

        return respuesta;
    }

    // Métodos auxiliares para tratar las solicitudes

    private boolean validarSolicitudDatosPersonales(Solicitud solicitud) {
        boolean res = true;
        if("".equals(solicitud.getNombre()) ||
                "".equals(solicitud.getPrimerApellido()) ||
                "".equals(solicitud.getSegundoApellido()) ||
                solicitud.getFechaNacimiento().after(new Date()) ||
                !solicitud.getSubtipo().contains(solicitud.getTipo()) ||
                (solicitud.getTipoIdentificacion() != 0 && "".equals(solicitud.getIdentificacion())))
        {
            res = false;
        }
        return res;
    }
    // Métodos para los administradores
}
