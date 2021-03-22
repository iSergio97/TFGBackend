package com.tfg.pmh.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Solicitud implements Serializable {

    private static final long serialVersionUID = 9178661439383356177L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Past
    private Date fecha;

    @Valid
    @ManyToOne
    private Habitante solicitante;

    @ManyToOne
    private Habitante solicitaPor; // TODO: TIRAR COLUMNA EN LA SIGUIENTE REPOBLACIÓN

    private String justificacion;

    @Pattern(regexp = "^[AM]$")
    @NotBlank
    private String tipo;

    @Pattern(regexp = "^(ACR|AIM|MV|MD|MRE)$")
    @NotBlank
    private String subtipo;

    // El estado puede ser pendiente (P), aceptada (A), rechazada (R) y cancelada (C)
    @NotBlank
    @Pattern(regexp = "^[ACRP]$") // TODO: REPOBLAR TODO PARA ELIMINAR LAS OPCIONES DE PENDIENTE O CANCELADO, YA QUE UNA OPERACIÓN SE PRODUCE A RAÍZ DE UNA SOLICITUD
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

    private String pais;

    private String provincia;

    private String municipio;

    private String calle;

    private Integer numero;


    @Past
    private Date fechaNacimiento;

    @Valid
    @OneToMany
    private List<Documento> documentos;
    
}