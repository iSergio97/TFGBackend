package com.tfg.pmh.models;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
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

    private Date fecha;

    @Valid
    @ManyToOne
    private Habitante solicitante;

    private String justificacion;

    @Pattern(regexp = "^[AM]$")
    @NotBlank
    private String tipo;

    @Pattern(regexp = "^(ACR|AIM|MV|MD|MRE)$")
    @NotBlank
    private String subtipo;

    // El estado puede ser pendiente (P), aceptada (A), rechazada (R) y cancelada (C)
    @NotBlank
    @Pattern(regexp = "^[ACRP]$")
    private String estado;

    // Sólo para menores de edad que lo quieran añadir o para personas con tarjeta identificativa
    // @Pattern(regexp = "^(\\d{8}\\w)|(\\d{7}[XYZ])$")
    private String identificacion;

    @Valid
    @ManyToOne
    private Identificacion tipoIdentificacion;

    private String nombre;

    private String primerApellido;

    private String segundoApellido;

    @Valid
    @ManyToOne
    private Hoja hoja;

    @Past
    private Date fechaNacimiento;

    @Valid
    @OneToMany
    private List<Documento> documentos;
    
}