package com.api.usafety_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.usafety_backend.entities.Comentario;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    public List<Comentario> findByTopico_IdAndAutor_Ativo(Long id, boolean ativo);
}
