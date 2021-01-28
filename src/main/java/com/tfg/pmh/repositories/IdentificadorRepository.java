package com.tfg.pmh.repositories;

import com.tfg.pmh.models.Identificador;
import com.tfg.pmh.models.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentificadorRepository extends JpaRepository<Identificador, Long> {
}
