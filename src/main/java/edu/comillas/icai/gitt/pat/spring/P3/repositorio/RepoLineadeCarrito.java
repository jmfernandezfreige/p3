package edu.comillas.icai.gitt.pat.spring.P3.repositorio;

import edu.comillas.icai.gitt.pat.spring.P3.entidad.LineadeCarrito;

public interface RepoLineadeCarrito extends CrudRepository<LineaCarrito, Long> {
    LineadeCarrito findByIdArticulo(Long idArticulo);
}
