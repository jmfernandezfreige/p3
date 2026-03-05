package edu.comillas.icai.gitt.pat.spring.P3.controlador;

import edu.comillas.icai.gitt.pat.spring.P3.entidad.Carrito;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoCarrito;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoLineadeCarrito;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoUsuario;
import edu.comillas.icai.gitt.pat.spring.P3.servicio.ServicioCarritos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

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
        return this.repoCarrito.findAll();
    }

    @PostMapping("/api/carritos")
    @ResponseStatus(HttpStatus.CREATED)
    public Carrito creaCarrito(@RequestBody Carrito carritoNuevo, Principal principal) {
        return servicioCarritos.creaCarrito(carritoNuevo.idCarrito, principal.getName());
    }

    @GetMapping("/api/carritos/{idCarrito}")
    public Carrito getCarrito(@PathVariable Long idCarrito, Principal principal) {
        return servicioCarritos.getCarrito(idCarrito, principal.getName());
    }

    @DeleteMapping("/api/carritos/{idCarrito}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void borraCarrito(@PathVariable Long idCarrito, Principal principal) {
        servicioCarritos.borraCarrito(idCarrito, principal.getName());
    }

    @PutMapping("/api/carritos/{idCarrito}")
    public Carrito cambiaCarrito(@PathVariable Long idCarrito, @RequestBody Carrito carritoCambiado) {
        Carrito carritoExistente = this.repoCarrito.findById(idCarrito)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Carrito no encontrado"));

        carritoExistente.idUsuario = carritoCambiado.idUsuario;
        carritoExistente.precioTotal = carritoCambiado.precioTotal;

        this.repoCarrito.save(carritoExistente);
        return carritoExistente;
    }
}
