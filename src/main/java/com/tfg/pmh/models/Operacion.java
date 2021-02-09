package com.tfg.pmh.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.*;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Operacion {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Past
    private Date fechaOperacion;

    @NotBlank
    @Pattern(regexp = "^[ABM]$")
    private String tipo;

    @NotBlank
    private String subtipo;

    @Valid
    @ManyToOne
    private Habitante habitante;

    @Valid
    @ManyToOne
    private Vivienda viviendaOrigen;

    // Este campo puede estar nulo, si la operación es por modificación de datos personales
    @ManyToOne
    @Valid
    private Vivienda viviendaDestino;

    @ManyToOne
    @Valid
    private Solicitud solicitud;
}
