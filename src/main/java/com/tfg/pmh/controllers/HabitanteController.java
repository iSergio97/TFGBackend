package com.tfg.pmh.controllers;

import com.tfg.pmh.models.Habitante;
import com.tfg.pmh.services.HabitanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.security.provider.MD5;
import sun.security.provider.SecureRandom;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@RestController
@RequestMapping("/habitante")
@CrossOrigin(origins = {"*"})
public class HabitanteController {

    @Autowired
    private HabitanteService habitanteService;

    @GetMapping("/login")
    private ResponseEntity<Integer> login(String username, String password) {
        try {
            Habitante habitante = habitanteService.findByUsername(username);
            String hashedPassword = hashPassword(password, "ABCDE");
            assert hashedPassword != null;
            if(hashedPassword.equals(habitante.getCuentaUsuario().getPassword())) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else{
                return new ResponseEntity<>(HttpStatus.valueOf(401));
            }
        } catch (Exception e) {
            // Consultar si añadir opcion de mensajes de trazas
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    private String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        try {
            SecureRandom secureRandom = new SecureRandom();

            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 10, 512);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("SHA512");
            byte[] hash = skf.generateSecret(pbeKeySpec).getEncoded();

            //converting to string to store into database

            return Base64.getMimeEncoder().encodeToString(hash);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

}
