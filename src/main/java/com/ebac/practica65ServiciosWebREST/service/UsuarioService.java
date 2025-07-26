package com.ebac.practica65ServiciosWebREST.service;

import com.ebac.practica65ServiciosWebREST.dto.Usuario;
import com.ebac.practica65ServiciosWebREST.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Usuario guardarUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> obtenerUsuarios(){
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerUsuarioPorID(long id){
        return usuarioRepository.findById(id);
    }

    public Usuario actualizarUsuario(Usuario usuarioActualizado){
        return usuarioRepository.save(usuarioActualizado);
    }

    public void eliminarUsuarioPorID(long id){
        usuarioRepository.deleteById(id);
    }

}
