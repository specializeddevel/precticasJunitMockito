package org.rburgos.appmockito.ejemplos.repositories;

import java.util.List;

public interface PreguntaRepository {

    List<String> findPreguntasPorExamenId(Long examenId);

}
