package com.tfg.pmh.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Administrador extends Persona {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
}
