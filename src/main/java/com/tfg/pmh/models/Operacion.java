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
public class Operacion {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Past
    private Date fechaOperacion;

    @NotBlank
    @Pattern(regexp = "^(A|B)$")
    private String tipo;

    @Valid
    @ManyToOne
    private Habitante habitante;

    @Valid
    @ManyToOne
    private Operacion origen;

    // Este campo es opcional, debería poder ser nulo
    @Valid
    @ManyToOne
    private Operacion destino;
}
