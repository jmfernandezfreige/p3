package edu.comillas.icai.gitt.pat.spring.P3.controlador;

import edu.comillas.icai.gitt.pat.spring.P3.entidad.Carrito;
import edu.comillas.icai.gitt.pat.spring.P3.entidad.Usuario;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoCarrito;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoLineadeCarrito;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@RestController
public class CarritoControlador {
    @Autowired
    private RepoCarrito repoCarrito;
    @Autowired
    private RepoLineadeCarrito repoLineadeCarrito;
    @Autowired
    private RepoUsuario repoUsuario;

    @GetMapping("/api/carritos")
    public Iterable<Carrito> getCarritos() {
        return this.repoCarrito.findAll();
    }
    @PostMapping("/api/carritos")
    @ResponseStatus(HttpStatus.CREATED)
    public Carrito creaCarrito(@RequestBody Carrito carritoNuevo) {
        this.repoCarrito.save(carritoNuevo);
        return carritoNuevo;
    }

    @GetMapping("/api/carritos/{idCarrito}")
    public Carrito getCarrito(@PathVariable Long idCarrito) {
        return this.repoCarrito.findById(id).orElse(null);
    }

    @DeleteMapping("/api/carritos/{idCarrito}")
    public void borraCarrito(@PathVariable Long idCarrito) {
        Carrito carrito = this.repoCarrito.findById(idCarrito).orElse(null);
        this.repoCarrito.delete(carrito);
    }

    @PutMapping("/api/carritos/{idCarrito}")
    public Carrito actualizaCarrito(@PathVariable Long idCarrito, @RequestBody Carrito carritoCambiado) {
        Carrito carritoExistente = this.repoCarrito.findById(idCarrito)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Carrito no encontrado"));

        carritoExistente.idUsuario = carritoCambiado.idUsuario;
        carritoExistente.precioTotal = carritoCambiado.precioTotal;

        this.repoCarrito.save(carritoExistente);
        return carritoExistente;
    }
}
