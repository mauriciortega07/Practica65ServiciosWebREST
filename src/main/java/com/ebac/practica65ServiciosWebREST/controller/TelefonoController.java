package com.ebac.practica65ServiciosWebREST.controller;

import com.ebac.practica65ServiciosWebREST.dto.Telefono;
import com.ebac.practica65ServiciosWebREST.dto.Usuario;
import com.ebac.practica65ServiciosWebREST.service.TelefonoService;
import com.ebac.practica65ServiciosWebREST.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class TelefonoController {

    @Autowired
    TelefonoService telefonoService;

    @Autowired
    UsuarioService usuarioService;



    @PostMapping("/telefonos")
    public ResponseEntity<ResponseWrapper<Telefono>> guardarTelefono(@RequestBody Telefono telefono) throws Exception {
        Usuario usuarioARegistrarTelefono = telefono.getUsuario();
        int idUsuarioARegistrarTelefono = usuarioARegistrarTelefono.getIdUsuario();

        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioPorID(idUsuarioARegistrarTelefono);

        if (usuarioOptional.isPresent()) {
            log.info("Se encontro el usuario al cual se registrara el telefono");
            Telefono telefonoGuardado = telefonoService.guardarTelefono(telefono);
            ResponseEntity<Telefono> responseEntity = ResponseEntity.created(new URI("http://localhost/usuarios")).body(telefonoGuardado);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper<>(true, "Telefono Guardado", responseEntity));
        }

        log.info("No hay un usuario a cual registrar el telefono");
        ResponseEntity<Telefono> responseEntity = ResponseEntity.notFound().build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper<>(false, "No se encontro usuario a quien asociar este telefono", responseEntity));

        /*try {
            if(telefono.getUsuario() == null){
                log.warn("El telfono recibido no tiene usuario asignado");
                ResponseEntity responseEntity = ResponseEntity.badRequest().build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper<>(false, "Debe haber un usuario registrado para asociarle un telefono", responseEntity));
            }

            List<Telefono> telefonoList = telefonoService.obtenerTelefonos();
            boolean existUser = false;
            Usuario telefonoUsuario = telefono.getUsuario();

            for (Telefono tel : telefonoList) {
                log.info("Se comienza a buscar si existe un usuario para asociar el numero");
                Usuario usuarioTel = tel.getUsuario();
                if (usuarioTel != null && usuarioTel.equals(telefonoUsuario)) {
                    existUser = true;
                    break;
                }
            }

            if (existUser) {
                log.info("No se encontro el usuario");
                ResponseEntity<Telefono> responseEntity = ResponseEntity.notFound().build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper<>(false, "No se encontro usuario a quien asociar este telefono", responseEntity));
            }

            log.info("Se encontro el usuario, se registrara el telefono");
            Telefono telefonoGuardado = telefonoService.guardarTelefono(telefono);
            ResponseEntity<Telefono> responseEntity = ResponseEntity.created(new URI("http://localhost/usuarios")).body(telefonoGuardado);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper<>(true, "Telefono Guardado", responseEntity));

        } catch (DataIntegrityViolationException e) {
            log.error("Error de persistencia" + e.getMessage());
            ResponseEntity<Telefono> responseEntity = ResponseEntity.badRequest().build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper<>(false, "Usuario no existente en la BD, debe registrarse", responseEntity));
        } catch (Exception e){
            log.error("Error de persistencia SQL" + e.getMessage());
            ResponseEntity<Telefono> responseEntity = ResponseEntity.badRequest().build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper<>(false, "Usuario no existente en la BD, debe registrarse", responseEntity));
        }*/


    }

    @GetMapping("/telefonos")
    public ResponseEntity<ResponseWrapper<List<Telefono>>> obtenerTelefonos() {
        List<Telefono> telefonoList = telefonoService.obtenerTelefonos();

        if (telefonoList.isEmpty()) {
            log.info("La lista se encuentra vacia");
            ResponseEntity<List<Telefono>> responseEntity = ResponseEntity.notFound().build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper<>(false, "La lista esta vacia", responseEntity));
        }

        ResponseEntity<List<Telefono>> responseEntity = ResponseEntity.ok(telefonoList);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(true, "Registros Obtenidos", responseEntity));
    }

    @GetMapping("/telefonos/{idTelefono}")
    public ResponseEntity<ResponseWrapper<Telefono>> obtenerTelefonoPorID(@PathVariable int idTelefono) {
        Optional<Telefono> telefonoOptional = telefonoService.obtenerTelefonoPorID(idTelefono);

        if (telefonoOptional.isPresent()) {
            Telefono telefonoObtenido = telefonoOptional.get();
            ResponseEntity<Telefono> responseEntity = ResponseEntity.accepted().body(telefonoObtenido);
            log.info("Se encontro el telefono");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseWrapper<>(true, "Telefono Obtenido", responseEntity));
        } else {
            log.info("No esta registrado el telefono");
            ResponseEntity<Telefono> responseEntity = ResponseEntity.notFound().build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper<>(false, "Telefono no registrado", responseEntity));
        }
    }

    @PutMapping("/telefonos/{idTelefono}")
    public ResponseEntity<ResponseWrapper<Telefono>> actualizarTelefono(@PathVariable int idTelefono, @RequestBody Telefono telefonoActualizado) throws Exception {
        Optional<Telefono> telefonoOptional = telefonoService.obtenerTelefonoPorID(idTelefono);
        if (telefonoOptional.isPresent()) {
            Telefono telefonoObtenido = telefonoOptional.get();

            telefonoActualizado.setIdTelefono(telefonoObtenido.getIdTelefono());

            if (telefonoActualizado.getUsuario() == null) {
                telefonoActualizado.setUsuario(telefonoObtenido.getUsuario());
            }

            Telefono telefonoGuardado = telefonoService.actualizarTelefono(telefonoActualizado);

            ResponseEntity<Telefono> responseEntity = ResponseEntity.status(202).body(telefonoGuardado);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseWrapper<>(true, "Telefono editado", responseEntity));
        } else {
            ResponseEntity<Telefono> responseEntity = ResponseEntity.notFound().build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper<>(false, "Telefono no encontrado", responseEntity));
        }
    }

    @DeleteMapping("/telefonos/{idTelefono}")
    public ResponseEntity<ResponseWrapper<Void>> eliminarTelefonoPorID(@PathVariable long idTelefono) {
        Optional<Telefono> telefonoOptional = telefonoService.obtenerTelefonoPorID(idTelefono);
        if (telefonoOptional.isEmpty()) {
            ResponseEntity<Void> responseEntity = ResponseEntity.notFound().build();
            log.info("No hay telefono con ese ID registrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper<Void>(false, "No se encontro el telefono solicitado", responseEntity));
        } else {
            telefonoService.eliminarTelefonoPorID(idTelefono);
            ResponseEntity<Void> responseEntity = ResponseEntity.ok().build();
            log.info("Telefono localizado eliminado");
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(true, "Registro eliminado"));
        }
    }
}
