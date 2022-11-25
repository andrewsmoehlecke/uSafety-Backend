package com.api.usafety_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.usafety_backend.entities.Comentario;
import com.api.usafety_backend.repositories.ComentarioRepository;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    public void save(Comentario comentario) {
        comentarioRepository.save(comentario);
    }
}
