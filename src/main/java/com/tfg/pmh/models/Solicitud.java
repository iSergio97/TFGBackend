package com.tfg.pmh.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Solicitud {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Past
    private Date fecha;

    @Valid
    @ManyToOne
    private Habitante solicitante;

    @ManyToOne
    private Habitante solicitaPor; // Campo añadido para la modificación de datos de menores por sus padres

    private String justificacion;

    @Pattern(regexp = "^[ABM]$")
    @NotBlank
    private String tipo;

    @Pattern(regexp = "^(AN|AO|ACR|BD|BCD|MV|MD)$")
    @NotBlank
    private String subtipo;

    // El estado puede ser pendiente (P), aceptada (A), rechazada (R) y cancelada (C)
    @NotBlank
    @Pattern(regexp = "^[PARC]$")
    private String estado;

    // Sólo para menores de edad que lo quieran añadir o para personas con tarjeta identificativa
    @Pattern(regexp = "^(\\d{8}\\w)|(\\d{7}[XYZ])$")
    private String identificacion;

    @NotBlank
    private String nombre;

    @NotBlank
    private String primerApellido;

    @NotBlank
    private String segundoApellido;

    @ManyToOne
    private Vivienda viviendaNueva;

    @Past
    private Date fechaNacimiento;
    
}