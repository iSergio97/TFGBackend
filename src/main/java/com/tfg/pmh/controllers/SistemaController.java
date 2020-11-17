package com.tfg.pmh.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sistema/administrador/")
public class SistemaController {

    @GetMapping("info")
    public List<Integer> systemInfo() {
        // Puede que interese crear un objeto Info, no persistente, para almacenar estas cosas
        return new ArrayList<>();
    }
}
