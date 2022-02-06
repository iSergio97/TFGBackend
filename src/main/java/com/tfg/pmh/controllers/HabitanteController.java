package com.tfg.pmh.controllers;

import com.tfg.pmh.models.Habitante;
import com.tfg.pmh.models.Respuesta;
import com.tfg.pmh.services.HabitanteService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/habitante")
@CrossOrigin(origins = {"*"})
public class HabitanteController {

    @Autowired
    private HabitanteService habitanteService;

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

    @GetMapping("/convivientes/{hojaId}/{hoja}")
    public Respuesta convivientes(@PathVariable("hojaId") Long hojaId, @PathVariable("hoja") Integer hoja, @RequestParam("habId") Long habId) {
        Respuesta res = new Respuesta();
        try {
            // Buscamos la habitante que viven con este usuario (por hojaId y hoja)
            Collection<Habitante> convivientes = this.habitanteService.findConvivientes(hojaId, hoja, habId);
            res.setStatus(200);
            res.setObject(convivientes);
        } catch (Exception e) {
            res.setObject(null);
            res.setStatus(400);
        }

        return res;
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
                .setExpiration(new Date(System.currentTimeMillis() + 8*60*60*1000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return token; // TODO: Añadir "Bearer " delante de cada petición en el frontend
    }
}
