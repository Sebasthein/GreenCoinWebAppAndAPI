package com.example.reciclaje.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.reciclaje.entidades.Rol;
import com.example.reciclaje.repositorio.RolRepositorio;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driverClassName=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=update"
})
@AutoConfigureMockMvc(addFilters = false)
public class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RolRepositorio rolRepositorio;

    @BeforeEach
    void setup() {
        if (rolRepositorio.findByNombre("USER").isEmpty()) {
            rolRepositorio.save(new Rol("USER", "Usuario regular"));
        }
    }

    @Test
    void testRegistrarUsuarioApi() throws Exception {
        String uniqueEmail = "test" + System.currentTimeMillis() + "@integration.com";
        String jsonPayload = "{" +
            "\"nombre\": \"Test User\"," +
            "\"email\": \"" + uniqueEmail + "\"," +
            "\"password\": \"password123\"," +
            "\"direccion\": \"Street 123\"," +
            "\"telefono\": \"5551234567\"" +
        "}";

        mockMvc.perform(post("/api/usuarios/api/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isCreated());
    }
}
