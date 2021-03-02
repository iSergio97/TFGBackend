package com.tfg.pmh.controllers;

import com.tfg.pmh.forms.CuentaUsuarioForm;
import com.tfg.pmh.models.CuentaUsuario;
import com.tfg.pmh.models.Habitante;
import com.tfg.pmh.models.Respuesta;
import com.tfg.pmh.services.CuentaUsuarioService;
import com.tfg.pmh.services.HabitanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
        Respuesta res = null;
        try {
            if("".equals(username) || "".equals(password) || null == username || null == password){
                return new Respuesta(350, null);
            }
            Habitante habitante = habitanteService.findByUsername(username);
            Assert.notNull(habitante);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if(encoder.matches(password + habitante.getCuentaUsuario().getSalt(), habitante.getCuentaUsuario().getPassword())) {
                res = new Respuesta(200, habitante);
            } else{
                res = new Respuesta(350, null);
            }
        } catch (Exception e) {
            // Excepción controlada
            res = new Respuesta(350, null);
        }

        return res;
    }

    @PostMapping("/user-account/edit")
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

    @GetMapping("/convivientes")
    public List<Habitante> convivientes(String vivienda, Integer numero, Long id) {
        List<Habitante> ls = new ArrayList<>();
        System.out.println(this.habitanteService.findConvivientes(vivienda, numero, id));
        return ls;

    }


    private String hashText(String text) {
        int dureza = 5;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(dureza, new SecureRandom());
        return encoder.encode(text);
    }
}
