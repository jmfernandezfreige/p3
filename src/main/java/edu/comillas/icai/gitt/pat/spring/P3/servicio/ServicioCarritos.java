package edu.comillas.icai.gitt.pat.spring.P3.servicio;

import edu.comillas.icai.gitt.pat.spring.P3.entidad.Carrito;
import edu.comillas.icai.gitt.pat.spring.P3.entidad.Usuario;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoCarrito;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoLineadeCarrito;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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


//    private String getEmailUsuarioAutenticado() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new ResponseStatusException(
//                    HttpStatus.UNAUTHORIZED,
//                    "No autenticado");
//        }
//
//        return authentication.getName();
//    }

    private Usuario getUsuarioLogueado(String emailUsuario) {
        Usuario usuario = repoUsuario.findByEmail(emailUsuario);
        if (usuario == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuario no encontrado");
        }
        return usuario;
    }

    public Carrito creaCarrito(Carrito carritoNuevo, String emailUsuario) {
        Usuario usuario = getUsuarioLogueado(emailUsuario);

        carritoNuevo.idUsuario = usuario.id;
        return repoCarrito.save(carritoNuevo);
    }

    public Carrito getCarrito(Long Id, Usuario usuario) {
        Usuario usuario = getUsuarioLogueado(emailUsuario);

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

    public Carrito cambiaCarrito(Long idCarrito, Carrito carritoCambiado, String emailUsuario) {
        Carrito carritoExistente = getCarrito(idCarrito, emailUsuario);

        carritoExistente = carritoCambiado

        return repoCarrito.save(carritoExistente);
    }

    public void borraCarrito(Long idCarrito, String emailUsuario) {
        Carrito carritoExistente = getCarrito(idCarrito, emailUsuario);
        repoCarrito.delete(carrito);
    }
}
