package com.tfg.pmh.controllers;

import com.tfg.pmh.forms.CuentaUsuarioForm;
import com.tfg.pmh.models.CuentaUsuario;
import com.tfg.pmh.models.Habitante;
import com.tfg.pmh.models.Respuesta;
import com.tfg.pmh.services.CuentaUsuarioService;
import com.tfg.pmh.services.HabitanteService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
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
    @PostMapping("/login")
    private Respuesta login(@RequestParam("username") String username, @RequestParam("password") String password) {
        Respuesta res = new Respuesta();
        try {
            if("".equals(username) || "".equals(password) || null == username || null == password){
                return new Respuesta(350, null);
            }
            Habitante habitante = habitanteService.findByUsername(username);
            Assert.notNull(habitante);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if(encoder.matches(password + habitante.getCuentaUsuario().getSalt(), habitante.getCuentaUsuario().getPassword())) {
                List<Object> lista = new ArrayList<>();
                String token = getJWTToken(habitante.getCuentaUsuario().getUsername());
                lista.add(habitante);
                lista.add(token);
                res = new Respuesta(200, lista);
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
        return new ArrayList<>();
    }


    private String hashText(String text) {
        int dureza = 5;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(dureza, new SecureRandom());
        return encoder.encode(text);
    }

    private String getJWTToken(String username) {
        String secretKey = "tfg-pmh-poh";

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("HABITANTE");

        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return token; // TODO: Añadir "Bearer " delante de cada petición en el frontend
    }
}
