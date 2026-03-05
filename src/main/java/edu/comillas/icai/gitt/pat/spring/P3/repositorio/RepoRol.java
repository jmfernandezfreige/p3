package edu.comillas.icai.gitt.pat.spring.P3.repositorio;

import edu.comillas.icai.gitt.pat.spring.P3.entidad.Rol;
import org.springframework.data.repository.CrudRepository;

public interface RepoRol extends CrudRepository<Rol, Long> {
    Rol findByNombreRol(String nombreRol);
}
