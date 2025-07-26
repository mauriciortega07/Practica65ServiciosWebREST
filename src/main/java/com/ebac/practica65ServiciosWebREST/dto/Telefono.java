package com.ebac.practica65ServiciosWebREST.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
public class Telefono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTelefono;

    private String tipo;
    private int lada;
    private String numero;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;
}
