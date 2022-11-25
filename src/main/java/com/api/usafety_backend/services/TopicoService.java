package com.api.usafety_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.usafety_backend.entities.Topico;
import com.api.usafety_backend.repositories.TopicoRepository;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    public void save(Topico topico) {
        topicoRepository.save(topico);
    }
}
