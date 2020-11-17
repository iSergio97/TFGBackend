package com.tfg.pmh.models;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

//@Inheritance
@MappedSuperclass
@Getter
@Setter
public abstract class Persona {

	@NotBlank
	private String nombre;

	private String particulaPrimerApellido;

	@NotBlank
	private String primerApellido;

	private String particulaSegundoApellido;

	@NotBlank
	private String segundoApellido;

	//TODO: Plantear Regex
	@NotBlank
	private String email;

	@NotNull
	@Past
	private Date fechaNacimiento;

	@Valid
	@OneToOne(optional = false, cascade = CascadeType.REMOVE)
	private CuentaUsuario cuentaUsuario;
}
