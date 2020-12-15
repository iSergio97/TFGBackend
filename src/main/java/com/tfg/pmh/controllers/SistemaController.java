package com.tfg.pmh.controllers;

import com.tfg.pmh.models.*;
import com.tfg.pmh.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.Id;
import javax.xml.ws.Response;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

@RestController
@RequestMapping("/sistema/administrador/")
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

    @GetMapping("info")
    public List<Integer> systemInfo() {
        // Puede que interese crear un objeto Info, no persistente, para almacenar estas cosas
        return new ArrayList<>();
    }

    @GetMapping("poblate")
    public Integer poblate() throws NoSuchAlgorithmException {

        //Creamos las tarjeta de identificación
        /*
            0 - No identificación
            1 - NIF
            2 - Pasaporte
            3 - Tarjeta de extranjería
         */
        Identificador menor = new Identificador();
        menor.setCodigoTarjeta(0);
        menor.setNombreTarjeta("Menor de edad");
        identificadorService.save(menor);

        menor = new Identificador();
        menor.setCodigoTarjeta(1);
        menor.setNombreTarjeta("NIF");
        identificadorService.save(menor);

        menor = new Identificador();
        menor.setCodigoTarjeta(2);
        menor.setNombreTarjeta("Pasaporte");
        identificadorService.save(menor);

        menor = new Identificador();
        menor.setCodigoTarjeta(3);
        menor.setNombreTarjeta("Tarjeta de extranjería");
        identificadorService.save(menor);
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

        administradorService.save(administrador);
    }

    private void cuentaHabitantes() throws NoSuchAlgorithmException {
        CuentaUsuario cuentaUsuario;
        Habitante habitante;
        Calendar fechaNacimiento;
        Identificador identificador;
        Vivienda vivienda;
        String calles = "A-4円Autovía de Andalucía円Autovía del Sur円Av Andalucía円Av Antonio Piña円Av Blas Infante円Av de Andalucía円Av de Francisco Díaz円Av de Jerónimo de Aguilar円Av de John Lennon円Av de la Alcarrachela円Av de la Libertad円Av de la Plaza de Toros円Av de Limero円Av de los Emigrantes円Av de los Toreros円Av de María Auxiliadora円Av de Miguel de Cervantes円Av de Miguel de Cervantes, 41, 41400 Écija, España円Av de Nuestra Señora del Valle円Av de Nuestra Señora del Valle, 73, 41400 Écija, España円Av de San Benito円Av de San Juan de la Salle円Av de Tomás Bevía円Av de Tomás Bevía, 1円Av de Toros円Av del Cristo de Confalón円Av del Doctor Fleming円Av del Doctor Sánchez Malo円Av del Ferrocarril円Av del Genil円Av Doctor Sánchez Malo円Av Emigrantes円Av II Republica円Av Manuel de Falla円Avenida Andalucía円Avenida Blas Infante円Avenida Blas Infante, 8, 41400 Écija, Sevilla, España円Avenida Constitución円Avenida Cristo de Confalón円Avenida de Andalucía円Avenida de la Alcarrachela円Avenida de los Emigrantes円Avenida de los Ilustrados円Avenida de María Auxiliadora円Avenida de Miguel de Cervantes円Avenida de Toros円Avenida del Ferrocarril円Avenida del Genil円Avenida Doctor Fleming円Avenida Doctor Sánchez Malo円Avenida Francisco Díaz円Avenida II República円Avenida Jerónimo de Aguilar円Avenida John Lennon円Avenida la Libertad円Avenida Limero円Avenida los Emigrantes円Avenida los Toreros円Avenida Manuel de Falla円Avenida María Auxiliadora円Avenida Miguel de Cervantes円Avenida Nuestra Señora del Valle円Avenida Ocho de Marzo円Avenida Plaza de Toros円Avenida Tomás Beviá円Barriada Almazara円Barriada Colonda円Barriada de la Almazara円Barriada de la Fuensanta円Barriada de la Paz円Barriada de la Paz, 7, 41400 Écija, España円Barriada de San Pablo円Barriada Destacamento Obras Publicas円Barriada la Fuensanta円Barriada la Paz円Barriada los Miradores円Barriada Nueva Andalucía円Barriada San Agustín円C/ BENEFICIADOS円C/ GENERAL WEYLER円C/ José Herrainz Caraballo円C/ Marquesa円C/ MERCED円C/ Pintor Fernández Briones円C/ Santiago円c/. San Francisco円Calle 4 de diciembre円Calle Abeto円Calle Abogado Juan Antonio Gamero円Calle Acacias円Calle Aceite円Calle Adriano円Calle Aguabajo円Calle Aguayo円Calle Agustín Rivero円Calle Álamo円Calle Álamos円Calle Albaicín円Calle Alcázar円Calle Alfareros円Calle Alfares円Calle Alfonso Aragón 'Fofó円Calle Algeciras円Calle Alhambra円Calle Alisio円Calle Almendro円Calle Almenillas円Calle Almonas円Calle Alonso Fernández de Grajera円Calle Álvaro Custodio円Calle Amadeo Arias円Calle Amadeo Vives円Calle Amapola円Calle Ancha円Calle Andrés Segovia円Calle Antequera円Calle Antón de Arjona円Calle Antonio Guerra Gonzále円Calle Antonio Guerra GonzalezCalle Antonio Romero Martín円Calle Arabella円Calle Arahales円Calle Arquillos円Calle Arrieros円Calle Arroyo円Calle Artesanos円Calle Asturias円Calle Avendaño円Calle Azacanes円Calle Azofaifo円Calle Bachiller円Calle Bañales円Calle Barba円Calle Barcelona円Calle Barquete円Calle Barrasa円Calle Barrera de San Gregorio円Calle Barrera Oñate円Calle Barrero円Calle Barriada de la Fuensanta 10円Calle Bartolomé Jiménez Torres円Calle Bataneros円Calle Beatas円Calle Bellidos円Calle Beneficiados円Calle Berbisa円Calle Bizco Pardal円Calle Boabdil円Calle Bodegas円Calle Brisa円Calle Cádiz円Calle Caleros円Calle Camille Claudel円Calle Camilo José Cela円Calle Camino de la Fuensanta円Calle Campillo円Calle Campomanes円Calle Cañaveralejo円Calle Cánovas del Castillo円Calle Capataz Gutiérrez Tagua円Calle Capataz- Gutiérrez -Tagua円Calle Capilla円Calle Cárcel円Calle Carmelitas円Calle Carmen円Calle Carmona円Calle Carreras円Calle Cartuja円Calle Casilla de Alberto Postigo円Calle Casilla de la Lagunilla円Calle Casilla del Rubio Inés円Calle Castaño円Calle Castilla-la Mancha円Calle Castilla-León円Calle Castril円Calle Caus円Calle Cava円Calle Cavilla円Calle Cecilia円Calle Celestino Montero円Calle Cerro de la Concepción円Calle Cerro de la Habana円Calle Cerro de la Pólvora円Calle Cesáreo Cambronero円Calle Céspedes円Calle Cestería円Calle Chopo円Calle Cierzo円Calle Cinteria円Calle Ciprés円Calle Ciudad de Aranjuez円Calle Clara Campoamor円Calle Columela円Calle Comedias円Calle Compañía円Calle Compositor Juan Bermudo円Calle Comunidad de Madrid円Calle Concordia円Calle Conde円Calle Cordero円Calle Cordoba円Calle Cordobés円Calle Coronado円Calle Cuatro de Diciembre円Calle Curtidores円Calle de Albaicín円Calle de Albertos円Calle de Alcázar円Calle de Alfares円Calle de Algeciras円Calle de Almenillas円Calle de Almería円Calle de Almonas円Calle de Alonso円Calle de Antequera円Calle de Antón de Arjona円Calle de Arahales円Calle de Bachiller円Calle de Bañales円Calle de Barba円Calle de Barquete円Calle de Barrasa円Calle de Bartolomé Jiménez Torres円Calle de Beatas円Calle de Bellidos円Calle de Beneficiados円Calle de Berbisa円Calle de Bermuda円Calle de Bizco Pardal円Calle de Boabdil円Calle de Bocoloma円Calle de Cabriteros円Calle de Cádiz円Calle de Calderón円Calle de Cañaveralejo円Calle de Canio Rufo円Calle de Cánovas del Castillo円Calle de Capataz Gutiérrez Tagua円Calle de Carmona円Calle de Carreras円Calle de Caus円Calle de Cecilia円Calle de Celestino Montero円Calle de Cesáreo Cambronero円Calle de Céspedes円Calle de Cesteria円Calle de Cintería円Calle de Comedias円Calle de Córdoba円Calle de Cordobés円Calle de Coronado円Calle de Cronista Martín Jiménez円Calle de Delgadillo円Calle de Drago円Calle de Elvira円Calle de Emilio Castelar円Calle de Enrique 'El Gandinga'円Calle de Eslava円Calle de Fernández Pintado円Calle de Fernando III円Calle de Fernando Labrada円Calle de Fiel円Calle de Forjadores円Calle de Francia円Calle de Francos円Calle de Fray Juan Bermudo円Calle de Galindo円Calle de Gameras円Calle de Garay y Conde円Calle de Garci Sánchez円Calle de Garcilaso円Calle de Garcilópez円Calle de Gonzalo円Calle de Granada円Calle de Guadalajara円Calle de Guillermo Gutiérrez円Calle de Hernán Pérez円Calle de Huelva円Calle de Ignacio de Soto円Calle de Isaac Peral円Calle de Italia円Calle de Jaén円Calle de Jaime Ostos円Calle de Jesús Sin Soga円Calle de Joaquín Turina円Calle de Jorge Manrique円Calle de José Canalejas円Calle de Jovar円Calle de Juan de Angulo円Calle de Juan Muñoz円Calle de Juan Páez円Calle de Juan Pavón円Calle de Juan XXIII円Calle de Jurado円Calle de la A円Calle de la Albarrana円Calle de la Alcarria円Calle de La Alhambra円Calle de la Amapola円Calle de la Arabella円Calle de la Calzada円Calle de la Calzada円Calle de la Campana円Calle de la Capilla円Calle de la Cárcel円Calle de la Cartuja円Calle de la Cava円Calle de la Compañía円Calle de la Concordia円Calle de la Doctrina円Calle de la Empedrada円Calle de la Encina円Calle de la Espada円Calle de la Estepa円Calle de la Feria円Calle de la Guerra円Calle de La Haya円Calle de la Herrera円Calle de la Luisiana円Calle de la Luna円Calle de la Marquesa円Calle de la Merced円Calle de la Mezquita円Calle de la Muralla円Calle de la Padilla円Calle de la Paloma円Calle de la Pedregosa円Calle de la Platería円Calle de la Puerta Nueva円Calle de la Pulgosa円Calle de La Rinconada円Calle de la Rueda円Calle de la Soledad円Calle de la Tórtola円Calle de la Vega円Calle de la Victoria円Calle de la Virgen de la Piedad円Calle de las Acacias円Calle de las Bodegas円Calle de las Cadenas円Calle de las Carmelitas円Calle de las Flores円Calle de las Huertas円Calle de las Navajas円Calle de las Parteras円Calle de las Recogidas円Calle de las Rejas円Calle de las Rojas円Calle de las Salas円Calle de las Tres Cruces円Calle de las Vacas円Calle de las Violetas円Calle de las Vírgenes円Calle de las Zayas円Calle de las Zurcideras円Calle de Lebrija円Calle de Leonis円Calle de Leonor円Calle de López円Calle de Lorenzo Lucena円Calle de los Alfareros円Calle de los Arquillos円Calle de los Azacanes円Calle de los Bataneros円Calle de los Caleros円Calle de los Curtidores円Calle de los Dos Pozos円Calle de los Girasoles円Calle de los Henchideros円Calle de los Julianes円Calle de los Mármoles円Calle de los Mostaceros円Calle de los Nardos円Calle de los Olivares円Calle de los Parralejos円Calle de los Ramos円Calle de los Romeros円Calle de los Rosales円Calle de los Yepes円Calle de los Zapateros円Calle de Lucas円Calle de Luque円Calle de Maestre円Calle de Málaga円Calle de Malaliño円Calle de Mandoble円Calle de Manuel Ostos y Ostos円Calle de Marchena円Calle de María Bel En Peña円Calle de María Guerrero円Calle de Maritorija円Calle de Más y Prat円Calle de Mendoza円Calle de Merinos円Calle de Moleros円Calle de Morería円Calle de Mortecina円Calle de Najeras円Calle de Nueva円Calle de Oñate円Calle de Ostos円Calle de Pacheco円Calle de Pepe Luis Vargas円Calle de Pío XII円Calle de Portugal円Calle de Pradiz円Calle de Rafael María Molina円Calle de Ramón y Cajal円Calle de Reina円Calle de Rigoberta Menchú円Calle de Rodríguez Marín円Calle de San Antonio円Calle de San Bartolomé円Calle de San Cristóbal円Calle de San Francisco円Calle de San Fulgencio円Calle de San Gregorio円Calle de San Juan Bosco円Calle de San Marcos円Calle de San Pablo円Calle de Santa Beatriz de Silva円Calle de Santa Brígida円Calle de Santa Catalina円Calle de Santa Cruz円Calle de Santa Florentina円Calle de Santa Inés円Calle de Santiago円Calle de Sevilla円Calle de Sor Angela de la Cruz円Calle de Sor Cándida Sáinz Alonso円Calle de Soria円Calle de Tello円Calle de Teresa de Calcuta円Calle de Torres Quevedo円Calle de Useras円Calle de Valderrama円Calle de Vélez de Guevara円Calle de Vicente Aleixandre円Calle de Victoriano Valpuesta円Calle de Vidal円Calle de Villarreal円Calle de Villates円Calle de Zamorano円Calle del Abeto円Calle del Aceite円Calle del Adarve円Calle del Aguabajo円Calle del Aguayo円Calle del Almendro円Calle del Aragón Alfonso Fofo円Calle del Arcipreste Aparicio円Calle del Arco de Belén円Calle del Arroyo円Calle del Avendaño円Calle del Azofaifo円Calle del Bonsái円Calle del Camino de la Fuensanta円Calle del Carmen円Calle del Castaño円Calle del Castril円Calle del Cerrillo円Calle del Chopo円Calle del Ciprés円Calle del Clavel円Calle del Conde円Calle del Doctor Benítez円Calle del Doctor Jiménez de Lorite円Calle del Ébano円Calle del Emparedamiento円Calle del Espíritu Santo円Calle del Estatuto de Autonomía円Calle del Evaristo Espinosa円Calle del Fresno円Calle del General Weyler円Calle del Generalife円Calle del Horno円Calle del Hospital円Calle del Huerto円Calle del Minarete円Calle del Nogal円Calle del Olmo円Calle del Palo Santo円Calle del Palomar円Calle del Paraíso円Calle del Pardillo円Calle del Pardo円Calle del Pensamiento円Calle del Peso円Calle del Picadero Alto円Calle del Picador円Calle del Pilarejo円Calle del Pino円Calle del Pintor Fernando Briones円Calle del Poeta Manolo Mora円Calle del Pozo円Calle del Practicante Manuel Romero Nieto円Calle del Practicante Romero Gordillo円Calle del Primero de Mayo円Calle del Profesor Hernández Díaz円Calle del Puente円Calle del Regidor円Calle del Rejón円Calle del Reloj円Calle del Río円Calle del Robles円Calle del Rocío円Calle del Rosario円Calle del Sacromonte円Calle del Saltadero円Calle del Salto円Calle del Sauce円Calle del Secretario Armesto円Calle del Severo Ochoa円Calle del Sol円Calle del Sumidero円Calle del Tarancón円Calle del Torcal円Calle del Trascampanario円Calle del Ventorrillo円Calle Delgadillo円Calle Diamantino Garcia円Calle Doctor Benítez円Calle Doctor Ignacio Osuna Gomez円Calle Doctor Jiménez de Lorite円Calle Doctor Mariano Torres円Calle Doctrina円Calle Dolores Ibárruri円Calle Dos Pozos円Calle Dr Luis Gil Toresano円Calle Drago円Calle Dressel円Calle Ebanista円Calle Ébano円Calle Ecija円Calle Elvira円Calle Emilio Castelar円Calle Emilio Castelar Antes Caballeros円Calle Emparedamiento円Calle Empedrada円Calle Encina円Calle Enrique el Gandinga円Calle Ermita円Calle Escritor Mas y Laglera円Calle Eslava円Calle Espada円Calle Espíritu Santo円Calle Estatuto de Autonomía円Calle Este円Calle Estepa円Calle Eugenio d'Ors円Calle Evaristo Espinosa円Calle Extremadura円Calle Federica Montseny円Calle Federico García Lorca円Calle Feria円Calle Fernández Pintado円Calle Fernando III円Calle Fiel円Calle Fiesta円Calle Forjadores円Calle Francia円Calle Francisco Mateo Díaz González円Calle Francos円Calle Fray Agustín de los Reyes円Calle Fray Carlos Amigo Vall円Calle Fray Carlos Amigo Vallejo円Calle Fray Juan Bermudo円Calle Fresno円Calle Frida Kalho円Calle Fuente Nueva円Calle Fuentes de Andalucía円Calle Gabriela Mistral円Calle Galindo円Calle Gameras円Calle Garay y Conde円Calle García Tejero円Calle Garcilaso円Calle Garcilopez円Calle General Weyler円Calle Generalife円Calle Gonzalo円Calle Granada円Calle Guadalajara円Calle Guerra円Calle Guillermo Gutiérrez円Calle Haya円Calle Henchideros円Calle Hernán Pérez円Calle Herrera円Calle Historiador Padre Martín de Roa円Calle Horno円Calle Hospital円Calle Hospitalet円Calle Huelva円Calle Huerta de la Pintada円Calle Huerto円Calle Ignacio de Soto円Calle Ignacio Ellacuria円Calle II República円Calle Impresor Juan de los Reyes円Calle Isaac Albéniz円Calle Isaac Peral円Calle Isadora Duncan円Calle Isla Cíes円Calle Isla de Alborán円Calle Islas Canarias円Calle Italia円Calle Jaén円Calle Jaime Ostos円Calle Jesús del Gran Poder円Calle Jesús Nazareno円Calle Joaquin Ojeda円Calle Joaquín Pavón円Calle Joaquín Rodrigo円Calle José Canalejas円Calle José Fernández Sigles円Calle José Herráinz Caraballo円Calle Jose Manuel Gracia Caparros円Calle Jovar円Calle Juan Antonio Gamero円Calle Juan de Angulo円Calle Juan Díaz Custodio円Calle Juan Jiménez Ripoll円Calle Juan Pablo II円Calle Juan Páez円Calle Juan XXIII円Calle Julianes円Calle Julio Galio円Calle Jurado円Calle la Calzada円Calle la Campana円Calle la Cartuja円Calle la Huerta円Calle la Marquesa円Calle La Rioja円Calle la Victoria円Calle Lebrija円Calle Lebron円Calle Leonor円Calle López円Calle Lorenzo Lucena円Calle Los Merinos円Calle Loza円Calle Lucano円Calle Luis Cernuda円Calle Luis Lucena円Calle Luisiana円Calle Luna円Calle Luque円Calle Maestre円Calle Maestro Arrieta円Calle Málaga円Calle Malaliño円Calle Mandoble円Calle Manuel de Falla円Calle Manuel Ostos y Ostos円Calle Marchena円Calle Margarita Xirgu円Calle María Curie円Calle María de la O円Calle María de la O Lejárraga円Calle Maria Teresa Leon円Calle Mariana Pineda円Calle Marie Curie円Calle Marinaleda円Calle Maritorija円Calle Mármoles円Calle Marquesa de Peñaflor円Calle Mas y Prat円Calle Mayor円Calle Mendizabal円Calle Mendoza円Calle Merced円Calle Merinos円Calle Mezquita円Calle Milagrosa de la Matta円Calle Minarete円Calle Miragenil円Calle Moderato de Gades円Calle Molino Batañejo円Calle Morería円Calle Mujeres Jornaleras円Calle Muralla円Calle Nájera円Calle Naranjillos円Calle Nardo円Calle Navajas円Calle Navarra円Calle Norias I R円Calle Nueva円Calle Olmo円Calle Ostos円Calle Osuna円Calle Pablo de Olavide円Calle Pablo Iglesias円Calle Pacheco円Calle Padilla円Calle Palo Santo円Calle Paloma円Calle Palomar円Calle Panaderos円Calle Paraíso円Calle Parteras円Calle Pedregosa円Calle Pensamiento円Calle Pepe Luis Vargas円Calle Pepita Tomás円Calle Perdiz円Calle Peso円Calle Petra Kelly円Calle Picadero Bajo円Calle Picador円Calle Pilar Miró円Calle Pinichi円Calle Pino円Calle Pinsapo円Calle Pintor Fernando Briones円Calle Pío XII円Calle Platería円Calle Plaza Central円Calle Poeta Balmaseda González円Calle Poeta Balmaseda y González円Calle Poeta Manolo Mora円Calle Poncio Latrón円Calle Portugal円Calle Pozo円Calle Pozoseco円Calle Practicante Romero Gordillo円Calle Primero de Mayo円Calle Profesor Collantes de Terán円Calle Profesor Hernández Día円Calle Profesor Hernández Díaz円Calle Profesor Sancho Corbacho円Calle Pueblo Saharaui円Calle Puente円Calle Puerta Nueva円Calle Rafael María Aguilar円Calle Ramón y Cajal円Calle Recogidas円Calle Regidor円Calle Rejano円Calle Rejón円Calle Reloj円Calle Rigoberta Menchú円Calle Rinconada円Calle Río円Calle Rio Blanco円Calle Río Cabra円Calle Roble円Calle Robles円Calle Rocío円Calle Rodríguez Marín円Calle Rojas円Calle Ronda de las Huertas円Calle Ronda de San Agustín円Calle Ronda San Agustín円Calle Rosales円Calle Ruimartin円Calle Ruiz Martín円Calle Sacromonte円Calle Saltadero円Calle Salto円Calle San Antonio円Calle San Bartolomé円Calle San Benito円Calle San Cristóbal円Calle San Felipe Neri円Calle San Francisco円Calle San Fulgencio円Calle San Gregorio円Calle San Juan Bosco円Calle San Marcos円Calle San Pablo円Calle Santa Ana円Calle Santa Angela de la Cruz円Calle Santa Brígida円Calle Santa Catalina円Calle Santa Cruz円Calle Santa Florentina円Calle Santa Inés円Calle Santa Lucía円Calle Santiago円Calle Sauce円Calle Secretario Armesto円Calle Séneca円Calle Severo Ochoa円Calle Sevilla円Calle Sol円Calle Soledad円Calle Sor Angela de la Cruz円Calle Sor Cándida Sainz Alonso円Calle Soria円Calle Tarancón円Calle Tello円Calle Teresa de Calcuta円Calle Tomas Bevia円Calle Torcal円Calle Torres Quevedo円Calle Tórtola円Calle Trajano円Calle Trascampanario円Calle Tres Cruces円Calle Vacas円Calle Valderrama円Calle Valencia円Calle Vega円Calle Ventorrillo円Calle Vicente Alexandre円Calle Victoria Kent円Calle Victoriano Valpuesta円Calle Vidal円Calle Villa de Madrid円Calle Villa de Pabellón Sousbois円Calle Villa de Pavillon Sous Bois円Calle Villanueva del Rey円Calle Villarreal円Calle Villates円Calle Violetas円Calle Virgen de la Piedad円Calle Vírgenes円Calle Yepes円Calle Zamorano円Calle Zapateros円Calle Zayas円Calle Zurcideras円Camino de de las Huertas円Camino de Écija a Herrera円Camino de Físico円Camino de los Romeros円Camino del Campillo円Camino del Físico円Camino del Valle円Camino Físico円Camino Fuensanta円Camino la Ponderosa円Cañada del Rosal円Carrera円Carretera Cañada del Rosal円Carretera de Cañada Rosal円Carretera de la Cañada del Rosal円Carretera de la Herrera円Carretera de Palma del Río円Carretera Écija -Herrera I R円Carretera Madrid Cádiz円Carretera Osuna円Carretera Palma del Río円Carretera Puente del Genil円Carretera Rubio円CO-4311円CP-191円CP-246円Dehesa de las Yeguas円Doctor Mariano Torres円E-5円Écija, Calle de Cánovas del Castillo, 4, 41400 Écija, España円Estación de Autobuses de Ecija円Grupo San Hermenegildo円Grupo Santa Ana円Grupo Santo Domingo円Iglesia de Santiago円Isaac Albeniz円Joaquín Turina円José Herrainz Caraballo円Los Girasoles円Nueva円Pasaje Curro Romero円Pasaje la Milagrosa円Pasaje las Celindas円Pasaje Virgen de Soterraño円Pasaje Virgen del Rocío円Pje de la Milagrosa円Pje de la Virgen de Soterraño円Pje de la Virgen del Rocío円Pje de las Celindas円Plaza Armas円Plaza Camilo J Cela円Plaza Camilo José Cela円Plaza Campiña円Plaza Central円Plaza Colón円Plaza de Armas円Plaza de Colón円Plaza de España円Plaza de Europa円Plaza de Gilés y Rubio円Plaza de la Constitución円Plaza de la Ópera円Plaza de la Puerta Osuna円Plaza de la Santa María円Plaza de la Zarzuela円Plaza de las Armas円Plaza de Luis Vélez de Guevara円Plaza de Nuestra Señora del Valle円Plaza de San Gil円Plaza de San Juan円Plaza de Santa Irene円Plaza de Santo Domingo円Plaza de Viriato円Plaza del Matadero円Plaza Dulce Chacón円Plaza España円Plaza Giles y Rubio円Plaza Matadero円Plaza Mirador el Industrial円Plaza Nuestra Señora de los Dolores円Plaza Nuestra Señora del Valle円Plaza Plazvela de Santa María円Plaza Puerta Cerrada円Plaza Puerta Osuna円Plaza Quintana円Plaza San Gil円Plaza San Juan円Plaza Santa Irene円Plaza Santa María円Plaza Tolerancia円Plaza Toros円Plaza Vélez de Guevara円Plaza Virgen del Rocio円Plaza Viriato円Plaza Zarzuela円Plazuela de la Puerta Cerrada円Polígono El Mirador円Polígono Industrial Lagunilla円Polígono Lagunilla円Polígono Virgen del Rocio円Portugal円Quinta de Machado円Ronda de la Paz円Ronda de las Huertas円Ronda de los Molinos円Ronda de San Agustín円Ronda del Ferrocarril円Ronda el Ferrocarril円Ronda San Agustín円SE-9104円SE-9105円Urbanización de las Palmeras円Urbanización las Palmeras";
        List<String> callesList = Arrays.asList(calles.split("円"));

        for(int i = 0; i < 100; i++) {
            try {
                System.out.println("Creación habitante " + i);
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

                int tarjeta = (int)(Math.random() * (4));
                identificador = this.identificadorService.findById((long) tarjeta);
                habitante.setTarjetaIdentificacion(identificador);
                habitante.setSexo(tarjeta % 2 == 0 ? "H" : "M");

                int calle = (int) (Math.random() * (callesList.size() +1));
                vivienda = new Vivienda();
                vivienda.setCalle(callesList.get(i));
                vivienda.setPais("ESPAÑA");
                vivienda.setProvincia("SEVILLA");
                vivienda.setMunicipio("ÉCIJA");
                viviendaService.save(vivienda);

                habitante.setVivienda(vivienda);

                habitanteService.save(habitante);

                System.out.println("Habitante " + i + " creado");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Excepción en habitante " + e.getMessage());
                break;
            }
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
}
