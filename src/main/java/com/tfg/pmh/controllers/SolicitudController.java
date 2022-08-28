package com.tfg.pmh.controllers;

import com.tfg.pmh.email.Email;
import com.tfg.pmh.email.EmailService;
import com.tfg.pmh.models.*;
import com.tfg.pmh.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private NumeracionService numeracionService;

    @Autowired
    private IdentificacionService identificacionService;

    @Autowired
    private CalleService calleService;

    @Autowired
    private OperacionService operacionService;

    @Autowired
    private HojaService hojaService;

    @Autowired
    private EmailService emailService;
    // Métodos para los habitantes

    // Documentos: https://www.youtube.com/watch?v=znjhY71F-8I
    // StackOverflow question: https://stackoverflow.com/questions/67010558/spring-boot-as-api-rest-is-not-deserializing-the-entire-object
    @PostMapping(value = "/habitante/new")
    public Respuesta nuevaSolicitud(@RequestBody Solicitud solicitud){
        boolean res;
        Respuesta respuesta = null;
        try {
            if(solicitud.getSolicitante().getId() != null) {
                solicitud.setSolicitante(this.habitanteService.findById(solicitud.getSolicitante().getId()));
            }
            if(solicitud.getTipoIdentificacion() != null && solicitud.getTipoIdentificacion().getId() != null) {
                solicitud.setTipoIdentificacion(this.identificacionService.findByid(solicitud.getTipoIdentificacion().getId()));
            }
            if(solicitud.getHoja() != null) {
                solicitud.setHoja(this.hojaService.findById(solicitud.getHoja().getId()));
            }
            switch (solicitud.getTipo()){
                case "A":
                    res = validarSolicitudVivienda(solicitud);
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
            solicitud.setFecha(new Date());
            if(res) {
                if (solicitud.getDocumentos().size() == 0) {
                    // Se ha decidido rechazar directamente la solicitud por no haber adjuntado documentos.
                    // Por aquí no debería salir
                    solicitud.setEstado("R");
                    solicitud.setJustificacion("JUSTIFICACIÓN AUTOMÁTICA: RECHAZADA POR NO ADJUNTAR DOCUMENTOS. SI CREE QUE ES UN ERROR DEL SISTEMA. REALICE OTRA SOLICITUD NUEVA.");
                } else if("MV".equals(solicitud.getSubtipo()) && "B".equals(solicitud.getSolicitante().getEstado())) {
                    solicitud.setEstado("R");
                    solicitud.setJustificacion("JUSTIFICACIÓN AUTOMÁTICA: RECHAZADA PORQUE NO PUEDE REALIZAR UNA SOLICITUD DE MODIFICACIÓN DE VIVIENDA SI NO TIENE VIVIENDA. DEBE REALIZAR UNA SOLICITUD DE ALTA POR CAMBIO DE RESIDENCIA");
                } else if("ACR".equals(solicitud.getSubtipo()) && "A".equals(solicitud.getSolicitante().getEstado())) {
                    solicitud.setEstado("R");
                    solicitud.setJustificacion("JUSTIFICACIÓN AUTOMÁTICA: RECHAZADA PORQUE NO PUEDE REALIZAR UNA SOLICITUD DE ALTA POR CAMBIO DE RESIDENCIA SI TIENE VIVIENDA. DEBE REALIZAR UNA SOLICITUD DE MODIFICACIÓN DE VIVIENDA");
                }
                this.service.save(solicitud);
                Solicitud sol = this.service.findById(solicitud.getId());
                respuesta = new Respuesta(200, sol);
            }
        } catch (Exception e) {
            respuesta = new Respuesta(400, null);
        }
        return respuesta;
    }

    @PostMapping(value = "/habitante/edit/{id}")
    public Respuesta editarSolicitudHabitante(@PathVariable("id") Long id, @RequestParam ("justificacion") String justificacion){
        Respuesta respuesta = null;
        Solicitud solicitud;
        try {
            respuesta = new Respuesta();
            solicitud = this.service.findById(id);

            if(!solicitud.getEstado().equals("P")) {
                respuesta.setObject(null);
                respuesta.setStatus(405);

                return respuesta;
            }

            solicitud.setJustificacionHab(justificacion);
            solicitud.setFecha(new Date()); // Le ponemos la nueva fecha tras editarlo

            this.service.save(solicitud);
            respuesta.setStatus(200);
            respuesta.setObject(solicitud);
        } catch (Exception e) {
            respuesta = new Respuesta(400, null);
        }
        return respuesta;
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

    @GetMapping("/habitante/mine/filter")
    public Respuesta getSolicitudesDeHabConFiltro(@RequestParam("userId") Long userId, @RequestParam("fechaDesde") String fechaDesde, @RequestParam("fechaHasta") String fechaHasta) {
        Respuesta res = new Respuesta();
        try {
            Date fDesde = parseaFecha(fechaDesde);
            Date fHasta = parseaFecha(fechaHasta);
            List<Solicitud> solicitudes = this.service.findSolicitudesBySolicitanteFiltro(userId, fDesde, fHasta);
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

    @GetMapping("/habitante/calles/tipo")
    public List<String> tiposDeCalle() {
        return this.calleService.tiposDeCalle();
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

    @PostMapping(value= "/document/edit/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<List<Documento>> editUploadFiles(@RequestParam("file") MultipartFile[] file, @PathVariable("id") Long id) {
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
            Solicitud solicitud = this.service.findById(id);
            solicitud.getDocumentos().addAll(documentoList);
            this.service.save(solicitud);

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
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(doc.getData());
        }

    }

    @GetMapping("/habitante/numeracion/all")
    public Respuesta getAllNumeraciones() {
        Respuesta respuesta = new Respuesta();
        try {
            respuesta.setStatus(200);
            respuesta.setObject(this.numeracionService.findAll());
        } catch (Exception e) {
            respuesta.setStatus(404);
            respuesta.setObject(null);
        }

        return respuesta;
    }

    @GetMapping("/habitante/viviendas/all")
    public Respuesta getAllCalles(@RequestParam("munId") Long munId) {
        Respuesta res = new Respuesta();
        try {
            res.setStatus(200);
            res.setObject(this.calleService.getCallesByMunicipioId(munId));
        } catch (Exception e) {
            res.setStatus(404);
            res.setObject(null);
        }

        return res;
    }

    @GetMapping("/habitante/calles/{calleTipo}")
    public Respuesta getCallesByName(@PathVariable("calleTipo") String calleTipo) {
        Respuesta res = new Respuesta();
        try {
            res.setStatus(200);
            res.setObject(this.calleService.getCallesByTipo(calleTipo));
        } catch (Exception e) {
            res.setStatus(404);
            res.setObject(null);
        }

        return res;
    }

    @GetMapping("/habitante/numeraciones/{calleId}")
    public Respuesta getNumeracionesByCalleId(@PathVariable Long calleId) {
        Respuesta res = new Respuesta();
        try {
            res.setStatus(200);
            List<Numeracion> numeraciones = this.numeracionService.findByCalleId(calleId);
            numeraciones.sort(Comparator.comparing(Numeracion::getNumero, Comparator.naturalOrder()));
            res.setObject(numeraciones);
        } catch (Exception e) {
            res.setStatus(404);
            res.setObject(null);
        }

        return res;
    }

    @GetMapping("/habitante/hojas/{numeracionId}")
    public Respuesta getAllHojasByNumeracionId(@PathVariable Long numeracionId) {
        Respuesta res = new Respuesta();
        try {
            res.setStatus(200);
            res.setObject(this.hojaService.findByNumeracion(numeracionId));
        } catch (Exception e) {
            res.setStatus(404);
            res.setObject(null);
        }

        return res;
    }

    @GetMapping("/habitante/numeraciones/rc")
    public Respuesta getDomicilioPorRC(@RequestParam("referenciaCatastral") String referenciaCatastral) {
        Respuesta res = new Respuesta();
        try {
            Numeracion numeracion = this.numeracionService.findNumeracionByReferenciaCatastral(referenciaCatastral);
            res.setObject(numeracion);
            res.setStatus(200);
        } catch (Exception e) {
            res.setObject(null);
            res.setStatus(404);
        }

        return res;
    }

    // Métodos auxiliares para tratar las solicitudes

    private boolean validarSolicitudDatosPersonales(Solicitud solicitud) {
        boolean res = true;
        if(solicitud.getTipoIdentificacion() != null)
        {
            if(solicitud.getSolicitante() == null ||
                    solicitud.getNombre().trim().isEmpty() ||
                    solicitud.getPrimerApellido().trim().isEmpty() ||
                    solicitud.getSegundoApellido().trim().isEmpty() ||
                    solicitud.getFechaNacimiento().after(new Date()) ||
                    !solicitud.getSubtipo().contains(solicitud.getTipo()) ||
                    (solicitud.getTipoIdentificacion().getCodigoTarjeta() != 0 && solicitud.getIdentificacion().trim().isEmpty()
                            && solicitud.getIdentificacion().length() > 0  && solicitud.getIdentificacion().length() < 8)) {
                res = false;
            }
        } else {
            res = false;
        }
        return res;
    }

    private boolean validarSolicitudVivienda(Solicitud solicitud) {
        return solicitud.getSubtipo().contains(solicitud.getTipo()) && solicitud.getHoja() != null && solicitud.getSolicitante() != null;
    }
    // Métodos para los administradores

    @GetMapping("/administrador/all")
    public Respuesta allRequestsAdmin() {
        Respuesta respuesta = new Respuesta();
        try {
            respuesta.setObject(this.service.findAllPendientes());
            respuesta.setStatus(200);
        } catch (Exception e) {
            respuesta.setObject(null);
            respuesta.setStatus(404);
        }
        return respuesta;
    }

    @GetMapping("/administrador/filter")
    public Respuesta requestAdminByFilter(@RequestParam("estado") String estado, @RequestParam("desde") String desde, @RequestParam("hasta") String hasta) {
        Respuesta respuesta = new Respuesta();
        try {
            Date fechaDesde = parseaFecha(desde);
            Date fechaHasta = parseaFecha(hasta);
            List<Solicitud> res;
            if("T".equals(estado)) {
                res = this.service.findSolicitudesEntreFechas(fechaDesde, fechaHasta);
            } else {
                res = this.service.findSolicitudesPorFiltro(estado, fechaDesde, fechaHasta);
            }
            respuesta.setObject(res);
            respuesta.setStatus(200);
        } catch (Exception e) {
            respuesta.setObject(null);
            respuesta.setStatus(404);
        }
        return respuesta;
    }

    @GetMapping("/administrador/{id}")
    public Respuesta getSolicitudAdmin(@PathVariable Long id) {
        Solicitud solicitud = this.service.findById(id);
        Respuesta res = new Respuesta();
        if(solicitud != null) {
            res.setObject(solicitud);
            res.setStatus(200);
        } else {
            res.setObject(null);
            res.setStatus(404);
        }
        return res;
    }


    @PostMapping("/administrador/update")
    public Respuesta updateSolicitud(@RequestParam("solicitudId") Long solicitudId, @RequestParam("estado") String estado, @RequestParam("justificacion") String justificacion) {
        Respuesta res;
        try {
            Solicitud solicitudBD = this.service.findById(solicitudId);
            // Si se intenta actualizar una solicitud que no está pendiente o el estado no es (A)ceptar o (R)echazar, se devuelve error de petición.
            if(!solicitudBD.getEstado().equals("P")) {
                res = new Respuesta(400, null);
            } else {
                solicitudBD.setEstado(estado);
                solicitudBD.setJustificacion(justificacion);
                this.service.save(solicitudBD);
                if("A".equals(estado)) {
                    realizarOperacion(solicitudBD);
                }
                res = new Respuesta(200, solicitudBD);

                if(!solicitudBD.getSolicitante().getEmail().isEmpty()) {
                    String content = "Su solicitud ha sido aceptada. Podrá comprobar sus datos actualizados en su próximo acceso a la aplicación.";
                    if("R".equals(estado)) {
                        content = "Su solicitud ha sido rechazada. Para más información, puede leer el comentario que ha sido adjuntado en su solicitud.";
                    } else if ("P".equals(estado)) {
                        content = "Un administrador ha actualizado su justificación, solicitando más información al respecto sobre el cambio. Para más información, puede acceder a la aplicación, acceder a su solicitud y ver el campo 'Justificación'.";
                    }

                    content += "\n\nGracias por usar el sistema del Padrón Online de Habitantes, el lugar donde realizar tus solicitudes desde cualquier parte del mundo.";

                    Email email = new Email("POH - Actualización solicitud", solicitudBD.getSolicitante().getEmail(), content);
                    emailService.sendEmail(email);
                }
            }
        } catch (Exception e) {
            // Se ha producido un error inesperado y se va a devolver un 400 como respuesta a la petición
            res = new Respuesta(400, null);
        }
        return res;
    }

    private void realizarOperacion(Solicitud solicitud) {
        Habitante solicitante = solicitud.getSolicitante();
        Operacion operacion = new Operacion();
        operacion.setFechaOperacion(new Date());
        operacion.setTipo(solicitud.getTipo());
        operacion.setSubtipo(solicitud.getSubtipo());
        operacion.setHabitante(solicitante);
        operacion.setSolicitud(solicitud);
        if(solicitud.getSubtipo().equals("MD")) {
            solicitante.setNombre(solicitud.getNombre());
            operacion.setNombre(solicitud.getNombre());
            solicitante.setPrimerApellido(solicitud.getPrimerApellido());
            operacion.setPrimerApellido(solicitud.getPrimerApellido());
            solicitante.setSegundoApellido(solicitud.getSegundoApellido());
            operacion.setSegundoApellido(solicitud.getSegundoApellido());
            solicitante.setFechaNacimiento(solicitud.getFechaNacimiento());
            operacion.setFechaNacimiento(solicitud.getFechaNacimiento());
            solicitante.setIdentificacion(solicitud.getIdentificacion());
            operacion.setIdentificacion(solicitud.getIdentificacion());
            solicitante.setTarjetaIdentificacion(solicitud.getTipoIdentificacion());
            operacion.setTipoIdentificacion(solicitud.getTipoIdentificacion());
        } else {
            solicitante.setEstado("A");
            Hoja hoja = this.hojaService.create(solicitud.getHoja().getNumeracion()); // Creamos una nueva hoja con un número de hoja más
            this.hojaService.save(hoja);

            solicitud.getGrupo().forEach((Habitante hab) -> hab.setHoja(hoja));
            this.habitanteService.saveAll(solicitud.getGrupo());

            solicitante.setHoja(hoja);
            operacion.setHoja(hoja);
        }
        this.habitanteService.save(solicitante);
        this.operacionService.save(operacion);
    }

    private void convertirSolicitud(Solicitud solicitud) {
        Habitante habitante = solicitud.getSolicitante();
        if("A".equals(solicitud.getTipo()) || "MD".equals(solicitud.getSubtipo())) {
            habitante.setNombre(solicitud.getNombre());
            habitante.setPrimerApellido(solicitud.getPrimerApellido());
            habitante.setSegundoApellido(solicitud.getSegundoApellido());
            habitante.setIdentificacion(solicitud.getIdentificacion());
        } else if("M".equals(solicitud.getTipo()) && !"MD".equals(solicitud.getSubtipo())) {
            habitante.setHoja(solicitud.getHoja());
        }
    }

    private Date parseaFecha(String fecha) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
    }
}
