package org.rburgos.appmockito.ejemplos.services;

import org.rburgos.appmockito.ejemplos.models.Examen;

import java.util.List;

public class Datos {
    public final static List<Examen> EXAMENES = List.of(
            new Examen(5L, "Matematicas"),
            new Examen(6L, "Lenguaje"),
            new Examen(7L, "Historia"));

    public final static List<String> PREGUNTAS = List.of(
            "aritmetica",
            "integrales",
            "derivadas",
            "trigonometria",
            "geometria"
    );
}
