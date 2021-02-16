package com.tfg.pmh.forms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tfg.pmh.models.Habitante;
import com.tfg.pmh.models.Vivienda;
import lombok.*;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class SolicitudForm {

    @NotNull
    private Long solicitanteID;

    @NotNull
    private Long solicitaPorID; // Campo añadido para la modificación de datos de menores por sus padres
    
    @NotBlank
    private String identificacion;

    @NotBlank
    private String nombre;

    @NotBlank
    private String primerApellido;

    @NotBlank
    private String segundoApellido;

    @NotBlank
    private String pais;

    @NotBlank
    private String provincia;

    @NotBlank
    private String municipio;

    @NotBlank
    private String calle;

    @NotBlank
    private Integer numero;

    @Past
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date fechaNacimiento;

    // Campos para el alta de usuario
    private String username;

    private String password;
}
