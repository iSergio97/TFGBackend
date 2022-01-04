package com.tfg.pmh.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.Valid;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hoja {

    private static final long serialVersionUID = 9178661439383356177L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Valid
    @ManyToOne
    private Numeracion numeracion;

    @Range(min = 0)
    private Integer hoja;
}
