package edu.comillas.icai.gitt.pat.spring.P3.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long idCarrito;

    @Column(nullable = false, unique = true)
    public Long idUsuario;

    @Email
    @Column(nullable = false, unique = true)
    public String correoUsuario;

    @PositiveOrZero
    @Column(nullable = false, unique = true)
    public double precioTotal;
}
