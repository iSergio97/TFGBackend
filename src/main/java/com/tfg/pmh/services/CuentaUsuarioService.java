package com.tfg.pmh.services;

import com.tfg.pmh.forms.CuentaUsuarioForm;
import com.tfg.pmh.models.CuentaUsuario;
import com.tfg.pmh.repositories.CuentaUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CuentaUsuarioService {

    @Autowired
    private CuentaUsuarioRepository repository;

    public CuentaUsuario create() {
        CuentaUsuario res = new CuentaUsuario();
        res.setUsername("");
        res.setPassword("");
        res.setSalt(this.getSalt());

        return res;
    }

    public void save(CuentaUsuario cuentaUsuario) {
        assert cuentaUsuario != null;

        this.repository.save(cuentaUsuario);
    }

    public CuentaUsuario findOne(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    private String getSalt() {
        String[] combination = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".split("");
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < 8; i++) {
            Random generator = new Random();
            int random = generator.nextInt(combination.length);
            res.append(combination[random]);
        }
        return res.toString();
    }

    public Boolean existUsername(String username, Long id) {
        return this.repository.existUsername(username, id) != 0;
    }

    public CuentaUsuario deconstruct(CuentaUsuarioForm cuentaUsuarioForm) {
        CuentaUsuario cuentaUsuario = this.findOne(cuentaUsuarioForm.getId());

        cuentaUsuario.setUsername(cuentaUsuarioForm.getNewUsername());
        cuentaUsuario.setPassword(cuentaUsuarioForm.getNewPassword());

        return cuentaUsuario;
    }
}
