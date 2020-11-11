package com.tfg.pmh.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
public class CuentaUsuario {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    @Column(unique = true) //TODO: Plantear si esto es bueno en cuanto a eficiencia de cálculo
    private String salt;
}
