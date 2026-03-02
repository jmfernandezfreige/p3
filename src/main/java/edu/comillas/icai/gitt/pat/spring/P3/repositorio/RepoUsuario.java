package edu.comillas.icai.gitt.pat.spring.P3.repositorio;

import edu.comillas.icai.gitt.pat.spring.P3.entidad.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface RepoUsuario extends CrudRepository<Usuario, Long> {
    Usuario findById(Long id);
    Usuario findByEmail(String email);
}
