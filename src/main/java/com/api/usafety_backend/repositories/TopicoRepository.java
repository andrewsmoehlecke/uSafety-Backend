package com.api.usafety_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.usafety_backend.entities.Topico;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {

}
