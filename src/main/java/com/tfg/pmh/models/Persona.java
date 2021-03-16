package com.tfg.pmh.models;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.io.Serializable;
import java.util.Date;

//@Inheritance
@MappedSuperclass
@Data
public abstract class Persona implements Serializable {

	private static final long serialVersionUID = 9178661439383356177L;

	@NotBlank
	private String nombre;

	@NotBlank
	private String primerApellido;

	@NotBlank
	private String segundoApellido;

	//TODO: Plantear Regex
	@NotBlank
	private String email;

	@NotNull
	@Past
	private Date fechaNacimiento;

	@URL
	@NotBlank
	private String image;

	@Valid
	@OneToOne(optional = false, cascade = CascadeType.REMOVE)
	private CuentaUsuario cuentaUsuario;
}
