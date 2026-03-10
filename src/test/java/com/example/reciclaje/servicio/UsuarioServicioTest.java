package com.example.reciclaje.servicio;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.reciclaje.entidades.Usuario;
import com.example.reciclaje.repositorio.LogroRepositorio;
import com.example.reciclaje.repositorio.NivelRepositorio;
import com.example.reciclaje.repositorio.UsuarioLogroRepositorio;
import com.example.reciclaje.repositorio.UsuarioRepositorio;

@ExtendWith(MockitoExtension.class)
public class UsuarioServicioTest {

    @Mock
    private UsuarioRepositorio usuarioRepository;
    @Mock
    private NivelRepositorio nivelRepository;
    @Mock
    private LogroRepositorio logroRepositorio;
    @Mock
    private UsuarioLogroRepositorio usuarioLogroRepositorio;

    @InjectMocks
    private UsuarioServicio usuarioServicio;

    @Test
    void testAgregarPuntos() {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setId(1L);
        mockUsuario.setPuntos(10);
        
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(mockUsuario));
        when(nivelRepository.findAll()).thenReturn(java.util.Collections.emptyList());
        when(logroRepositorio.findAll()).thenReturn(java.util.Collections.emptyList());
        when(usuarioLogroRepositorio.findByUsuarioId(1L)).thenReturn(java.util.Collections.emptyList());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario resultado = usuarioServicio.agregarPuntos(1L, 5);

        assertNotNull(resultado);
        assertEquals(15, resultado.getPuntos());
        verify(usuarioRepository, atLeastOnce()).save(any(Usuario.class));
    }

    @Test
    void testObtenerPosicionRanking() {
        when(usuarioRepository.countByPuntosGreaterThan(100)).thenReturn(3L);
        
        int posicion = usuarioServicio.obtenerPosicionRanking(100);
        
        assertEquals(4, posicion);
        verify(usuarioRepository, times(1)).countByPuntosGreaterThan(100);
    }
}
