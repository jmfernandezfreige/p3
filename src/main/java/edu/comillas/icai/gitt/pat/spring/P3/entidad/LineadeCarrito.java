package edu.comillas.icai.gitt.pat.spring.P3.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class LineadeCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long idLinea;

    @Column(nullable = false)
    public Long idCarrito;

    @Column(nullable = false)
    public Long idArticulo;

    @Column(nullable = false)
    public int unidades;

    @Column(nullable = false)
    public double precioUnitario;

    @Column(nullable = false)
    public double costeLinea;

}
