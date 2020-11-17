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

    // Sólo para menores de edad que lo quieran añadir
    @NotBlank
    @Pattern(regexp = "^\\d{8}$")
    private String dni;

    private String nombre;

    private String apellidos;

    @Valid
    @ManyToOne
    private Vivienda viviendaNueva;

    @Past
    private Date fechaNacimiento;
    
}