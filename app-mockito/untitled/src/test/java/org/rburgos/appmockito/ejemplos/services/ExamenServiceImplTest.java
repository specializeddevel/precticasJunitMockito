package org.rburgos.appmockito.ejemplos.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.rburgos.appmockito.ejemplos.models.Examen;
import org.rburgos.appmockito.ejemplos.repositories.ExamenRepository;
import org.rburgos.appmockito.ejemplos.repositories.PreguntaRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ExamenServiceImplTest {

    ExamenRepository examenRepository;
    PreguntaRepository preguntaRepository;
    ExamenService service;

    @BeforeEach
    void setUp() {
        examenRepository = mock(ExamenRepository.class);
        preguntaRepository = mock(PreguntaRepository.class);
        service = new ExamenServiceImpl(examenRepository, preguntaRepository);
    }

    @Test
    void findExamenPorNombre() {


        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");

        assertTrue(examen.isPresent());
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.orElseThrow().getNombre());
    }

    @Test
    void findExamenPorNombreEmptyList() {

        List<Examen> datos = Collections.emptyList();
        when(examenRepository.findAll()).thenReturn(datos);

        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");

        assertFalse(examen.isPresent());

    }

    @Test
    void testPreguntasExamen() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.finExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5, examen.getPreguntas().size());
        assertEquals("Matematicas", examen.getNombre());
        assertTrue(examen.getPreguntas().contains("integrales"));
    }

    @Test
    void testPreguntasExamenVerify() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.finExamenPorNombreConPreguntas("Matematicas");

        //Verifica que se invoque el metodo
        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(5L);
        //Verifica que se ejecute el metodo N cantidad de veces, en este caso una
        verify(preguntaRepository, times(1)).findPreguntasPorExamenId(anyLong());
    }

    @Test
    void testNoExisteExamenVerify() {
        when(examenRepository.findAll()).thenReturn(Collections.emptyList());
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.finExamenPorNombreConPreguntas("Matematicas");

        assertNull(examen);
        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());

    }
}