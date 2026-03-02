package edu.comillas.icai.gitt.pat.spring.P3.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class LineadeCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long idCarrito;

    @Column(nullable = false, unique = true)
    public Long idArticulo;

    @Positive
    @Column(nullable = false, unique = true)
    public int unidades;

    @PositiveOrZero
    @Column(nullable = false, unique = true)
    public double precioUnitario;

    @PositiveOrZero
    @Column(nullable = false, unique = true)
    public double precioTotal;

    @PositiveOrZero
    @Column(nullable = false, unique = true)
    public double costeLinea;

}
