package com.tfg.pmh.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Getter
@Setter
public class Habitante extends Persona {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
}
