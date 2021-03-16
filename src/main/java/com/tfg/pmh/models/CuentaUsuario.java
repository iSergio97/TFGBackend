package com.tfg.pmh.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuentaUsuario implements Serializable {

    private static final long serialVersionUID = 9178661439383356177L;

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
