package org.rburgos.appmockito.ejemplos.services;

import org.rburgos.appmockito.ejemplos.models.Examen;

import java.util.List;

public class Datos {
    public final static List<Examen> EXAMENES = List.of(
            new Examen(5L, "Matematicas"),
            new Examen(6L, "Lenguaje"),
            new Examen(7L, "Historia"));

    public final static List<Examen> EXAMENES_ID_NULL = List.of(
            new Examen(null, "Matematicas"),
            new Examen(null, "Lenguaje"),
            new Examen(null, "Historia"));

    public final static List<String> PREGUNTAS = List.of(
            "aritmetica",
            "integrales",
            "derivadas",
            "trigonometria",
            "geometria"
    );

    public final static Examen EXAMEN = new Examen(null, "Fisica");
}
