package com.ebac.practica65ServiciosWebREST.controller;

import com.ebac.practica65ServiciosWebREST.dto.Telefono;
import com.ebac.practica65ServiciosWebREST.dto.Usuario;
import com.ebac.practica65ServiciosWebREST.service.TelefonoService;
import com.ebac.practica65ServiciosWebREST.service.UsuarioService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelefonoControllerTest {

    @BeforeEach
    void inicio(){
        System.out.println("Comienza una prueba de metodos");
    }

    @Mock
    TelefonoService telefonoService;

    @Mock
    UsuarioService usuarioService;

    @InjectMocks
    TelefonoController telefonoController;

    private List<Telefono> generarTelefonos(int elementos){
        return IntStream.range(1, elementos + 1)
                .mapToObj(value -> {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(value);
                    Telefono telefono = new Telefono();
                    telefono.setIdTelefono(value);
                    telefono.setNumero("10102020");
                    telefono.setTipo("casa"+value);
                    telefono.setLada(value);
                    telefono.setUsuario(usuario);
                    return telefono;
                }).collect(Collectors.toList());
    }

    @Test
    void guardarTelefono() throws Exception {

        //Agregamos al usuario a registrar
        Usuario usuarioPrueba = new Usuario();
        usuarioPrueba.setIdUsuario(1);

        Telefono telefono = new Telefono();
        telefono.setUsuario(usuarioPrueba);

        when(usuarioService.obtenerUsuarioPorID(1)).thenReturn(Optional.of(usuarioPrueba));

        when(telefonoService.guardarTelefono(telefono)).thenReturn(telefono);

        ResponseEntity<ResponseWrapper<Telefono>> telefonoResponseEntity = telefonoController.guardarTelefono(telefono);

        ResponseWrapper<Telefono> telefonoActual = telefonoResponseEntity.getBody();


        assertEquals(201, telefonoActual.getResponseEntity().getStatusCode().value());
        assertTrue(telefonoActual.isSucces());
        assertEquals("Telefono Guardado",telefonoActual.getMessage());
        assertEquals(1, telefonoActual.getResponseEntity().getBody().getUsuario().getIdUsuario());
    }

    @Test
    void guardarTelefonoCuandoUsuarioNoExiste() throws Exception {

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);

        Telefono telefono = new Telefono();
        telefono.setUsuario(usuario);

        when(usuarioService.obtenerUsuarioPorID(1)).thenReturn(Optional.empty());


        ResponseEntity<ResponseWrapper<Telefono>> telefono1 = telefonoController.guardarTelefono(telefono);

        ResponseWrapper<Telefono> telefonoResponseWrapper = telefono1.getBody();

        assertEquals(404, telefonoResponseWrapper.getResponseEntity().getStatusCode().value());
        assertFalse(telefonoResponseWrapper.isSucces());
        assertEquals(HttpStatus.NOT_FOUND, telefonoResponseWrapper.getResponseEntity().getStatusCode());

        verify(telefonoService, never()).guardarTelefono(telefono);
    }

    @Test
    void obtenerTelefonos() {
        List<Telefono> telefonoList = generarTelefonos(3);

        when(telefonoService.obtenerTelefonos()).thenReturn(telefonoList);

        ResponseEntity<ResponseWrapper<List<Telefono>>> telefonos = telefonoController.obtenerTelefonos();

        assertEquals(HttpStatus.OK, telefonos.getStatusCode());
        assertTrue(telefonos.getBody().isSucces());
        assertEquals("Registros Obtenidos", telefonos.getBody().getMessage());
    }

    @Test
    void obtenerTelfonosCuandoNoHay(){
        List<Telefono> telefonoList = generarTelefonos(2);

        when(telefonoService.obtenerTelefonos()).thenReturn(List.of());

        ResponseEntity<ResponseWrapper<List<Telefono>>> telefonoResponseEntity = telefonoController.obtenerTelefonos();
        ResponseWrapper<List<Telefono>> telefonoResponseWrapper = telefonoResponseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, telefonoResponseEntity.getStatusCode());
        assertFalse(telefonoResponseWrapper.isSucces());
        assertEquals("La lista esta vacia", telefonoResponseWrapper.getMessage());


    }

    @Test
    void obtenerTelefonoPorID() {
        int idTelefono = 1;
        Telefono telefono = new Telefono();
        telefono.setIdTelefono(1);

        Optional<Telefono> telefonoOptional = telefonoService.obtenerTelefonoPorID(1);

        when(telefonoService.obtenerTelefonoPorID(idTelefono)).thenReturn(Optional.of(telefono));

        ResponseEntity<ResponseWrapper<Telefono>> telefonoPorIDResponseEntity = telefonoController.obtenerTelefonoPorID(idTelefono);
        ResponseWrapper<Telefono> telefonoPorIDResponseWrapper = telefonoPorIDResponseEntity.getBody();
        Telefono telefonoObtneido = telefonoPorIDResponseWrapper.getResponseEntity().getBody();

        assertEquals(HttpStatus.ACCEPTED, telefonoPorIDResponseEntity.getStatusCode());
        assertTrue(telefonoPorIDResponseWrapper.isSucces());
        assertEquals("Telefono Obtenido", telefonoPorIDResponseWrapper.getMessage());
        assertEquals(idTelefono, telefonoObtneido.getIdTelefono());

    }

    @Test
    void obtenerTelefonoPorIDCuandoNoExiste(){
        int idTelefono = 1;
        Telefono telefono = new Telefono();

        when(telefonoService.obtenerTelefonoPorID(idTelefono)).thenReturn(Optional.empty());

        ResponseEntity<ResponseWrapper<Telefono>> telefonoResponseEntity = telefonoController.obtenerTelefonoPorID(idTelefono);
        ResponseWrapper<Telefono> telefonoResponseWrapper = telefonoResponseEntity.getBody();


        assertEquals(HttpStatus.NOT_FOUND, telefonoResponseEntity.getStatusCode());
        assertFalse(telefonoResponseWrapper.isSucces());
        assertEquals("Telefono no registrado", telefonoResponseWrapper.getMessage());
    }

    @Test
    void actualizarTelefono() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(12);

        List<Telefono> telefonoList = generarTelefonos(1);
        Telefono telefonoRegistrado = telefonoList.getFirst();
        int idTelefonoAEditar = telefonoRegistrado.getIdTelefono();
        telefonoRegistrado.setUsuario(usuario);

        String nuevoNumero = "2020204040";
        int nuevaLada = 20;
        String nuevoTipo = "Camper";

        Telefono telefonoActualizado = new Telefono();
        telefonoActualizado.setNumero(nuevoNumero);
        telefonoActualizado.setLada(nuevaLada);
        telefonoActualizado.setTipo(nuevoTipo);

        when(telefonoService.obtenerTelefonoPorID(idTelefonoAEditar)).thenReturn(Optional.of(telefonoRegistrado));
        when(telefonoService.actualizarTelefono(telefonoActualizado)).thenReturn(telefonoActualizado);

        ResponseEntity<ResponseWrapper<Telefono>> telefonoResponseEntity = telefonoController.actualizarTelefono(idTelefonoAEditar, telefonoActualizado);
        ResponseWrapper<Telefono> telefonoResponseWrapper = telefonoResponseEntity.getBody();

        assertTrue(telefonoResponseWrapper.isSucces());
        assertEquals("Telefono editado", telefonoResponseWrapper.getMessage());
        assertEquals(nuevoNumero, telefonoActualizado.getNumero());
        assertEquals(nuevoTipo, telefonoActualizado.getTipo());
        assertEquals(idTelefonoAEditar, telefonoActualizado.getIdTelefono());
        assertEquals(nuevaLada, telefonoActualizado.getLada());
        assertEquals(12, telefonoRegistrado.getUsuario().getIdUsuario());
    }

    @Test
    void actualizarTelefonoCuandoNoExiste() throws Exception {
        int idTelefono = 10;

        List<Telefono> telefonoList = generarTelefonos(1);
        Telefono telefonoRegistrado = telefonoList.getFirst();
        Telefono telefonoActualizado = new Telefono();

        when(telefonoService.obtenerTelefonoPorID(idTelefono)).thenReturn(Optional.empty());

        ResponseEntity<ResponseWrapper<Telefono>> telefonoResponseEntity = telefonoController.actualizarTelefono(idTelefono, telefonoRegistrado);
        ResponseWrapper<Telefono> telefonoResponseWrapper = telefonoResponseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, telefonoResponseEntity.getStatusCode());
        assertFalse(telefonoResponseWrapper.isSucces());
        assertEquals("Telefono no encontrado", telefonoResponseWrapper.getMessage());

        verify(telefonoService, never()).actualizarTelefono(telefonoActualizado);

    }

    @Test
    void eliminarTelefonoPorID() {
        int idTelefonoAEliminar = 5;
        Telefono telefonoAEliminar = new Telefono();


        Optional<Telefono> telefonoOptional = telefonoService.obtenerTelefonoPorID(idTelefonoAEliminar);

        when(telefonoService.obtenerTelefonoPorID(idTelefonoAEliminar)).thenReturn(Optional.of(telefonoAEliminar));
        doNothing().when(telefonoService).eliminarTelefonoPorID((long) idTelefonoAEliminar);

        ResponseEntity<ResponseWrapper<Void>> telefonoResponseEntity = telefonoController.eliminarTelefonoPorID(idTelefonoAEliminar);
        ResponseWrapper<Void> telefonoRepsonseWrapper = telefonoResponseEntity.getBody();

        assertEquals(HttpStatus.OK, telefonoResponseEntity.getStatusCode());
        assertEquals("Registro eliminado", telefonoRepsonseWrapper.getMessage());
        assertTrue(telefonoRepsonseWrapper.isSucces());

    }

    @Test
    void eliminarTelefonoPorIDCuandoNoExiste(){
        int idTelefonoAEliminar = 5;
        Telefono telefonoAEliminar = new Telefono();

        when(telefonoService.obtenerTelefonoPorID(idTelefonoAEliminar)).thenReturn(Optional.empty());

        ResponseEntity<ResponseWrapper<Void>> telefonoResponseEntity = telefonoController.eliminarTelefonoPorID((long) idTelefonoAEliminar);
        ResponseWrapper<Void> telefonoResponseWrapper = telefonoResponseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, telefonoResponseEntity.getStatusCode());
        assertFalse(telefonoResponseWrapper.isSucces());
        assertEquals("No se encontro el telefono solicitado", telefonoResponseWrapper.getMessage());

        verify(telefonoService, never()).eliminarTelefonoPorID((long)idTelefonoAEliminar);
        verify(telefonoService, atLeastOnce()).obtenerTelefonoPorID(idTelefonoAEliminar);

    }

    @AfterEach
    void fin(){
        System.out.println("fin de test");
    }
}