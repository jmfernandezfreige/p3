package edu.comillas.icai.gitt.pat.spring.P3.servicio;

import edu.comillas.icai.gitt.pat.spring.P3.entidad.Carrito;
import edu.comillas.icai.gitt.pat.spring.P3.entidad.LineadeCarrito;
import edu.comillas.icai.gitt.pat.spring.P3.entidad.Usuario;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoCarrito;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoLineadeCarrito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ServicioCarritos {
    @Autowired
    RepoCarrito repoCarrito;
    @Autowired
    RepoLineadeCarrito repoLineadeCarrito;
    @Autowired
    ServicioUsuarios servicioUsuarios;


    public Iterable<Carrito> getAllCarritos() {
        return repoCarrito.findAll();
    }
    public Carrito creaCarrito(Carrito carritoNuevo, Authentication authentication) {
        Usuario usuario = servicioUsuarios.getUsuarioLogueado(authentication);

        carritoNuevo.usuario = usuario;
        return repoCarrito.save(carritoNuevo);
    }

    public Carrito getCarrito(Long idCarrito, Authentication authentication) {
        Usuario usuario = servicioUsuarios.getUsuarioLogueado(authentication);

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

    //LÍNEAS DE CARRITO
    public LineadeCarrito anadirLinea(Long idCarrito, LineadeCarrito nuevaLinea, Authentication authentication) {
        Carrito carrito = getCarrito(idCarrito, authentication);

        LineadeCarrito lineaExistente = repoLineadeCarrito.findByCarritoAndIdArticulo(carrito, nuevaLinea.idArticulo);

        //Si ya existe una línea para ese artículo
        if (lineaExistente != null) {
            lineaExistente.unidades += nuevaLinea.unidades;
            lineaExistente.costeLinea = lineaExistente.unidades * lineaExistente.precioUnitario;

            //Se actualiza el precio
            carrito.precioTotal += (nuevaLinea.unidades * nuevaLinea.precioUnitario);
            repoCarrito.save(carrito); // Guardamos carrito actualizado

            return repoLineadeCarrito.save(lineaExistente);

        } else {
            // Si es un artículo nuevo en el carrito
            nuevaLinea.carrito = carrito; // Lo vinculamos a este carrito
            nuevaLinea.costeLinea = nuevaLinea.unidades * nuevaLinea.precioUnitario; // Calculamos el coste

            // Actualizamos el total del carrito
            carrito.precioTotal += nuevaLinea.costeLinea;
            repoCarrito.save(carrito); // Guardamos carrito actualizado

            return repoLineadeCarrito.save(nuevaLinea);
        }
    }

    public Iterable<LineadeCarrito> getLineas(Long idCarrito, Authentication authentication) {
        Carrito carrito = getCarrito(idCarrito, authentication);
        return repoLineadeCarrito.findByCarrito(carrito);
    }

    public void borrarLinea(Long idCarrito, Long idLinea, Authentication authentication) {
        Carrito carrito = getCarrito(idCarrito, authentication);

        LineadeCarrito linea = repoLineadeCarrito.findById(idLinea)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Línea no encontrada"));

        //Si no pertenecen al mismo carrito
        if (!linea.carrito.idCarrito.equals(carrito.idCarrito)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esa línea no pertenece a tu carrito");
        }

        //Se actualiza el precio del carrito
        carrito.precioTotal -= linea.costeLinea;
        repoCarrito.save(carrito);

        repoLineadeCarrito.delete(linea);
    }
}
