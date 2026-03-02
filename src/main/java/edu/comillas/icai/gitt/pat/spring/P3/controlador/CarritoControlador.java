package edu.comillas.icai.gitt.pat.spring.P3.controlador;

import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoCarrito;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoLineadeCarrito;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarritoControlador {
    @Autowired
    RepoCarrito repoCarrito;
    RepoLineadeCarrito repoLineadeCarrito;
    RepoUsuario repoUsuario;

    @PostMapping

}
