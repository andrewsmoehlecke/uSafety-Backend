package com.api.usafety_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.usafety_backend.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByUsername(String username);

    @Query("SELECT u from Usuario u JOIN FETCH u.cargos where u.username = :username")
    Usuario findByUsernameFetchCargos(@Param("username") String username);
}
