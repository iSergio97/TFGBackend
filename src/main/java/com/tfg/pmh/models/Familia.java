package com.tfg.pmh.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Collection;

@Getter
@Setter
@Entity
public class Familia {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Valid
    @OneToOne(optional = false)
    private Habitante padre;

    @Valid
    @OneToOne(optional = false)
    private Habitante madre;

    @Valid
    @OneToMany
    private Collection<Habitante> hijos;
}
