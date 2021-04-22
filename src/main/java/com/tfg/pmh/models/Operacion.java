package com.tfg.pmh.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Operacion implements Serializable {

    private static final long serialVersionUID = 9178661439383356177L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Past
    private Date fechaOperacion;

    @NotBlank
    @Pattern(regexp = "^[ABM]$")
    private String tipo;

    @NotBlank
    @Pattern(regexp = "^(ACR|AIM|BCD|MV|MD|MRE)$")
    private String subtipo;

    @Valid
    @ManyToOne
    private Habitante habitante;

    @ManyToOne
    @Valid
    private Solicitud solicitud;

    /*
        Propiedades duplicadas de solicitud para poder agilizar el proceso
        de mostrar los cambios que se han aplicado en esta solicitud
    */


    private String nombre;

    private String primerApellido;

    private String segundoApellido;

    @Pattern(regexp = "^(\\d{8}\\w)|(\\d{7}[XYZ])$")
    private String identificacion;

    @Valid
    @ManyToOne
    private Identificacion tipoIdentificacion;

    @Past
    private Date fechaNacimiento;

    @Valid
    @ManyToOne
    private Vivienda vivienda;


}
