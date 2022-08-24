package com.tfg.pmh.repositories;

import com.tfg.pmh.models.Habitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.List;

@Repository
public interface HabitanteRepository extends JpaRepository<Habitante, Long> {

    @Query("SELECT H FROM Habitante H WHERE H.Hoja.id = ?1 AND H.Hoja.hoja = ?2 AND H.id <> ?3")
    Collection<Habitante> findConvivientes(Long hojaId, Integer hoja, Long habId);

    @Query("SELECT H FROM Habitante H WHERE H.cuentaUsuario.username = ?1")
    Habitante findByUsername(String username);

    @Query("SELECT H.sexo as nombre, COUNT(H.sexo) as contador FROM Habitante H WHERE H.estado = 'A' GROUP BY H.sexo")
    List<CountHabitantes> contadorSexoHabitantes();

    @Query("SELECT H.sexo as nombre, COUNT(H.sexo) as contador FROM Habitante H WHERE (?3 - YEAR(H.fechaNacimiento)) >= ?1 AND (?3 - YEAR(H.fechaNacimiento)) <= ?2 AND H.estado = 'A' GROUP BY H.sexo")
    List<CountHabitantes> contadorSexoHabitantesFiltro(Integer edadDesde, Integer edadHasta, Integer anoActual);
}
