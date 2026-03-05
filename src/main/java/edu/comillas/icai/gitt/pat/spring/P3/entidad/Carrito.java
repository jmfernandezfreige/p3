package edu.comillas.icai.gitt.pat.spring.P3.entidad;

import jakarta.persistence.*;

@Entity
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long idCarrito;

    @ManyToOne
    public Usuario usuario;

    @Column(nullable = false)
    public double precioTotal;
}
