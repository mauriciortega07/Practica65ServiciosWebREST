package com.ebac.practica65ServiciosWebREST.repository;

import com.ebac.practica65ServiciosWebREST.dto.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
