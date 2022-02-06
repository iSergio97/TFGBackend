package com.tfg.pmh.models;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
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
    @JsonIdentityReference(alwaysAsId = true)
    private Hoja Hoja;

    @Valid
    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Identificacion tarjetaIdentificacion;
    
    @Column(unique = true)
    private String identificacion;
}
