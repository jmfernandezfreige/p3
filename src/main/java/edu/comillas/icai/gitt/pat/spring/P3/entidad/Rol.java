package edu.comillas.icai.gitt.pat.spring.P3.entidad;

import jakarta.persistence.*;

@Entity
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long idRol;

    @Column(nullable = false, unique = true) //Solo hay un rol con el nombre USER
    public String nombreRol;

    @Column(nullable = true)
    public String descripcion;
}
