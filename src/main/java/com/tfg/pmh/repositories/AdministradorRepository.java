package com.tfg.pmh.repositories;

import com.tfg.pmh.models.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

    @Query("SELECT A FROM Administrador A WHERE A.cuentaUsuario.username = ?1")
    Administrador findByUsername(String username);
}
