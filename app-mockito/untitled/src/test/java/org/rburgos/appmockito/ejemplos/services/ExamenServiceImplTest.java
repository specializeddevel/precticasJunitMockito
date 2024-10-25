package org.rburgos.appmockito.ejemplos.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.rburgos.appmockito.ejemplos.models.Examen;
import org.rburgos.appmockito.ejemplos.repositories.ExamenRepository;
import org.rburgos.appmockito.ejemplos.repositories.PreguntaRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//Esta anotacion permite utilizar las anotaciones de mockito
@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

    //Inyecta el examenRepository como mock
    @Mock
    ExamenRepository examenRepository;

    @Mock
    PreguntaRepository preguntaRepository;

    //Inyecta el servicio y le pasa los mock al contructor
    @InjectMocks
    ExamenServiceImpl service;

    @BeforeEach
    void setUp() {
        //Esta es otra forma de habilitar las anotaciones de mockito
//        MockitoAnnotations.openMocks(this);
        // No es necesario hacer la inicializacion de los mock porque arriba ya se anotaron con @mock
//        examenRepository = mock(ExamenRepository.class);
//        preguntaRepository = mock(PreguntaRepository.class);
//        service = new ExamenServiceImpl(examenRepository, preguntaRepository);
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

        //GIVEN
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        // WHEN
        Examen examen = service.finExamenPorNombreConPreguntas("Matematicas");

        // THEN
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

        // GIVEN
        when(examenRepository.findAll()).thenReturn(Collections.emptyList());
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        // WHEN
        Examen examen = service.finExamenPorNombreConPreguntas("Matematicas");

        // THEN
        assertNull(examen);
        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());

    }

    @Test
    void testGuardarExamen() {

        // GIVEN
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        //Aqui se genera automaticamante el ID para el examen que se esta guardando y se retorna el objeto con ID
        when(examenRepository.guardarExamen(any(Examen.class))).then(new Answer<Examen>() {

            Long secuencia = 8L;

            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(secuencia++);

                return examen;
            }
        });

        // WHEN
        Examen examen = service.guardar(Datos.EXAMEN);

        // THEN
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Fisica", examen.getNombre());

        verify(examenRepository).guardarExamen(any(Examen.class));
        verify(preguntaRepository).guardarVariasPreguntas(anyList());
    }


    @Test
    void testManejoException() {

        //GIVEN
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
        //lanzara una excepcion al recibir argumento null
        when(preguntaRepository.findPreguntasPorExamenId(isNull())).thenThrow(IllegalArgumentException.class);

        // THEN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.finExamenPorNombreConPreguntas("Matematicas");
        });

        assertEquals(IllegalArgumentException.class, exception.getClass());

        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(isNull());
    }
}