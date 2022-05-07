package com.tfg.pmh.repositories;

import com.tfg.pmh.models.Numeracion;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NumeracionRepository extends JpaRepository<Numeracion, Long> {

    List<Numeracion> findNumeracionByCalleId(Long calleId);

    Numeracion findNumeracionByReferenciaCatastral(String referenciaCatastral);
}
