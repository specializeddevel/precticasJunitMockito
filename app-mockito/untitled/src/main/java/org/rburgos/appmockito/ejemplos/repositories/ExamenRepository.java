package org.rburgos.appmockito.ejemplos.repositories;

import org.rburgos.appmockito.ejemplos.models.Examen;

import java.util.List;

public interface ExamenRepository {

    Examen guardarExamen(Examen examen);
    List<Examen> findAll();

}
