package edu.comillas.icai.gitt.pat.spring.P3.seguridad;

import edu.comillas.icai.gitt.pat.spring.P3.entidad.Usuario;
import edu.comillas.icai.gitt.pat.spring.P3.repositorio.RepoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.server.ResponseStatusException;

@Configuration
@EnableMethodSecurity
public class ConfiguracionSeguridad {
    //Se conecta el repo de usuarios
    @Autowired
    RepoUsuario repoUsuario;

    //Cadena de filtros
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //Se dehabilita CRSF porque se usa Postman no HTML (Api Rest sin estado)
        http.csrf(csrf -> csrf.disable());

        //Se indica que no se creen sesiones en memoria
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //Permisos de las rutas
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/register").permitAll() // Para el registro se permiten TODAS
                .requestMatchers("/api/auth/login").permitAll() // Para el login se permiten TODAS
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated() // Cualquier otra ruta requiere estar autenticado (logueado)
        );

        //LOGIN
        http.formLogin(form -> form
                .loginProcessingUrl("/api/auth/login") //Definimos la URL
                //Se indica qué se responde si hay éxito o fracaso en el Login
                .successHandler((req, res, auth) -> res.setStatus(HttpStatus.OK.value()))
                .failureHandler((req, res, ex) -> res.setStatus(HttpStatus.UNAUTHORIZED.value()))
        );

        //LOGOUT
        http.logout(logout -> logout
                .logoutUrl("/api/auth/logout") //Definimos la URL
                //Si se hace logout con éxito
                .logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpStatus.NO_CONTENT.value()))
        );

        //Configuración básica
        http.httpBasic(httpBasic -> {});

        return http.build();
    }

    //Configuración Usuarios (Definir Accesos)
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            //Se busca el usuario por email
            Usuario u =repoUsuario.findByEmail(username);

            //En caso de que no exista
            if (u == null) {
                throw new UsernameNotFoundException("Usuario no encontrado");
            }

            //En caso de que sí que exista
            return User.withDefaultPasswordEncoder()
                    .username(u.email)
                    .password(u.contraseña)
                    .roles(u.rol.nombreRol)
                    .build();

        };
    }


}
