package com.tfg.pmh.repositories;

import com.tfg.pmh.models.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia, Long> {

    List<Provincia> findProvinciaByPaisId(Long pais_id);
}
