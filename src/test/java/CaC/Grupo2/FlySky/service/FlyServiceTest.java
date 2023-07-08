package CaC.Grupo2.FlySky.service;

import CaC.Grupo2.FlySky.dto.AsientoDto;
import CaC.Grupo2.FlySky.dto.ReservaDto;
import CaC.Grupo2.FlySky.entity.Vuelo;
import CaC.Grupo2.FlySky.exception.IllegalArgumentException;
import CaC.Grupo2.FlySky.exception.NotFoundException;
import CaC.Grupo2.FlySky.repository.FlyRepository;
import CaC.Grupo2.FlySky.repository.UsuarioRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class FlyServiceTest {

    @Autowired
    IFlyService flyService;

    @Autowired
    FlyRepository flyRepository;
    UsuarioRepository usuarioRepository;

    ReservaDto reservaDto = new ReservaDto();

    @Test
    @DisplayName("validar Lista Vuelo Vacia..")
    void ListaVuelosVacia(){

        //arrange
        flyRepository= Mockito.mock(FlyRepository.class);
        List<Vuelo> listaMockVuelos = new ArrayList<>();

        when(flyRepository.findAll()).thenReturn(listaMockVuelos);

        //assert
        NotFoundException exception= assertThrows(NotFoundException.class,()->{
            flyService.buscarTodosVuelos();
        });

        // Assert
        String expectedMessage = "la lista de vuelos esta vacía";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }
    @Test
    @DisplayName("validar usuario que no existe..")
    void validarUsuarioNoExistente(){
        reservaDto.setUsuarioID(500l);

        //act and Assert
        Exception exception= assertThrows(IllegalArgumentException.class,()->{
            flyService.reservarVuelo(reservaDto);
        });

        // Assert
        String expectedMessage = "El usuario no existe";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("validar tipo usuario no Cliente..")
    void validarTipoUsuarioNoCliente() {

        //Verificacion tipo de usuario Admin
        // Arrange
        reservaDto.setUsuarioID(1L); // Usuario existente en el repositorio tipo Administrador

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            flyService.reservarVuelo(reservaDto);
        });

        // Assert
        String expectedMessage = "Por favor Registrese para reservar un vuelo";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);

        //Verificacion tipo de usuario Agente_Ventas
        // Arrange
        reservaDto.setUsuarioID(2L); // Usuario existente en el repositorio tipo Agente_Ventas

        // Act
        Exception exception2 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            flyService.reservarVuelo(reservaDto);
        });

        // Assert
        String expectedMessage2 = "Por favor Registrese para reservar un vuelo";
        String actualMessage2 = exception2.getMessage();
        Assertions.assertEquals(expectedMessage2, actualMessage2);

    }

    @Test
    @DisplayName("validar vuelo que no existe..")
    void validarVueloNoExistente(){
        reservaDto.setUsuarioID(3L); //usuario tipo cliente
        reservaDto.setVueloID(50000l);

        //act and assert
        Exception exception = assertThrows(IllegalArgumentException.class,()->{
            flyService.reservarVuelo(reservaDto);
        });

        // Assert
        String expectedMessage = "No se encontró el vuelo con el ID especificado";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);

    }

    @Test
    @DisplayName("validar Asiento NO Existente..")
    void validarAsientoNoExistente(){


        List<AsientoDto> asientos = new ArrayList<>();
        asientos.add(new AsientoDto(4000l,"1V","JIMGAVIDIA",true,"Ventana"));

        reservaDto.setUsuarioID(4L);
        reservaDto.setVueloID(1l);
        reservaDto.setAsientos(asientos);

        //act and assert
        Exception exception =  assertThrows(IllegalArgumentException.class,()->{
            flyService.reservarVuelo(reservaDto);
        });

        // Assert
        String expectedMessage = "No se encontró el asiento con el ID especificado";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);


    }



    @Test
    @DisplayName("validar vuelo Caducado..")
    void validarVueloCaducado(){

        reservaDto.setUsuarioID(4L);
        reservaDto.setVueloID(2l);

        //act and assert
        Exception exception = assertThrows(IllegalArgumentException.class,()->{
            flyService.reservarVuelo(reservaDto);
        });

        // Assert
        String expectedMessage = "el vuelo que intenta reserva su fecha ya caduco";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }



    @Test
    @DisplayName("validar Asiento Ocupado..")
    void validarAsientoOcupado(){

        AsientoDto asiento = new AsientoDto();
        asiento.setAsientoID(1l);
        asiento.setOcupado(true);

        List<AsientoDto> asientos = new ArrayList<>();

        reservaDto.setUsuarioID(4L);
        reservaDto.setVueloID(1l);
        reservaDto.setAsientos(asientos);

        //act and assert
        Exception exception = assertThrows(IllegalArgumentException.class,()->{
            flyService.reservarVuelo(reservaDto);
        });

        // Assert
        String expectedMessage = "el vuelo que intenta reserva su fecha ya caduco";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }
/*
    @Test
    @DisplayName("Camino no tan feliz...")
    void buscarTodosThrowExTest(){
        //act and assert
        assertThrows(IllegalArgumentException.class,()->{
            userService.buscarTodos();
        });
    }

    @Test
    @DisplayName("US0001-Camino Feliz :D")
    void buscarTodosOkTest() {

        //ARRANGE
        List<UserDto> expected = new ArrayList<>();
        expected.add(new UserDto("Jose","Perez", 32));
        expected.add(new UserDto("María", "Paz", 25));
        expected.add(new UserDto("Emilio", "Gonzales", 30));


        //ACT
        List<UserDto> result = userService.buscarTodos();
        //ASSERT
        assertEquals(expected,result);
    }


    @Test
    @DisplayName("US0002-Camino feliz :D")
    void buscarUnUsuarioPorNombreOKTest(){

        //ARRANGE
        String name= "Jose";
        UserDto expected = new UserDto("Jose","Perez", 32);

        //ACT
        UserDto result = userService.buscarUnUsuarioPorNombre(name);

        //ASSERT
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("US0001-Test para probar el camino correcto del metodo calculeTotal")
    void calculeTotalOkTest(){
        //Arrange - Definir los parámetros con valores que debe recibir el método a testear.
        int a = 6;
        int b = 4;
        int resultadoEsperado = 11;

        boolean var1 = true;
        boolean var2 = false;

        //Act
        int result = userService.calculeTotal(a,b);

        //Assert
        assertEquals(resultadoEsperado,result);

    }

 */























}
