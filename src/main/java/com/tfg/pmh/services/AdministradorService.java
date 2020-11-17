package com.tfg.pmh.services;

import com.tfg.pmh.models.Administrador;
import com.tfg.pmh.models.CuentaUsuario;
import com.tfg.pmh.repositories.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository repository;

    @Autowired
    private CuentaUsuarioService cuentaUsuarioService;

    public void save(Administrador admin) {
        assert admin != null;

        this.repository.save(admin);
    }
    /* Se ha eliminado la opción delete ya que no es necesario eliminar a nadie, símplemente con poner su estado en baja, sobra */

    public Administrador findOne(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    public Collection<Administrador> findAll() {
        return this.repository.findAll();
    }
}
