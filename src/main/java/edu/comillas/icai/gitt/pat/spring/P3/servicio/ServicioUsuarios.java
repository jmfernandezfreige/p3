package edu.comillas.icai.gitt.pat.spring.P3.servicio;

import edu.comillas.icai.gitt.pat.spring.P3.entidad.Rol;
import edu.comillas.icai.gitt.pat.spring.P3.entidad.Usuario;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoRol;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ServicioUsuarios {
    @Autowired
    RepoUsuario repoUsuario;
    @Autowired
    RepoRol repoRol;

    public Usuario getUsuarioLogueado(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        Usuario usuario = repoUsuario.findByEmail(authentication.getName());
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado en BD");
        }
        return usuario;
    }

    public Usuario registrarUsuario(Usuario usuarioNuevo) {
        if (repoUsuario.findByEmail(usuarioNuevo.email) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email ya está en uso");
        }

        if (usuarioNuevo.rol == null || usuarioNuevo.rol.nombreRol == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe especificar el rol en la petición");
        }

        String nombreRol = usuarioNuevo.rol.nombreRol;

        Rol rol = repoRol.findByNombreRol(nombreRol);
        if (rol == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El rol '" + nombreRol + "' no existe. Solo se permiten 'USER' o 'ADMIN'.");
        }

        usuarioNuevo.rol = rol;

        return repoUsuario.save(usuarioNuevo);
    }

    //Para ADMIN
    public Usuario getUsuario(Long id) {
        return repoUsuario.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "El usuario con ID " + id + " no existe"));
    }

    public Usuario getMiPerfil(Authentication authentication) {
        return getUsuarioLogueado(authentication);
    }

    public Usuario modificarMiCuenta(Usuario usuarioCambiado, Authentication authentication) {
        Usuario usuarioExistente = getUsuarioLogueado(authentication);

        usuarioExistente.email = usuarioCambiado.email;
        usuarioExistente.contraseña = usuarioCambiado.contraseña;

        return repoUsuario.save(usuarioExistente);
    }

    public void borrarMiCuenta(Authentication authentication) {
        Usuario usuarioExistente = getUsuarioLogueado(authentication);
        repoUsuario.delete(usuarioExistente);
    }

    public Iterable<Usuario> getAllUsuarios() {
        return repoUsuario.findAll();
    }
}