package com.ebac.practica65ServiciosWebREST.service;

import com.ebac.practica65ServiciosWebREST.dto.Telefono;
import com.ebac.practica65ServiciosWebREST.repository.TelefonoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TelefonoService {

    @Autowired
    TelefonoRepository telefonoRepository;

    public Telefono guardarTelefono(Telefono telefono) throws Exception {
        if (telefono.getNumero().length() <= 15) {
            return telefonoRepository.save(telefono);
        }
        throw new Exception("El telefono es invalido");
    }

    public List<Telefono> obtenerTelefonos() {
        return telefonoRepository.findAll();
    }

    public Optional<Telefono> obtenerTelefonoPorID(long id){
        return telefonoRepository.findById(id);
    }

    public Telefono actualizarTelefono(Telefono telefonoActualizado) throws Exception {
        if (telefonoActualizado.getNumero().length() <= 15) {
            return telefonoRepository.save(telefonoActualizado);
        }
        throw new Exception("El telefono es invalido");
    }

    public void eliminarTelefonoPorID(Long id){
        telefonoRepository.deleteById(id);
    }

}