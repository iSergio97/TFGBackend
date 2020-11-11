package com.tfg.pmh.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
public class Vivienda {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String pais;

    @NotBlank
    private String provincia;

    @NotBlank
    private String municipio;

    @NotBlank
    private String distrito;

    @NotBlank
    private String seccion;

    @NotBlank
    private String calle;

    @NotBlank
    private Integer numero;

    @NotBlank
    private String puerta;

    @NotBlank
    private String planta;

}
