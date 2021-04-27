package com.tfg.pmh.controllers;

import com.tfg.pmh.forms.CuentaUsuarioForm;
import com.tfg.pmh.models.CuentaUsuario;
import com.tfg.pmh.models.Respuesta;
import com.tfg.pmh.services.CuentaUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;

@RestController
@RequestMapping("/perfil")
@CrossOrigin(origins = {"*"})
public class PerfilController {

    @Autowired
    private CuentaUsuarioService cuentaUsuarioService;

    @PostMapping("/actualizar")
    public Respuesta editUserAccount(@RequestBody CuentaUsuarioForm cuentaUsuario) {
        if("".equals(cuentaUsuario.getNewPassword()) || "".equals(cuentaUsuario.getNewUsername())) {
            return new Respuesta(380, null);
        }

        if(cuentaUsuarioService.existUsername(cuentaUsuario.getNewUsername(), cuentaUsuario.getId())) {
            // Existe ya un usuario con ese username
            return new Respuesta(390, null);
        }

        CuentaUsuario userAccount = this.cuentaUsuarioService.findOne(cuentaUsuario.getId());
        String userPassword = userAccount.getPassword();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String newPassword = cuentaUsuario.getCurrentPassword() + userAccount.getSalt();

        if(!encoder.matches(newPassword, userPassword)) {
            return new Respuesta(370, null); // Error de contraseña
        }

        CuentaUsuario cs = cuentaUsuarioService.deconstruct(cuentaUsuario);

        cs.setPassword(hashText(cs.getPassword() + cs.getSalt()));

        cuentaUsuarioService.save(cs);

        return new Respuesta(200, null);
    }

    private String hashText(String text) {
        int dureza = 5;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(dureza, new SecureRandom());
        return encoder.encode(text);
    }

}
