package com.tfg.pmh.repositories;

import com.tfg.pmh.models.Habitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Collection;

@Repository
public interface HabitanteRepository extends JpaRepository<Habitante, Long> {

    @Query("SELECT H FROM Habitante  H WHERE H.Vivienda = ?1")
    public Collection<Habitante> findHabitanteByViviendaId(Long id);

    @Query("SELECT H FROM Habitante H WHERE H.cuentaUsuario.username = ?1")
    public Habitante findByUsername(String username);
}
