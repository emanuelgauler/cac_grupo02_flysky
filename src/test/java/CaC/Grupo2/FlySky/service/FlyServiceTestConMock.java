package CaC.Grupo2.FlySky.service;


import CaC.Grupo2.FlySky.dto.AsientoDto;
import CaC.Grupo2.FlySky.dto.ReservaDto;
import CaC.Grupo2.FlySky.dto.RespReservaDto;
import CaC.Grupo2.FlySky.dto.VueloDto;
import CaC.Grupo2.FlySky.entity.Asiento;
import CaC.Grupo2.FlySky.entity.Reserva;
import CaC.Grupo2.FlySky.entity.Vuelo;
import CaC.Grupo2.FlySky.entity.usuario.Usuario;
import CaC.Grupo2.FlySky.exception.NotFoundException;
import CaC.Grupo2.FlySky.repository.AsientoRepository;
import CaC.Grupo2.FlySky.repository.FlyRepository;
import CaC.Grupo2.FlySky.repository.ReservaRepository;
import CaC.Grupo2.FlySky.repository.UsuarioRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

import static CaC.Grupo2.FlySky.entity.usuario.TipoUsuarioEnum.CLIENTE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class FlyServiceTestConMock {


    @Mock
    FlyRepository flyRepository;
    @Mock
    ReservaRepository reservaRepository;

    @Mock
    UsuarioRepository usuarioRepository;
    @Mock
    AsientoRepository asientoRepository;
    @InjectMocks
    FlyService flyService;



    @Test
    @DisplayName("validar Lista Vuelo Vacia..")
    void ListaVuelosVacia() {

        //arrange
        List<Vuelo> listaVueloVacia = new ArrayList<>();

        when(flyRepository.findAll()).thenReturn(listaVueloVacia);

        //assert
        Exception exception = assertThrows(NotFoundException.class, () -> {
            flyService.buscarTodosVuelos();
        });

        // Assert
        String expectedMessage = "la lista de vuelos esta vacía";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("validar Lista Vuelo")
    void validarListaVueloOK() {
        //arrange
        List<Asiento> asientosEnt = new ArrayList<>();
        Asiento asiento1 = new Asiento();
        asiento1.setAsientoID(50l);
        asiento1.setUbicacion("ventana");
        asiento1.setNombreAsiento("1V");
        asientosEnt.add(asiento1);

        Asiento asiento2 = new Asiento();
        asiento2.setAsientoID(51l);
        asiento2.setOcupado(true);
        asientosEnt.add(asiento2);

        Calendar calendar = Calendar.getInstance();
        // agregamos 30 minutos a la fecha actual
        calendar.add(Calendar.MINUTE, 30); //agregamos 30 minutos
        // Obtener la nueva fecha
        Date FechaActualcon30MinMas = calendar.getTime();

        List<Vuelo> vuelosEnt = new ArrayList<>();

        Vuelo vuelo1 = new Vuelo();
        vuelo1.setVueloID(7L);
        vuelo1.setFecha(FechaActualcon30MinMas);
        vuelo1.setAerolinea("azul");
        vuelo1.setOrigen("venezuela");
        vuelo1.setDestino("Buenos Aires");
        vuelo1.setAsientos(asientosEnt);
        vuelosEnt.add(vuelo1);

        when(flyRepository.findAll()).thenReturn(vuelosEnt);

        // Act
        List<VueloDto> result = flyService.buscarTodosVuelos();

        // Assertions
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(7L, result.get(0).getVueloID());
        assertEquals("azul", result.get(0).getAerolinea());
        assertEquals("venezuela", result.get(0).getOrigen());
        assertEquals("Buenos Aires", result.get(0).getDestino());
    }

    @Test
    @DisplayName("validar reserva vuelo")
    void reservarVueloOK() {
        // arrange
        Calendar calendar = Calendar.getInstance();
        // agregamos 30 minutos a la fecha actual
        calendar.add(Calendar.MINUTE, 30); //agregamos 30 minutos
        // Obtener la nueva fecha
        Date FechaActualcon30MinMas = calendar.getTime();

        ReservaDto reservaDto = new ReservaDto();
        reservaDto.setUsuarioID(1L);
        reservaDto.setVueloID(1L);

        Usuario usuario = new Usuario();
        usuario.setUsuarioID(1L);
        usuario.setTipoUsuario(CLIENTE);

        Vuelo vueloExistente = new Vuelo();
        vueloExistente.setVueloID(1L);
        vueloExistente.setFecha(FechaActualcon30MinMas);
        vueloExistente.setPrecio(100.0);

        AsientoDto asientoDto = new AsientoDto();
        asientoDto.setAsientoID(1L);
        asientoDto.setPasajero("Jim Gavidia");
        asientoDto.setUbicacion("Ventana");

        List<AsientoDto> asientosDto = new ArrayList<>();
        asientosDto.add(asientoDto);
        reservaDto.setAsientos(asientosDto);

        List<Asiento> asientosReservados = new ArrayList<>();
        Asiento asientoExistente = new Asiento();
        asientoExistente.setAsientoID(1L);
        asientoExistente.setOcupado(false);
        asientoExistente.setUbicacion("Ventana");
        asientosReservados.add(asientoExistente);

        Reserva persistReserva = new Reserva();
        persistReserva.setReservaID(1L);
        persistReserva.setUsuario(usuario);
        persistReserva.setAsientos(asientosReservados);
        persistReserva.setFechaReserva(new Date());
        persistReserva.setVueloID(1L);
        persistReserva.setMonto(100.0);

        when(usuarioRepository.findById(1L)).thenReturn(java.util.Optional.of(usuario));
        when(flyRepository.findById(1L)).thenReturn(java.util.Optional.of(vueloExistente));
        when(asientoRepository.findById(1L)).thenReturn(java.util.Optional.of(asientoExistente));
        when(reservaRepository.save(Mockito.any(Reserva.class))).thenReturn(persistReserva);

        // Act
        RespReservaDto respReservaDto = flyService.reservarVuelo(reservaDto);

        // Assertions
        assertNotNull(respReservaDto);
        assertEquals(1L, respReservaDto.getReserva().getNumeroReserva());
        assertEquals(1L, respReservaDto.getReserva().getUsuarioID());
        assertEquals(1, respReservaDto.getReserva().getAsientos().size());
        assertEquals("Jim Gavidia", respReservaDto.getReserva().getAsientos().get(0).getPasajero());
        assertEquals("Ventana", respReservaDto.getReserva().getAsientos().get(0).getUbicacion());
        assertEquals("Su reserva se realizó con éxito... Tienes 10 minutos para realizar el pago", respReservaDto.getMensaje());
    }

}
