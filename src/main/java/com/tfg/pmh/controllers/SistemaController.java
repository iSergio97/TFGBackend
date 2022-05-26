package com.tfg.pmh.controllers;

import com.tfg.pmh.models.*;
import com.tfg.pmh.services.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/sistema")
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
    private NumeracionService numeracionService;

    @Autowired
    private HojaService hojaService;

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

    // @GetMapping("/administrador/callejero")
    public Integer callejero() {
        try {
            File file = new ClassPathResource("/static/calles.txt").getFile();
            Scanner scanner = new Scanner(file);
            while(scanner.hasNext()) {
                String data = scanner.nextLine().trim();
                Calle calle = new Calle();
                String[] split = data.split(" ");
                calle.setTipo(split[0]);
                split = Arrays.copyOfRange(split, 1, split.length);
                calle.setNombre(String.join(" ", split));
                Municipio municipio = this.municipioService.findById(3L);
                calle.setMunicipio(municipio);
                this.calleService.save(calle);
            }
            return 200;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @GetMapping("/administrador/generateOPs")
    public Integer generarOperaciones() {
        Date start = new Date();
        int actualYear = start.getYear();
        start.setDate(1);
        start.setMonth(Calendar.JANUARY);

        Date end = new Date();
        end.setDate(31);
        end.setMonth(Calendar.DECEMBER);

        for(int i = 3; i > 0; i--) {
            start.setYear(actualYear - i);
            end.setYear(actualYear - i);
            for(int j = 0; j < 400; j++) {
                Date randomDate = new Date(ThreadLocalRandom.current().nextLong(start.getTime(), end.getTime()));
                Operacion op = new Operacion();
                long randomId = (long) (Math.random() * (12734 - 11876 + 1) + 11876);
                while(randomId % 2 != 0) {
                    randomId = (long) (Math.random() * (12734 - 11876 + 1) + 11876);
                }
                Habitante randomHab = this.habitanteService.findById(randomId);
                long randomIdHoja = (long) (Math.random() * (11874 - 890 + 1) + 890);
                while(randomIdHoja % 2 != 0) {
                    randomIdHoja = (long) (Math.random() * (11874 - 890 + 1) + 890);
                }
                Hoja randomHoja = this.hojaService.findById(randomIdHoja);
                op.setHoja(randomHoja);
                op.setHabitante(randomHab);
                op.setFechaOperacion(randomDate);
                op.setTipo("M");
                op.setSubtipo("MV");

                Solicitud solicitud = new Solicitud();
                solicitud.setSolicitante(randomHab);
                solicitud.setTipo("M");
                solicitud.setSubtipo("MV");
                solicitud.setFecha(randomDate);
                solicitud.setEstado("A");
                this.solicitudService.save(solicitud);
                op.setSolicitud(solicitud);
                this.operacionService.save(op);
            }
        }
        return 200;
    }

    @GetMapping("/administrador/poblate")
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

    @GetMapping("/fluctuacion")
    public List<Integer> fluctuacionHabitantes() {
        return null;
    }

    @GetMapping("/administrador/identificacion")
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

    @PostMapping("/administrador/login")
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
                List<Object> lista = new ArrayList<>();
                String token = getJWTToken(admin.getCuentaUsuario().getUsername());
                lista.add(admin);
                lista.add(token);
                res = new Respuesta(200, lista);
            } else{
                res = new Respuesta(350, null);
            }
        } catch (Exception e) {
            // Excepción controlada
            res = new Respuesta(350, null);
        }

        return res;
    }

    @GetMapping("/administrador/prueba")
    public Respuesta pruebaMultiobjeto() {
        Map<String, Object> ls = new HashMap<>();
        ls.put("Cadena de texto", "A");
        ls.put("Número", 333);
        ls.put("habitante", this.administradorService.findByUsername("sergio"));

        return new Respuesta(200, ls);
    }

    @GetMapping("/administrador/habitantes/count")
    public Respuesta numHabitantes() {
        return new Respuesta(200, this.habitanteService.findAll().size());
    }

    // TODO: Corregir problema se crean solicitudes pero el tipo y el subtipo no son del mismo grupo
    @GetMapping("/administrador/operaciones/create")
    public Integer newOperaciones() {
        List<String> tipos = new ArrayList<>();
        tipos.add("A");
        tipos.add("M");

        List<String> altas = new ArrayList<>();
        altas.add("AIM");
        altas.add("ACR");

        List<String> modificaciones = new ArrayList<>();
        modificaciones.add("MD");
        modificaciones.add("MV");
        modificaciones.add("MRE");


        List<Habitante> habitantes = this.habitanteService.findAll();
        Operacion op;
        for(int i = 0; i < 9; i++) {
            int opcion = (int) (Math.random() * (2));
            if(opcion > 2) {
                opcion = 1;
            }
            int range = 2;
            if(opcion == 1) {
                range = 3;
            }
            int subOpcion = (int) (Math.random() * (range));

            int habEscogido = (int) (Math.random() * habitantes.size());

            Habitante habitanteElegido = habitantes.get(habEscogido);
            habitantes.remove(habitanteElegido);
            op = new Operacion();
            op.setHabitante(habitanteElegido);
            op.setTipo(tipos.get(opcion));
            op.setSubtipo(opcion == 0 ?  altas.get(subOpcion) : modificaciones.get(subOpcion));
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
            this.operacionService.save(op);
        }
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
        Numeracion numeracion;
        List<Hoja> hoja;
        List<Habitante> habitantes = new ArrayList<>();

        Random random = new Random();

        crearNumeracion();

        for(int i = 0; i < 250; i++) {
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
                int year = (int) (Math.random() * (2020 - 1900 + 1) + 1900);
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
                identificador = this.identificadorService.findById((long) tarjeta);
                habitante.setTarjetaIdentificacion(identificador);
                habitante.setSexo(tarjeta % 2 == 0 ? "H" : "M");
                int low = 451;
                int high = 884;
                random = new Random();
                Long calleId = (long) random.nextInt((high-low)) + low;
                List<Numeracion> ls = this.numeracionService.findByCalleId(calleId);
                numeracion = ls.get(random.nextInt(ls.size() - 1) + 1);
                hoja = this.hojaService.findByNumeracion(numeracion.getId());
                habitante.setHoja(hoja.get(0));

                this.habitanteService.save(habitante);

            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Excepción en habitante " + e.getMessage());
                throw e;
            }
        }
    }

    @GetMapping("numeracion/geocoding")
    public List<Numeracion> getNumeracionAll() {
        return this.numeracionService.findAll();
    }

    // @PostMapping("/numeracion/geocoding")
    public void completarDireccion(@RequestParam("lat") Double lat, @RequestParam("lng") Double lng, @RequestParam("idNumeracion") Long idNumeracion) {
        Numeracion num = this.numeracionService.findById(idNumeracion);
        if(num.getLat() == null) {
            num.setLat(lat);
            num.setLng(lng);
            this.numeracionService.save(num);
        }
    }

    @GetMapping("/operacion/heatmap")
    public Respuesta heatMap() {
        Respuesta res = new Respuesta();
        try {
            // Lista que comprenden las operaciones de los 3 últimos años
            List<List<Operacion>> listaDeLista = this.operacionService.mapaDeCalor();
            res.setStatus(200);
            res.setObject(listaDeLista);
        } catch (Exception e) {
            res.setStatus(400);
            res.setObject(null);
        }
        return res;
    }

    private void crearNumeracion() {
        List<Calle> calles = this.calleService.findAll();
            calles.forEach((calle) -> {
            for(int i = 0; i < 4; i++) {
                int high = 50;
                int low = 1;
                Random random = new Random();
                int numero = random.nextInt((high-low)) + low;
                Numeracion numeracion = new Numeracion();
                numeracion.setCalle(calle);
                numeracion.setNumero(numero);
                String referenciaCatastral = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
                numeracion.setReferenciaCatastral(referenciaCatastral);
                this.numeracionService.save(numeracion);
                Hoja hoja = new Hoja();
                hoja.setNumeracion(numeracion);
                hoja.setHoja(i);
                this.hojaService.save(hoja);
            }
        });
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

    private String getJWTToken(String username) {
        String secretKey = "tfg-pmh-poh";

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ADMINISTRADOR");

        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 8*60*60*1000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return token;
    }
}
