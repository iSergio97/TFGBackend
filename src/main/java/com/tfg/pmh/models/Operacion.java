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
    @Pattern(regexp = "^(A|B|M)$")
    private String tipo;

    @Valid
    @ManyToOne
    private Habitante habitante;

    @Valid
    @ManyToOne
    private Vivienda viviendaOrigen;

    // Este campo coincide con el campo viviendaOrigen si se mantiene en la misma vivienda
    @ManyToOne
    @Valid
    private Vivienda viviendaDestino;
}
