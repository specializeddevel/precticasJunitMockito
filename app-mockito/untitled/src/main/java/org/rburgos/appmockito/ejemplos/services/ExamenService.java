package org.rburgos.appmockito.ejemplos.services;

import org.rburgos.appmockito.ejemplos.models.Examen;

public interface ExamenService {

    Examen findExamenPorNombre(String nombre);

}
