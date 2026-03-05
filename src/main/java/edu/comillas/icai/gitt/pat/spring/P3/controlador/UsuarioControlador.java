package edu.comillas.icai.gitt.pat.spring.P3.controlador;

import edu.comillas.icai.gitt.pat.spring.P3.entidad.Usuario;
import edu.comillas.icai.gitt.pat.spring.P3.servicio.ServicioUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsuarioControlador {
    @Autowired
    private ServicioUsuarios servicioUsuarios;

    @PostMapping("/api/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario creaUsuario(@RequestBody Usuario usuarioNuevo) {
        return servicioUsuarios.registrarUsuario(usuarioNuevo);
    }

    @GetMapping("/api/usuarios")
    @PreAuthorize("hasRole('ADMIN')")
    public Iterable<Usuario> getUsuarios() {
        return servicioUsuarios.getAllUsuarios();
    }

    @GetMapping("/api/usuarios/me")
    public Usuario getMiPerfil(Authentication authentication) {
        return servicioUsuarios.getMiPerfil(authentication);
    }

    @GetMapping("/api/usuarios/{idUsuario}")
    @PreAuthorize("hasRole('ADMIN')")
    public Usuario getUsuario(@PathVariable Long idUsuario) {
        return servicioUsuarios.getUsuario(idUsuario);
    }

    @PutMapping("/api/usuarios/me")
    public Usuario modificaUsuario(@RequestBody Usuario usuarioCambiado, Authentication authentication) {
        return servicioUsuarios.modificarMiCuenta(usuarioCambiado, authentication);
    }

    @DeleteMapping("/api/usuarios/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void borraUsuario(Authentication authentication) {
        servicioUsuarios.borrarMiCuenta(authentication);
    }
}