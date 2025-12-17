package com.example.reciclaje.servicio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.reciclaje.entidades.Nivel;
import com.example.reciclaje.entidades.Usuario;
import com.example.reciclaje.repositorio.NivelRepositorio;
import com.example.reciclaje.repositorio.RolRepositorio;
import com.example.reciclaje.repositorio.UsuarioRepositorio;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepositorio usuarioRepositorio;
    private final RolRepositorio rolRepositorio;
    private final NivelRepositorio nivelRepositorio;
    private final UsuarioServicio usuarioServicio;

    @Override
    public void run(String... args) throws Exception {
        crearNivelesPorDefecto();
        crearUsuarioAdminPorDefecto();
    }

    private void crearUsuarioAdminPorDefecto() {
        String adminEmail = "admin";
        if (usuarioRepositorio.existsByEmail(adminEmail)) {
            log.info("Usuario admin por defecto ya existe");
            return;
        }

        log.info("Creando usuario admin por defecto");

        Usuario admin = new Usuario();
        admin.setNombre("Super Admin");
        admin.setEmail(adminEmail);
        admin.setPassword("admin"); // Será encriptada en registrarUsuarioConRol
        admin.setPuntos(0);
        admin.setDireccion("Sistema");
        admin.setTelefono("0000000000");

        // Usar el método de registro que maneja la encriptación, nivel inicial y rol ADMIN
        try {
            usuarioServicio.registrarUsuarioConRol(admin);
            log.info("Usuario admin por defecto creado exitosamente");
        } catch (Exception e) {
            log.error("Error al crear usuario admin por defecto", e);
        }
    }

    private void crearNivelesPorDefecto() {
        if (nivelRepositorio.count() > 0) {
            log.info("Niveles por defecto ya existen");
            return;
        }

        log.info("Creando niveles por defecto");

        Nivel[] niveles = {
            crearNivel("Novato", 0),
            crearNivel("Principiante", 50),
            crearNivel("Intermedio", 150),
            crearNivel("Avanzado", 300),
            crearNivel("Experto", 500),
            crearNivel("Maestro", 750),
            crearNivel("Leyenda", 1000)
        };

        for (Nivel nivel : niveles) {
            nivelRepositorio.save(nivel);
        }

        log.info("Niveles por defecto creados exitosamente");
    }

    private Nivel crearNivel(String nombre, int puntosRequeridos) {
        Nivel nivel = new Nivel();
        nivel.setNombre(nombre);
        nivel.setPuntosRequeridos(puntosRequeridos);
        return nivel;
    }
}