package org.rburgos.appmockito.ejemplos.services;

import org.rburgos.appmockito.ejemplos.models.Examen;

import java.util.Optional;

public interface ExamenService {

    Optional<Examen> findExamenPorNombre(String nombre);
    Examen finExamenPorNombreConPreguntas(String nombre);

    Examen guardar(Examen examen);
}
