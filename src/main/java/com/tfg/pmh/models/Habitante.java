package com.tfg.pmh.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Habitante extends Persona {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Pattern(regexp = "^[HM]$")
    private String sexo;

    @NotBlank
    private String nacionalidad;

    @Valid
    @ManyToOne
    private Vivienda Vivienda;

    @Valid
    @ManyToOne
    private Identificador tarjetaIdentificacion;

    // @NotBlank
    @Column(unique = true)
    private String identificacion;
}
