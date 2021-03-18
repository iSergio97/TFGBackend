package com.tfg.pmh.controllers;

import com.tfg.pmh.models.*;
import com.tfg.pmh.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@RestController
@RequestMapping("/sistema/administrador")
@CrossOrigin(origins = {"*"})
public class SistemaController {

    @Autowired
    private CuentaUsuarioService cuentaUsuarioService;

    @Autowired
    private AdministradorService administradorService;

    @Autowired
    private HabitanteService habitanteService;

    @Autowired
    private IdentificadorService identificadorService;

    @Autowired
    private ViviendaService viviendaService;

    @Autowired
    private OperacionService operacionService;

    @Autowired
    private SolicitudService solicitudService;

    @Autowired
    private PaisService paisService;

    @Autowired
    private ProvinciaService provinciaService;

    @Autowired
    private MunicipioService municipioService;

    @Autowired
    private CalleService calleService;

    @GetMapping("/info")
    public List<Integer> systemInfo() {
        // Puede que interese crear un objeto Info, no persistente, para almacenar estas cosas
        return new ArrayList<>();
    }

    @GetMapping("/poblate")
    public Integer poblate() throws NoSuchAlgorithmException {

        //Creamos las tarjeta de identificación
        /*
            0 - No identificación
            1 - NIF
            2 - Pasaporte
            3 - Tarjeta de extranjería
         */
        // Creamos las cuentas de usuario de administradores primero y los guardamos en la base de datos
        adminUA();
        // Tras ello, creamos a 100 habitantes
        cuentaHabitantes();

        return 200;
    }

    public void adminUA() throws NoSuchAlgorithmException {
        CuentaUsuario cuentaUsuario = new CuentaUsuario();
        cuentaUsuario.setUsername("sergio");
        cuentaUsuario.setSalt(hashText(createSalt()));
        cuentaUsuario.setRol("ADMIN");
        String password = "sergio" + cuentaUsuario.getSalt();
        cuentaUsuario.setPassword(hashText(password));

        cuentaUsuarioService.save(cuentaUsuario);

        Administrador administrador = new Administrador();
        administrador.setCuentaUsuario(cuentaUsuario);
        administrador.setEmail("sergio@gmail.com");
        administrador.setNombre("Sergio");
        administrador.setPrimerApellido("Garrido");
        administrador.setSegundoApellido("Domínguez");
        Calendar fechaNacimiento = Calendar.getInstance();
        fechaNacimiento.set(1997, Calendar.FEBRUARY, 10);
        administrador.setFechaNacimiento(fechaNacimiento.getTime());
        administrador.setImage("https://thispersondoesnotexist.com/image");

        administradorService.save(administrador);

        cuentaUsuario = new CuentaUsuario();
        cuentaUsuario.setUsername("espe");
        cuentaUsuario.setSalt(hashText(createSalt()));
        cuentaUsuario.setRol("ADMIN");
        cuentaUsuario.setPassword(hashText("espe"+cuentaUsuario.getSalt()));

        cuentaUsuarioService.save(cuentaUsuario);

        administrador = new Administrador();
        administrador.setCuentaUsuario(cuentaUsuario);
        administrador.setEmail("espe@gmail.com");
        administrador.setNombre("Espe");
        administrador.setPrimerApellido("Cañas");
        administrador.setSegundoApellido("Blanco");
        fechaNacimiento = Calendar.getInstance();
        fechaNacimiento.set(1996, Calendar.JANUARY, 31);
        administrador.setFechaNacimiento(fechaNacimiento.getTime());
        administrador.setImage("https://thispersondoesnotexist.com/image");
        administradorService.save(administrador);
    }

    private void cuentaHabitantes() throws NoSuchAlgorithmException {
        CuentaUsuario cuentaUsuario;
        Habitante habitante;
        Calendar fechaNacimiento;
        Identificacion identificador;
        Vivienda vivienda;
        List<Habitante> habitantes = new ArrayList<>();

        Random random = new Random();

        for(int i = 0; i < 9; i++) {
            try {
                cuentaUsuario = new CuentaUsuario();
                cuentaUsuario.setUsername("habitante"+ String.valueOf(i));
                cuentaUsuario.setSalt(hashText(createSalt()));
                cuentaUsuario.setRol("HABITANTE");
                cuentaUsuario.setPassword(hashText("habitante"+ String.valueOf(i) + cuentaUsuario.getSalt()));

                cuentaUsuarioService.save(cuentaUsuario);

                habitante = new Habitante();
                habitante.setCuentaUsuario(cuentaUsuario);
                habitante.setEmail("habitante"+String.valueOf(i)+"@gmail.com");
                habitante.setNombre("Habitante "+String.valueOf(i) + " nombre");
                habitante.setPrimerApellido("Habitante "+String.valueOf(i) + " primer apellido");
                habitante.setSegundoApellido("Habitante "+String.valueOf(i) + " segundo apellido");
                habitante.setImage("https://thispersondoesnotexist.com/image");
                fechaNacimiento = Calendar.getInstance();
                Random rand = new Random();
                int upperboundMonth = 11;
                int upperboundDay = 31;
                int month = rand.nextInt(upperboundMonth);
                int day = rand.nextInt(upperboundDay);
                int year= (int) (Math.random() * (2020 - 1900 + 1) + 1900);
                switch (month) {
                    case 0:
                        month = Calendar.JANUARY;
                        break;
                    case 1:
                        month = Calendar.FEBRUARY;
                        break;
                    case 2:
                        month = Calendar.MARCH;
                        break;
                    case 3:
                        month = Calendar.APRIL;
                        break;
                    case 4:
                        month = Calendar.MAY;
                        break;
                    case 5:
                        month = Calendar.JUNE;
                        break;
                    case 6:
                        month = Calendar.JULY;
                        break;
                    case 7:
                        month = Calendar.AUGUST;
                        break;
                    case 8:
                        month = Calendar.SEPTEMBER;
                        break;
                    case 9:
                        month = Calendar.OCTOBER;
                        break;
                    case 10:
                        month = Calendar.NOVEMBER;
                        break;
                    default:
                        month = Calendar.DECEMBER;
                        break;
                }
                Integer mes = month;
                Integer dia = day;
                if(mes.compareTo(3) == 0 || mes.compareTo(5) == 0 || mes.compareTo(8) == 0 || mes.compareTo(10) == 0) {
                    if(dia.compareTo(30) > 0) {
                        day = 30;
                    }
                }

                if(mes.compareTo(1) == 0 && day > 28) {
                    day = 28;
                }

                fechaNacimiento = Calendar.getInstance();
                fechaNacimiento.set(year, month, day);

                habitante.setFechaNacimiento(fechaNacimiento.getTime());
                habitante.setNacionalidad("ESPAÑA");

                int tarjeta = random.nextInt(4) + 16;
                System.out.println(tarjeta);
                identificador = this.identificadorService.findById((long) tarjeta);
                habitante.setTarjetaIdentificacion(identificador);
                habitante.setSexo(tarjeta % 2 == 0 ? "H" : "M");
                vivienda = this.viviendaService.findById((long) i + 7);
                System.out.println(vivienda);
                habitante.setVivienda(vivienda);

                habitantes.add(habitante);

            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Excepción en habitante " + e.getMessage());
                break;
            }
            // Se ha decidido guardar a todos de golpe por si se produce una excepción
            this.habitanteService.saveAll(habitantes);
        }

    }

    private String hashText(String text) {
        int dureza = 5;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(dureza, new SecureRandom());
        return encoder.encode(text);
    }

    private String createSalt() {
        int leftLimit = 65; // letter 'A'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 32;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @GetMapping("/identificacion")
    public Integer poblateIdentificacion() {
        Random random = new Random();
        List<Habitante> habitantes = this.habitanteService.findAll();
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for(Habitante h : habitantes) {
            Integer identificacion = h.getTarjetaIdentificacion().getCodigoTarjeta();
            if(identificacion == 1) { // Si el habitante tiene como código el DNI
                int randomNumber = random.nextInt(25);
                int dni = random.nextInt(99999999);
                h.setIdentificacion(Integer.toString(dni) + String.valueOf(alphabet.charAt(randomNumber)).toUpperCase());
            } else if (identificacion == 2) { // Si el habitante tiene pasaporte
                String letras =
                        alphabet.charAt((int) (Math.random() * 25)) +
                        String.valueOf(alphabet.charAt((int) (Math.random() * 25))) +
                                alphabet.charAt((int) (Math.random() * 25));
                int numeros = random.nextInt(999999);

                h.setIdentificacion(letras.toUpperCase() + numeros);
            }
            else if (identificacion == 3) {
                int numeros = random.nextInt(9999999);
                String letra = String.valueOf("XYZ".charAt(random.nextInt(3)));

                h.setIdentificacion(String.valueOf(numeros) + letra);
            }

            this.habitanteService.save(h);
        }

        return 200;
    }

    @PostMapping("/login")
    private Respuesta login(@RequestParam("username") String username, @RequestParam("password") String password) {
        Respuesta res = null;
        try {
            if("".equals(username) || "".equals(password) || null == username || null == password){
                return new Respuesta(350, null);
            }
            Administrador admin = administradorService.findByUsername(username);
            Assert.notNull(admin);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if(encoder.matches(password + admin.getCuentaUsuario().getSalt(), admin.getCuentaUsuario().getPassword())) {
                res = new Respuesta(200, admin);
            } else{
                res = new Respuesta(350, null);
            }
        } catch (Exception e) {
            // Excepción controlada
            res = new Respuesta(350, null);
        }

        return res;
    }

    @GetMapping("/prueba")
    public Respuesta pruebaMultiobjeto() {
        Map<String, Object> ls = new HashMap<>();
        ls.put("Cadena de texto", "A");
        ls.put("Número", 333);
        ls.put("habitante", this.administradorService.findByUsername("sergio"));

        return new Respuesta(200, ls);
    }

    @GetMapping("/habitantes/count")
    public Respuesta numHabitantes() {
        return new Respuesta(200, this.habitanteService.findAll().size());
    }

    @GetMapping("/habitantes/filter/dates")
    public Respuesta habitantesPorFechas(Date fechaFrom, Date fechaTo) {
        // Si la fecha hasta es nula, se toma como fecha tope, la fecha de ahora
        if(fechaTo == null) {
            fechaTo = new Date();
        }
        return null;
    }

    // TODO: Corregir problema se crean solicitudes pero el tipo y el subtipo no son del mismo grupo
    @GetMapping("/operaciones/create")
    public Integer newOperaciones() {
        List<String> tipos = new ArrayList<>();
        tipos.add("A");
        tipos.add("B");
        tipos.add("M");
        List<String> altas = new ArrayList<>();
        altas.add("AO");
        altas.add("AN");
        altas.add("ACR");

        List<String> bajas = new ArrayList<>();
        bajas.add("BCD"); // Baja por cambio de domicilio
        bajas.add("BD"); // Baja por defunción

        List<String> modificaciones = new ArrayList<>();
        modificaciones.add("MD");
        modificaciones.add("MV");


        List<Habitante> habitantes = this.habitanteService.findAll();
        Operacion op;
        for(int i = 0; i < 9; i++) {
            int opcion = (int) (Math.random() * (3));
            if(opcion > 2) {
                opcion = 2;
            }
            int range = 2;
            if(opcion == 0) {
                range = 3;
            }
            int subOpcion = (int) (Math.random() * (range));

            int habEscogido = (int) (Math.random() * habitantes.size());

            Habitante habitanteElegido = habitantes.get(habEscogido);
            habitantes.remove(habitanteElegido);
            op = new Operacion();
            op.setHabitante(habitanteElegido);
            op.setTipo(tipos.get(opcion));
            op.setSubtipo(opcion == 0 ?  altas.get(subOpcion) : (opcion == 1 ? bajas.get(subOpcion) : modificaciones.get(subOpcion)));
            Solicitud solicitud = new Solicitud();
            solicitud.setTipo(op.getTipo());
            solicitud.setSubtipo(op.getSubtipo());
            solicitud.setSolicitante(habitanteElegido);
            solicitud.setEstado("A");
            solicitud.setNombre(habitanteElegido.getNombre());
            solicitud.setPrimerApellido(habitanteElegido.getPrimerApellido());
            solicitud.setSegundoApellido(habitanteElegido.getSegundoApellido());
            solicitud.setIdentificacion(habitanteElegido.getIdentificacion());
            solicitud.setIdentificacion(habitanteElegido.getIdentificacion());
            solicitud.setFechaNacimiento(habitanteElegido.getFechaNacimiento());
            System.out.println(op.getTipo() + ", " + op.getSubtipo());
            this.solicitudService.save(solicitud);
            op.setSolicitud(solicitud);
            op.setFechaOperacion(new Date());
            op.setViviendaOrigen(habitanteElegido.getVivienda());
            op.setViviendaDestino(habitanteElegido.getVivienda());
            this.operacionService.save(op);
        }
        return 200;
    }

    @GetMapping("/habitantes/filter/alta")
    public List<Integer> habitantesConOPsAlta() {
        return this.operacionService.estadisticasHabsAlta();
    }

    @GetMapping("/habitantes/filter/baja")
    public List<Integer> habitantesConOPsBaja() {
        return this.operacionService.estadisticasHabsBaja();
    }

    @GetMapping("/habitantes/filter/modificacion")
    public List<Integer> habitantesConOPsModificacion() {
        return this.operacionService.estadisticasHabsModificacion();
    }
}
