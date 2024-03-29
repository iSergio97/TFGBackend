package com.tfg.pmh.models;

import com.tfg.pmh.repositories.MunicipioRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Calle implements Serializable {

    private static final long serialVersionUID = 9178661439383356177L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Valid
    @ManyToOne
    private Municipio municipio;

    @NotBlank
    private String nombre;

    @NotBlank
    private String tipo;
}
