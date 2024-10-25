package org.rburgos.appmockito.ejemplos.repositories;

import org.rburgos.appmockito.ejemplos.models.Examen;

import java.util.List;

public class ExamenRepositoryImpl implements ExamenRepository {
    @Override
    public Examen guardarExamen(Examen examen) {
        return null;
    }

    @Override
    public List<Examen> findAll() {
        return List.of(
                new Examen(5L, "Matematicas"),
                new Examen(6L, "Lenguaje"),
                new Examen(7L, "Historia"));
    }
}
