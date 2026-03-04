package edu.comillas.icai.gitt.pat.spring.P3.entidad;

import jakarta.persistence.*;

@Entity
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long idRol;

    @Column(nullable = false, unique = true) //Solo hay un rol con el nombre USER
    @Enumerated(EnumType.STRING)
    public NombreRol nombreRol;

    @Column(nullable = false)
    public String descripcion;

    public enum NombreRol {
        USER,
        ADMIN
    }
}
