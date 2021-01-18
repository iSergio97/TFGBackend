package com.tfg.pmh.controllers;

import com.tfg.pmh.forms.CuentaUsuarioForm;
import com.tfg.pmh.models.CuentaUsuario;
import com.tfg.pmh.models.Habitante;
import com.tfg.pmh.models.Respuesta;
import com.tfg.pmh.repositories.CuentaUsuarioRepository;
import com.tfg.pmh.services.CuentaUsuarioService;
import com.tfg.pmh.services.HabitanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;

@RestController
@RequestMapping("/habitante")
@CrossOrigin(origins = {"*"})
public class HabitanteController {

    @Autowired
    private HabitanteService habitanteService;

    @Autowired
    private CuentaUsuarioService cuentaUsuarioService;

    // Documentar método
    @GetMapping("/login")
    private Respuesta login(String username, String password) {
        try {
            if("".equals(username) || "".equals(password)){
                return new Respuesta(350, null);
            }
            Habitante habitante = habitanteService.findByUsername(username);
            String hashedPassword = hashText(password);
            Assert.notNull(habitante);
            if(hashedPassword.equals(habitante.getCuentaUsuario().getPassword())) {
                return new Respuesta(200, habitante);
            } else{
                return new Respuesta(350, null);
            }
        } catch (Exception e) {
            // Excepción controlada
            return new Respuesta(350, null);
        }
    }

    @PostMapping("/user-account/edit")
    public Respuesta editUserAccount(@RequestBody CuentaUsuarioForm cuentaUsuario) {
        if(cuentaUsuarioService.existUsername(cuentaUsuario.getNewUsername())) {
            // Existe ya un usuario con ese username
            return new Respuesta(390, null);
        }

        CuentaUsuario userAccount = this.cuentaUsuarioService.findOne(cuentaUsuario.getId());
        String userPassword = userAccount.getPassword();

        if(userPassword == null) {
            return new Respuesta(380, null);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String newPassword = cuentaUsuario.getNewPassword() + userAccount.getSalt();

        if(!encoder.matches(newPassword, userPassword)) {
            return new Respuesta(370, null);
        }

        CuentaUsuario cs = cuentaUsuarioService.deconstruct(cuentaUsuario);

        // TODO: Revisar porqué no guarda ninguna entidad
        cuentaUsuarioService.save(cs);

        return new Respuesta(200, habitanteService.findByUsername(cuentaUsuario.getNewUsername()));
    }


    private String hashText(String text) {
        int dureza = 5;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(dureza, new SecureRandom());
        return encoder.encode(text);
    }

}
