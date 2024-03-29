package com.tfg.pmh.repositories;

import com.tfg.pmh.models.Identificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface IdentificadorRepository extends JpaRepository<Identificacion, Long> {
}
