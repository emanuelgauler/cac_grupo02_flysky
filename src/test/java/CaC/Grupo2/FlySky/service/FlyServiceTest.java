package CaC.Grupo2.FlySky.service;

import CaC.Grupo2.FlySky.dto.*;
import CaC.Grupo2.FlySky.entity.Pago.Pago;
import CaC.Grupo2.FlySky.entity.Reserva;
import CaC.Grupo2.FlySky.exception.IllegalArgumentException;
import CaC.Grupo2.FlySky.repository.AsientoRepository;
import CaC.Grupo2.FlySky.repository.FlyRepository;
import CaC.Grupo2.FlySky.repository.UsuarioRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static CaC.Grupo2.FlySky.entity.Pago.TipoPago.efectivo;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class FlyServiceTest {

    @Autowired
    IFlyService flyService;

    @Autowired
    FlyRepository flyRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    AsientoRepository asientoRepository;

    ReservaDto reservaDto = new ReservaDto();

    @Test
    @DisplayName("validar usuario que no existe..")
    void validarUsuarioNoExistente() {
        reservaDto.setUsuarioID(500l);

        //act and Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
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
    void validarVueloNoExistente() {
        reservaDto.setUsuarioID(3L); //usuario tipo cliente
        reservaDto.setVueloID(50000l);

        //act and assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            flyService.reservarVuelo(reservaDto);
        });

        // Assert
        String expectedMessage = "No se encontró el vuelo con el ID especificado";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);

    }

    @Test
    @DisplayName("validar Asiento NO Existente..")
    void validarAsientoNoExistente() {


        List<AsientoDto> asientos = new ArrayList<>();
        asientos.add(new AsientoDto(4000l, "1V", "JIMGAVIDIA", true, "Ventana"));

        reservaDto.setUsuarioID(4L);
        reservaDto.setVueloID(1l);
        reservaDto.setAsientos(asientos);

        //act and assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            flyService.reservarVuelo(reservaDto);
        });

        // Assert
        String expectedMessage = "No se encontró el asiento con el ID especificado";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);


    }


    @Test
    @DisplayName("validar vuelo Caducado..")
    void validarVueloCaducado() {

        reservaDto.setUsuarioID(4L);
        reservaDto.setVueloID(2l);

        //act and assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            flyService.reservarVuelo(reservaDto);
        });

        // Assert
        String expectedMessage = "el vuelo que intenta reserva su fecha ya caduco";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("validar Asiento Ocupado")
    void validarAsientoOcupado() {
        // Arrange
        ReservaDto reservaDto = new ReservaDto();
        AsientoDto asientoDto = new AsientoDto();
        asientoDto.setAsientoID(15L); //este asiento ya se encuentra ocupado
        asientoDto.setOcupado(true);
        List<AsientoDto> asientos = new ArrayList<>();
        asientos.add(asientoDto);
        reservaDto.setUsuarioID(4L); //usuario tipo cliente
        reservaDto.setVueloID(3L);
        reservaDto.setAsientos(asientos);

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            flyService.reservarVuelo(reservaDto);
        });

        // Assert
        String expectedMessage = "el asiento ya se encuentra ocupado";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }
    /*

    @Test
    @DisplayName("validar Reserva Vuelo")
    void validarRservaVueloOK() throws ParseException {
        // Arrange
        AsientoDto asientoDto = new AsientoDto();
        asientoDto.setAsientoID(18L);//este asiento libre
        asientoDto.setPasajero("jim Gavidia");
        asientoDto.setUbicacion("pasillo");
        List<AsientoDto> asientos = new ArrayList<>();
        asientos.add(asientoDto);

        ReservaDto reservaDto = new ReservaDto();
        reservaDto.setUsuarioID(3l); //usuario tipo cliente
        reservaDto.setVueloID(3l);
        reservaDto.setAsientos(asientos);

        // Act
        RespReservaDto result = flyService.reservarVuelo(reservaDto);

        String mensajeExperado ="Su reserva se realizó con éxito... Tienes 10 minutos para realizar el pago";
        // Assertions
        Assertions.assertEquals(mensajeExperado, result.getMensaje());

    }

     */

/*
    @Test
    @DisplayName("validar Tiempo Limite de Pago")
    public void testHaPasadoTiempoLimiteDePago() {
        // Arrange
        // Obtener la fecha actual
        Date fechaActual = new Date();
        Calendar calendar = Calendar.getInstance();
        // quitamos 11 minutos a la fecha actual
        calendar.add(Calendar.MINUTE, -11); //quitamos 11 minutos
        // Obtener la nueva fecha
        Date FechaActualcon11MinMenos = calendar.getTime();

        Reserva reserva = new Reserva();
        reserva.setFechaReserva(FechaActualcon11MinMenos);

        // Act
        long tiempoTranscurrido = fechaActual.getTime() - FechaActualcon11MinMenos.getTime();
        long minutosTranscurridos = TimeUnit.MINUTES.convert(tiempoTranscurrido, TimeUnit.MILLISECONDS);
        boolean haPasadoTiempoLimite = minutosTranscurridos >= 10;

        // Assert
        assertTrue(haPasadoTiempoLimite);

        // Arrange
        Date fechaCreacionReciente = new Date();
        reserva.setFechaReserva(fechaCreacionReciente);

        // Act
        tiempoTranscurrido = fechaActual.getTime() - fechaCreacionReciente.getTime();
        minutosTranscurridos = TimeUnit.MINUTES.convert(tiempoTranscurrido, TimeUnit.MILLISECONDS);
        haPasadoTiempoLimite = minutosTranscurridos >= 10;

        // Assert
        assertFalse(haPasadoTiempoLimite);
    }


 */
    @Test
    @DisplayName("validar reserva no existe al intentar pagar una reserva")
    public void testReservaIdNoEncotradaAlIntentarPagar() {
        // Arrange
        PagoDto pago = new PagoDto();
        pago.setReservaID(50l);

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            flyService.pagarReserva(pago);
        });

        // Assert
        String expectedMessage = "No se encontró la reserva con el ID especificado";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("validar Pagar Reserva ya Pagada")
    public void testIntentarPagarReservaYaPagada() {
        // Arrange
        PagoDto pago = new PagoDto();
        pago.setReservaID(1l); //esta reserva ya se encuentra paga en el repository
        pago.setTipoPago(efectivo);
        pago.setMonto(100);

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            flyService.pagarReserva(pago);
        });

        // Assert
        String expectedMessage = "ya se realizo el pago de esta reserva";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("validar Pagar Reserva ya vencida")
    public void testIntentarPagarReservaConTiempoVencido() {
        // Arrange
        PagoDto pago = new PagoDto();
        pago.setReservaID(2l); //esta reserva ya se encuentra vencida
        pago.setTipoPago(efectivo);
        pago.setMonto(100);

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            flyService.pagarReserva(pago);
        });

        // Assert
        String expectedMessage = "El tiempo maximos para pagar vencio, por favor realice otra reserva";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("validar Pagar Reserva con monto incorrecto")
    public void testIntentarPagarReservaConMontoIncorrecto() {
        // Arrange
        PagoDto pago = new PagoDto();
        pago.setReservaID(3l); // reserva con monto de 150
        pago.setTipoPago(efectivo);
        pago.setMonto(100);

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            flyService.pagarReserva(pago);
        });

        // Assert
        String expectedMessage = "No ingreso el monto correcto";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("validar Pagar Reserva")
    public void testPagarReserva() {
        // Arrange
        PagoDto pago = new PagoDto();
        pago.setReservaID(3l); //esta reserva ya se encuentra cargada en el sql
        pago.setTipoPago(efectivo);
        pago.setMonto(150);

        // Act
        String act = flyService.pagarReserva(pago);

        String expected="Reserva pagada exitosamente";
        //assert
        assertEquals(expected,act);

    }


}







