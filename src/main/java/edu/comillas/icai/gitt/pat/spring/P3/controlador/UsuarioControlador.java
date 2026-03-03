package edu.comillas.icai.gitt.pat.spring.P3.controlador;

import edu.comillas.icai.gitt.pat.spring.P3.entidad.Usuario;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoCarrito;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoLineadeCarrito;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UsuarioControlador {
    @Autowired
    private RepoCarrito repoCarrito;
    @Autowired
    private RepoLineadeCarrito repoLineadeCarrito;
    @Autowired
    private RepoUsuario repoUsuario;

    @PostMapping("/api/usuarios")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario creaUsuario(@RequestBody Usuario usuarioNuevo) {
        this.repoUsuario.save(usuarioNuevo);
        return usuarioNuevo;
    }

    @GetMapping("/api/usuarios")
    public Iterable<Usuario> getUsuarios() {
        return this.repoUsuario.findAll();
    }

    @GetMapping("/api/usuarios/{idUsuario}")
    public Usuario getUsuario(@PathVariable Long idUsuario) {
        return this.repoUsuario.findById(idUsuario).orElse(null);
    }

    @DeleteMapping("/api/usuarios/{idUsuario}")
    public void borraUsuario(@PathVariable Long idUsuario) {
        Usuario usuario = this.repoUsuario.findById(idUsuario).orElse(null);
        this.repoUsuario.delete(usuario);
    }

    @PutMapping("/api/usuarios/{idUsuario")
    public Usuario modificaUsuario(@PathVariable Long idUsuario,
                                   @RequestBody Usuario usuarioCambiado) {
        Usuario usuarioExistente = this.repoUsuario.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "El usuario con id " + idUsuario + " no existe")
                );
        usuarioExistente.email = usuarioCambiado.email;
        usuarioExistente.credenciales = usuarioCambiado.credenciales;

        this.repoUsuario.save(usuarioExistente);
        return usuarioExistente;
    }
}