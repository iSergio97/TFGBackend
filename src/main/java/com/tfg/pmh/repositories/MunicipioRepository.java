package com.tfg.pmh.repositories;

import com.tfg.pmh.models.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Long> {

    List<Municipio> findMunicipioByPaisId(Long id);
}
