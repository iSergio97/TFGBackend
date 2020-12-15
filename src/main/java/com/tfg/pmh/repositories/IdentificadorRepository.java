package com.tfg.pmh.repositories;

import com.tfg.pmh.models.Identificador;
import com.tfg.pmh.models.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentificadorRepository extends JpaRepository<Identificador, Long> {
}
