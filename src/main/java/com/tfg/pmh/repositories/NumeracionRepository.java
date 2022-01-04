package com.tfg.pmh.repositories;

import com.tfg.pmh.models.Numeracion;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface NumeracionRepository extends JpaRepository<Numeracion, Long> {
}
