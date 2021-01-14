package com.tfg.pmh.controllers;

import com.tfg.pmh.models.Habitante;
import com.tfg.pmh.models.Respuesta;
import com.tfg.pmh.services.HabitanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;

@RestController
@RequestMapping("/habitante")
@CrossOrigin(origins = {"*"})
public class HabitanteController {

    @Autowired
    private HabitanteService habitanteService;

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

    private String hashText(String text) {
        int dureza = 5;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(dureza, new SecureRandom());
        return encoder.encode(text);
    }

}
