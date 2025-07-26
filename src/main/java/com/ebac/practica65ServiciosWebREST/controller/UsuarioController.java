package com.ebac.practica65ServiciosWebREST.controller;

import com.ebac.practica65ServiciosWebREST.dto.Usuario;
import com.ebac.practica65ServiciosWebREST.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/usuarios")
    public ResponseEntity<ResponseWrapper<Usuario>> guardarUsuario(@RequestBody Usuario usuario) throws URISyntaxException {
        List<Usuario> usuarioList = usuarioService.obtenerUsuarios();
        boolean exist = false;

        for (Usuario usuarioExist : usuarioList) {
            System.out.println(usuario.getNombre());
            String usuarioExistNombre = usuarioExist.getNombre();
            String usuarioNuevoNombre = usuario.getNombre();
            if (usuarioNuevoNombre.equalsIgnoreCase(usuarioExistNombre)) {
                exist = true;
                break;
            }
        }

        if (!exist) {
            Usuario usuarioGuardado = usuarioService.guardarUsuario(usuario);
            ResponseEntity<Usuario> responseEntity = ResponseEntity.created(new URI("http://localhost/usuarios")).body(usuarioGuardado);
            log.info("Usuario creado");
            return ResponseEntity.created(new URI("http://localhost/usuarios")).body(new ResponseWrapper<>(true, "Usuario Guardado", responseEntity));
        }

        ResponseEntity<Usuario> responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        log.info("Usuario ya existente");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper<>(false, "Usuario ya Existente", responseEntity));
    }

    @GetMapping("/usuarios")
    public ResponseEntity<ResponseWrapper<List<Usuario>>> obtenerUsuarios() {
        List<Usuario> usuarioList = usuarioService.obtenerUsuarios();

        if (usuarioList.isEmpty()) {
            ResponseEntity<List<Usuario>> responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            log.warn("No hay registros que mostrar");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper<>(false, "No hay registros que mostrar", responseEntity));
        }

        ResponseEntity<List<Usuario>> responseEntity = ResponseEntity.status(HttpStatus.OK).body(usuarioList);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(true, "Registros obtneidos", responseEntity));
    }

    @GetMapping("/usuarios/{idUsuario}")
    public ResponseEntity<ResponseWrapper<Usuario>> obtenerUsuarioPorID(@PathVariable long idUsuario) {
        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioPorID(idUsuario);
        if (usuarioOptional.isPresent()) {
            ResponseEntity<Usuario> responseEntity = ResponseEntity.status(HttpStatus.ACCEPTED).body(usuarioOptional.get());
            log.info("Usuario obtenido: " + usuarioOptional.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseWrapper<>(true, "Usuario encontrado", responseEntity));
        } else {
            ResponseEntity<Usuario> responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            log.warn("El usuario no existe");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper<>(false, "Usuario no existente", responseEntity));
        }
    }

    @PutMapping("/usuarios/{idUsuario}")
    public ResponseEntity<ResponseWrapper<Usuario>> actualizarUsuario(@PathVariable long idUsuario, @RequestBody Usuario usuarioActualizado) {
        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioPorID(idUsuario);

        if (usuarioOptional.isPresent()) {
            Usuario usuarioObtenido = usuarioOptional.get();
            usuarioActualizado.setIdUsuario(usuarioObtenido.getIdUsuario());
            usuarioService.guardarUsuario(usuarioActualizado);
            log.info("Usuario editado");
            ResponseEntity<Usuario> responseEntity = ResponseEntity.status(202).body(usuarioActualizado);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseWrapper<>(true, "Registro Actualizado", responseEntity));
        } else {
            log.warn("Usuario no existente");
            ResponseEntity<Usuario> responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper<>(false, "Registro no existente", responseEntity));
        }
    }

    @DeleteMapping("/usuarios/{idUsuario}")
    public ResponseEntity<ResponseWrapper<Void>> eliminarUsuarioPorID(@PathVariable long idUsuario) {
        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioPorID(idUsuario);

        if (usuarioOptional.isPresent()) {
            usuarioService.eliminarUsuarioPorID(idUsuario);
            log.info("Usuario encontrado y eliminado");
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(true, "Usuario encontrado y eliminado"));
        } else {
            log.warn("Usuario no encontrado");
            ResponseEntity<Void> responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper<>(false, "Usuarion no existente", responseEntity));
        }
    }


}
