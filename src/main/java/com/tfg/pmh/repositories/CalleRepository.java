package com.tfg.pmh.repositories;

import com.tfg.pmh.models.Calle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalleRepository extends JpaRepository<Calle, Long> {

    List<Calle> getCalleByMunicipioId(Long id);
}
