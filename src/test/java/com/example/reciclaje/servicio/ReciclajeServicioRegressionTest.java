package com.example.reciclaje.servicio;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.reciclaje.entidades.Reciclaje;
import com.example.reciclaje.repositorio.ReciclajeRepositorio;

@ExtendWith(MockitoExtension.class)
public class ReciclajeServicioRegressionTest {

    @Mock
    private ReciclajeRepositorio reciclajeRepository;

    @InjectMocks
    private ReciclajeServicio reciclajeServicio;

    @Test
    void testValidarReciclajeLanzaExcepcionSiYaEstaValidado() {
        // Objeto de prueba: un reciclaje que ya tiene validado=true
        Reciclaje reciclajeValidado = new Reciclaje();
        reciclajeValidado.setId(1L);
        reciclajeValidado.setValidado(true);

        when(reciclajeRepository.findById(1L)).thenReturn(Optional.of(reciclajeValidado));

        // Prueba de regresión: asegurar que el servicio proteja contra re-validar algo
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            reciclajeServicio.validarReciclaje(1L);
        });

        assertEquals("El reciclaje ya fue validado", exception.getMessage());
        verify(reciclajeRepository, never()).save(reciclajeValidado);
    }
}
