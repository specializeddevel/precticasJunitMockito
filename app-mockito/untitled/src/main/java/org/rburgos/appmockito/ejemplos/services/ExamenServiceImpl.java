package org.rburgos.appmockito.ejemplos.services;

import org.rburgos.appmockito.ejemplos.models.Examen;
import org.rburgos.appmockito.ejemplos.repositories.ExamenRepository;
import org.rburgos.appmockito.ejemplos.repositories.PreguntaRepository;

import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements ExamenService {

    private ExamenRepository examenRepository;
    private PreguntaRepository preguntaRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository, PreguntaRepository preguntaRepository)
    {
        this.examenRepository = examenRepository;
        this.preguntaRepository = preguntaRepository;
    }

    @Override
    public Optional<Examen> findExamenPorNombre(String nombre) {
        return examenRepository.findAll()
                .stream()
                .filter(e -> e.getNombre().equals(nombre))
                .findFirst();
    }

    @Override
    public Examen finExamenPorNombreConPreguntas(String nombre) {
        Optional<Examen> examenOptional = findExamenPorNombre(nombre);
        Examen examen = examenOptional.orElse(null);
        if (examenOptional.isPresent()) {
            examen = examenOptional.orElseThrow();
            List<String> preguntas = preguntaRepository.findPreguntasPorExamenId(examen.getId());
            examen.setPreguntas(preguntas);
        }
        return examen;
    }


}
