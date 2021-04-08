package com.tfg.pmh.controllers;

import com.tfg.pmh.models.*;
import com.tfg.pmh.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

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

    @Autowired
    private IdentificacionService identificacionService;
    // Métodos para los habitantes

    // Documentos: https://www.youtube.com/watch?v=znjhY71F-8I
    // StackOverflow question: https://stackoverflow.com/questions/67010558/spring-boot-as-api-rest-is-not-deserializing-the-entire-object
    @PostMapping(value = "/habitante/new")
    public Respuesta nuevaSolicitud(@RequestBody Solicitud solicitud){
        boolean res;
        // solicitud.setSolicitante(this.habitanteService.findById(solicitud.getSolicitante().getId()));
        Respuesta respuesta = new Respuesta(400, null);
        switch (solicitud.getTipo()){
            case "A":
                if(solicitud.getSolicitante().getId() != null) {
                    solicitud.setSolicitante(this.habitanteService.findById(solicitud.getSolicitante().getId()));
                }
                if(solicitud.getTipoIdentificacion().getId() != null) {
                    solicitud.setTipoIdentificacion(this.identificacionService.findByid(solicitud.getTipoIdentificacion().getId()));
                }
                res = validarSolicitudDatosPersonales(solicitud);
                break;
            case "M":
                if("MD".equals(solicitud.getSubtipo())) {
                    res = validarSolicitudDatosPersonales(solicitud);
                } else {
                    res = validarSolicitudVivienda(solicitud);
                }
                break;
            default:
                res = false;
                break;
        }
        if(res) {
            if (solicitud.getDocumentos().size() == 0) {
                // Se ha decidido rechazar directamente la solicitud por no haber adjuntado documentos.
                solicitud.setEstado("R");
                solicitud.setJustificacion("JUSTIFICACIÓN AUTOMÁTICA: RECHAZADA POR NO ADJUNTAR DOCUMENTOS. SI CREE QUE ES UN ERROR DEL SISTEMA. REALICE OTRA SOLICITUD NUEVA.");
            }
            this.service.save(solicitud);
            respuesta = new Respuesta(200, solicitud);
        }
        return respuesta;
    }

    @PostMapping("/prueba")
    public void pruebaPrueba(@RequestBody Habitante habitante) {
        boolean a = true;
        boolean b = false;
    }

    @GetMapping("/habitante/mine")
    public Respuesta getSolicitudesDeHab(@RequestParam("userId") Long userId) {
        Respuesta res = new Respuesta();
        try {
            List<Solicitud> solicitudes = this.service.findBySolicitante(userId);
            res.setStatus(200);
            res.setObject(solicitudes);
        } catch (Exception e) {
            res.setObject(null);
            res.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        return res;
    }

    @GetMapping("/{id}")
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

    // Comprobar si es posible enviar por parámetros enviando sólo el id en la petición, una entidad
    @PostMapping("/habitante/cancel")
    public Respuesta cancelarSolicitud(@RequestParam("solicitudId") Long solicitudId, @RequestParam("habitante") Habitante habitante) {
        return null;
    }

    @PostMapping(value= "/document/new", consumes = {"multipart/form-data"})
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value= "/document/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id, @RequestParam("requestId") Long requestId) {
        Documento doc = this.documentService.findById(id);
        Solicitud solicitud = this.service.findById(requestId);
        if(doc == null || !Objects.requireNonNull(solicitud).getDocumentos().contains(doc)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getName() + "\"")
                    .body(doc.getData());
        }

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
        if(solicitud.getTipoIdentificacion() != null)
        {
            if(solicitud.getSolicitante() == null ||
                    "".equals(solicitud.getNombre()) ||
                    "".equals(solicitud.getPrimerApellido()) ||
                    "".equals(solicitud.getSegundoApellido()) ||
                    solicitud.getFechaNacimiento().after(new Date()) ||
                    !solicitud.getSubtipo().contains(solicitud.getTipo()) ||
                    (solicitud.getTipoIdentificacion().getCodigoTarjeta() != 0 && "".equals(solicitud.getIdentificacion()))) {
                res = false;
            }
        } else {
            res = false;
        }
        return res;
    }

    private boolean validarSolicitudVivienda(Solicitud solicitud) {
        boolean res = true;
        if(!solicitud.getSubtipo().contains(solicitud.getTipo()) ||
                solicitud.getVivienda() == null ||
                solicitud.getSolicitante() == null)
        {
            res = false;
        }

        return res;
    }
    // Métodos para los administradores

    @PostMapping("/administrador/update")
    public Respuesta updateSolicitud(@RequestParam("solicitudId") Long solicitudId, @RequestParam("estado") String estado, @RequestParam("justificacion") String justificacion, @RequestParam("admin") Administrador admin) {
        Solicitud solicitudBD = this.service.findById(solicitudId);
        // Si se intenta actualizar una solicitud que no está pendiente o el estado no es (A)ceptar o (R)echazar, se devuelve error de petición.
        if(!solicitudBD.getEstado().equals("P") || !Arrays.asList("A", "R").contains(estado) || admin == null) {
            return new Respuesta(400, null);
        } else {
            solicitudBD.setEstado(estado);
            solicitudBD.setJustificacion(justificacion);
            this.service.save(solicitudBD);
            return new Respuesta(200, solicitudBD);
        }
    }
}
