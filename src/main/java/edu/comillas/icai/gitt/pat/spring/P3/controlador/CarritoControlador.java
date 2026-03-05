package edu.comillas.icai.gitt.pat.spring.P3.controlador;

import edu.comillas.icai.gitt.pat.spring.P3.entidad.Carrito;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoCarrito;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoLineadeCarrito;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoUsuario;
import edu.comillas.icai.gitt.pat.spring.P3.servicio.ServicioCarritos;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
public class CarritoControlador {
    @Autowired
    private RepoCarrito repoCarrito;
    @Autowired
    private RepoLineadeCarrito repoLineadeCarrito;
    @Autowired
    private ServicioCarritos servicioCarritos;

    @GetMapping("/api/carritos")
    @PreAuthorize("hasRole('ADMIN')")
    public Iterable<Carrito> getCarritos() {
        return servicioCarritos.getAllCarritos();
    }

    @PostMapping("/api/carritos")
    @ResponseStatus(HttpStatus.CREATED)
    public Carrito creaCarrito(@Valid @RequestBody Carrito carritoNuevo, Authentication authentication) {
        return servicioCarritos.creaCarrito(carritoNuevo, authentication);
    }

    @GetMapping("/api/carritos/{idCarrito}")
    public Carrito getCarrito(@PathVariable Long idCarrito, Authentication authentication) {
        return servicioCarritos.getCarrito(idCarrito, authentication);
    }

    @DeleteMapping("/api/carritos/{idCarrito}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void borraCarrito(@PathVariable Long idCarrito, Authentication authentication) {
        servicioCarritos.borraCarrito(idCarrito, authentication);
    }

    @PutMapping("/api/carritos/{idCarrito}")
    public Carrito cambiaCarrito(@PathVariable Long idCarrito, @Valid @RequestBody Carrito carritoCambiado, Authentication authentication) {
        return servicioCarritos.cambiaCarrito(idCarrito, carritoCambiado, authentication);
    }
}
