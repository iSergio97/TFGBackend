package com.tfg.pmh.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CuentaUsuario {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String salt;

    @NotBlank
    @Pattern(regexp = "^(ADMIN|HABITANTE)$")
    private String rol;
}
