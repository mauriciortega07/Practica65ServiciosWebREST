package com.ebac.practica65ServiciosWebREST.repository;

import com.ebac.practica65ServiciosWebREST.dto.Telefono;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelefonoRepository extends JpaRepository<Telefono, Long> {
}
