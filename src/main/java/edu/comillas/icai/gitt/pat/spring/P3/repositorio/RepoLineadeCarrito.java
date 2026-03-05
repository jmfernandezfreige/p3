package edu.comillas.icai.gitt.pat.spring.P3.repositorio;

import edu.comillas.icai.gitt.pat.spring.P3.entidad.Carrito;
import edu.comillas.icai.gitt.pat.spring.P3.entidad.LineadeCarrito;
import org.springframework.data.repository.CrudRepository;

public interface RepoLineadeCarrito extends CrudRepository<LineadeCarrito, Long> {
    LineadeCarrito findByCarritoAndIdArticulo(Carrito carrito, Long idArticulo);
    Iterable<LineadeCarrito> findByCarrito(Carrito carrito);
}
