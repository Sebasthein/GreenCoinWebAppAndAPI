package com.example.reciclaje.servicio;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.reciclaje.entidades.Reciclaje;
import com.example.reciclaje.entidades.Usuario;

@ExtendWith(MockitoExtension.class)
public class ReciclajeServicioTest {

    @Mock
    private UsuarioServicio usuarioService;

    @InjectMocks
    private ReciclajeServicio reciclajeServicio;

    @Test
    void testGetPuntosAcumulados() {
        Usuario mockUser = new Usuario();
        mockUser.setId(1L);

        Reciclaje r1 = new Reciclaje();
        r1.setValidado(true);
        r1.setPuntosGanados(10);
        
        Reciclaje r2 = new Reciclaje();
        r2.setValidado(false);
        r2.setPuntosGanados(20);
        
        Reciclaje r3 = new Reciclaje();
        r3.setValidado(true);
        r3.setPuntosGanados(15);
        
        mockUser.setReciclajes(Arrays.asList(r1, r2, r3));
        
        when(usuarioService.obtenerUsuario(1L)).thenReturn(mockUser);
        
        int result = reciclajeServicio.getPuntosAcumulados(1L);
        
        // Sum of valid points (10 + 15) = 25
        assertEquals(25, result);
        verify(usuarioService, times(1)).obtenerUsuario(1L);
    }
}
