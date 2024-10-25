package org.rburgos.appmockito.ejemplos.repositories;

import java.util.List;

public interface PreguntaRepository {

    void guardarVariasPreguntas(List<String> preguntas);
    List<String> findPreguntasPorExamenId(Long examenId);

}
