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
	private String name;
	
	@NotBlank
	private String surname;

	//TODO: Plantear Regex
	@NotBlank
	private String email;

	@NotNull
	@Past
	private Date fechaNacimiento;

	@NotBlank
	@Pattern(regexp = "^(H|M)$")
	private String sexo;

	@Valid
	@OneToOne(optional = false, cascade = CascadeType.REMOVE)
	private CuentaUsuario cuentaUsuario;
}
