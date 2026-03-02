package edu.comillas.icai.gitt.pat.spring.P3.repositorio;

import edu.comillas.icai.gitt.pat.spring.P3.entidad.Carrito;
import org.springframework.data.repository.CrudRepository;

public interface RepoCarrito extends CrudRepository<Carrito, Long> {
    Carrito findByIdCarrito(Long idCarrito);
}
