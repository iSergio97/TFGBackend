package com.tfg.pmh.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Provincia implements Serializable {

    private static final long serialVersionUID = 9178661439383356177L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Valid
    @ManyToOne
    private Pais pais;
}
