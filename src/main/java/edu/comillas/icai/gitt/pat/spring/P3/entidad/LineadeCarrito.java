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

    @ManyToOne(optional = false)
    public Carrito carrito;

    @Column(nullable = false)
    public Long idArticulo;

    @Column(nullable = false)
    @Positive(message = "No puede haber un artículo con 0 unidades")
    public int unidades;

    @Column(nullable = false)
    @PositiveOrZero(message = "El precio unitario no puede ser negativo")
    public double precioUnitario;

    @Column(nullable = false)
    @PositiveOrZero(message = "El coste de la línea no puede ser negativo")
    public double costeLinea;

}
