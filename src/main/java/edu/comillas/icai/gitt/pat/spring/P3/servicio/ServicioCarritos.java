package edu.comillas.icai.gitt.pat.spring.P3.servicio;

import edu.comillas.icai.gitt.pat.spring.P3.entidad.Carrito;
import edu.comillas.icai.gitt.pat.spring.P3.entidad.Usuario;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoCarrito;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoLineadeCarrito;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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


    private Usuario getUsuarioLogueado(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Usuario no autenticado");
        }

        Usuario usuario = repoUsuario.findByEmail(authentication.getName());
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado en BD");
        }
        return usuario;
    }

    public Carrito creaCarrito(Carrito carritoNuevo, Authentication authentication) {
        Usuario usuario = getUsuarioLogueado(authentication);

        carritoNuevo.usuario.id = usuario.id;
        return repoCarrito.save(carritoNuevo);
    }

    public Carrito getCarrito(Long idCarrito, Authentication authentication) {
        Usuario usuario = getUsuarioLogueado(authentication);

        Carrito carrito = repoCarrito.findById(idCarrito)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "El carrito no existe"));

        if (!carrito.usuario.id.equals(usuario.id)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Permiso denegado para ver este carrito"
            );
        }

        return carrito;
    }

    public Carrito cambiaCarrito(Long idCarrito, Carrito carritoCambiado, Authentication authentication) {
        Carrito carritoExistente = getCarrito(idCarrito, authentication);

        carritoExistente.precioTotal = carritoCambiado.precioTotal;

        return repoCarrito.save(carritoExistente);
    }

    public void borraCarrito(Long idCarrito, Authentication authentication) {
        Carrito carritoExistente = getCarrito(idCarrito, authentication);
        repoCarrito.delete(carritoExistente);
    }
}
