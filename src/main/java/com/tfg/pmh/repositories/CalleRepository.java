package com.tfg.pmh.repositories;

import com.tfg.pmh.forms.MapaCalor;
import com.tfg.pmh.models.Calle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CalleRepository extends JpaRepository<Calle, Long> {

    List<Calle> getCalleByMunicipioId(Long id);

    List<Calle> getCallesByTipo(String tipo);

    @Query("SELECT H.Hoja.numeracion.calle.nombre as calle, COUNT(H.Hoja.numeracion.calle.nombre) as cantidad FROM Habitante H GROUP BY H.Hoja.numeracion.calle.nombre")
    List<MapaCalor> mapaDeCalor();
}
