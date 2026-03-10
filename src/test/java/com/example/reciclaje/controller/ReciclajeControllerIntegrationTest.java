package com.example.reciclaje.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.reciclaje.entidades.Material;
import com.example.reciclaje.entidades.Rol;
import com.example.reciclaje.entidades.Usuario;
import com.example.reciclaje.entidades.UsuarioRol;
import com.example.reciclaje.repositorio.MaterialRepositorio;
import com.example.reciclaje.repositorio.RolRepositorio;
import com.example.reciclaje.repositorio.UsuarioRepositorio;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driverClassName=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=update"
})
@AutoConfigureMockMvc
public class ReciclajeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private MaterialRepositorio materialRepositorio;

    @Autowired
    private RolRepositorio rolRepositorio;

    @BeforeEach
    void setup() {
        if (rolRepositorio.findByNombre("USER").isEmpty()) {
            rolRepositorio.save(new Rol("USER", "Usuario regular"));
        }
        
        if (usuarioRepositorio.findByEmail("authuser@test.com").isEmpty()) {
            Usuario u = new Usuario();
            u.setEmail("authuser@test.com");
            u.setNombre("Auth User");
            u.setPassword("pass");
            u.setPuntos(0);
            u.setDireccion("123 Street");
            
            Rol rolUser = rolRepositorio.findByNombre("USER").get();
            UsuarioRol ur = new UsuarioRol(u, rolUser);
            u.getUsuarioRoles().add(ur);

            usuarioRepositorio.save(u);
        }
    }

    @Test
    @WithMockUser(username = "authuser@test.com", roles = {"USER"})
    void testRegistrarReciclajeExitoso() throws Exception {
        Material material = new Material();
        material.setNombre("Plástico PET");
        material.setReciclable(true);
        material.setPuntosPorUnidad(10);
        Material savedMaterial = materialRepositorio.save(material);

        mockMvc.perform(post("/api/reciclajes/registrar")
                .param("materialId", savedMaterial.getId().toString())
                .param("cantidad", "5")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isCreated());
    }
}
