package com.tfg.pmh.services;

import com.tfg.pmh.models.Administrador;
import com.tfg.pmh.models.CuentaUsuario;
import com.tfg.pmh.repositories.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AdministradorService {

    /*
        Aquí irán, a parte de la creación de nuevos admins, todo lo relacionado con el sistema
        Puede que interese un modelo para poder hacer las queries, pero debería ser el administrador
        el que realice y trate esas queries
    */
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

    public Administrador findByUsername(String username) {
        return this.repository.findByUsername(username);
    }
}
