package edu.comillas.icai.gitt.pat.spring.P3.servicio;

import edu.comillas.icai.gitt.pat.spring.P3.entidad.Carrito;
import edu.comillas.icai.gitt.pat.spring.P3.entidad.Usuario;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoCarrito;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoLineadeCarrito;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ServicioCarritos {
    @Autowired
    RepoUsuario repoUsuario;
    @Autowired
    RepoCarrito repoCarrito;
    @Autowired
    RepoLineadeCarrito repoLineadeCarrito;

    public Usuario autentica(String credenciales) { throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); }
    public Carrito creaCarrito(Carrito carritoNuevo, Usuario usuario) { return null; }

    public Carrito getCarrito(Long Id, Usuario usuario) {
        Carrito carrito = repoCarrito.findById(idCarrito)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El carrito no existe"));

        if (!carrito.idUsuario.equals(usuarioActual.id)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Permiso denegado para ver este carrito");
        }

        return carrito;
    }

    public Carrito cambiaCarrito(Carrito carrito, Usuario usuario) { return null; }
    public void    borraCarrito(Carrito carrito, Usuario usuario) { }
}
