package edu.comillas.icai.gitt.pat.spring.P3.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long idCarrito;

    @ManyToOne
    public Usuario usuario;

    @Column(nullable = false)
    @PositiveOrZero(message = "El precio no puede ser negativo")
    public double precioTotal;
}
