package com.tfg.pmh.models;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Numeracion implements Serializable {

    private static final long serialVersionUID = 9178661439383356177L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Valid
    @ManyToOne
    private Calle calle;

    @Range(min = 0)
    @NotNull
    private Integer numero;

    @Range(min = 1)
    @NotNull
    private Integer escalera = 1;

    @Range(min = -1) // -1 sótano, 0 planta baja
    @NotNull
    private Integer planta = 0;

    @NotBlank
    private String puerta = "01";

    @Column(unique = true)
    @NotBlank
    @Length(min = 14, max = 20)
    private String referenciaCatastral;

    // @NotNull
    private Double lat;

    // @NotNull
    private Double lng;
}
