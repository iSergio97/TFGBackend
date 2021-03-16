package com.tfg.pmh.models;

import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Administrador extends Persona {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
}
