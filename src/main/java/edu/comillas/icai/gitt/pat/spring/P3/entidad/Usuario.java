package edu.comillas.icai.gitt.pat.spring.P3.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "El email es obligatorio")
    public String email;

    @Column(nullable = false)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(message = "La contraseña debe tener al menos 6 caracteres")
    public String contraseña;

    @ManyToOne(optional = false)
    public Rol rol;
}
