package com.tfg.pmh.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Past;
import java.util.Date;

@Entity
@Getter
@Setter
public class Solicitud {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Past
    private Date fecha;

    @Valid
    @ManyToOne
    private Habitante solicitante;

    @ManyToOne
    private Habitante solicitaPor; // Campo añadido para la modificación de datos de menores por sus padres
    
}