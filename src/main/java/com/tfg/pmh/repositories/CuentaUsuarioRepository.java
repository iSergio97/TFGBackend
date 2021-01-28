package com.tfg.pmh.repositories;

import com.tfg.pmh.models.CuentaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CuentaUsuarioRepository extends JpaRepository<CuentaUsuario, Long> {
    @Query("SELECT COUNT(C) FROM CuentaUsuario C WHERE C.username = ?1 and C.id <> ?2")
    Integer existUsername(String username, Long id);
}
