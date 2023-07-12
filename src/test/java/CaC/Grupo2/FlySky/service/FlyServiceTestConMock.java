package CaC.Grupo2.FlySky.service;

import CaC.Grupo2.FlySky.dto.request.*;
import CaC.Grupo2.FlySky.dto.response.*;
import CaC.Grupo2.FlySky.entity.Asiento;
import CaC.Grupo2.FlySky.entity.Pago.Pago;
import CaC.Grupo2.FlySky.entity.Reserva;
import CaC.Grupo2.FlySky.entity.Vuelo;
import CaC.Grupo2.FlySky.entity.usuario.TipoUsuarioEnum;
import CaC.Grupo2.FlySky.entity.usuario.Usuario;
import CaC.Grupo2.FlySky.exception.NotFoundException;
import CaC.Grupo2.FlySky.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.util.Optional;
import static CaC.Grupo2.FlySky.entity.Pago.TipoPago.tarjeta_debito;
import static CaC.Grupo2.FlySky.entity.usuario.TipoUsuarioEnum.ADMINISTRADOR;
import static CaC.Grupo2.FlySky.entity.usuario.TipoUsuarioEnum.CLIENTE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SpringBootTest

public class FlyServiceTestConMock {


    @Mock
    FlyRepository flyRepository;
    @Mock
    ReservaRepository reservaRepository;

    @Mock
    UsuarioRepository usuarioRepository;
    @Mock
    PagoRepository pagoRepository;

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
        asientoExistente.setVuelo(vueloExistente);
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
        assertEquals("Su reserva se realizo con exito... Tienes 10 minutos para realizar el pago", respReservaDto.getMensaje());
    }

    @Test
    @DisplayName("US4-Camino Feliz")
    void getHistorialOKTest(){
        //ARRANGE
        //1-Creacion de los Objetos Mocks
        long usIdConsulta = 1L;
        long usIdRespuesta = 2L;

        SolHistorialDto solHistorialDtoMock = new SolHistorialDto();
        solHistorialDtoMock.setUsuarioIdAgente(usIdConsulta);
        solHistorialDtoMock.setUsuarioIdCliente(usIdRespuesta);

        Usuario usConsultaMock = new Usuario(1L, TipoUsuarioEnum.AGENTE_VENTAS,"Max Power",8810);
        Usuario usRespuestaMock = new Usuario(2L,TipoUsuarioEnum.CLIENTE,"Pechugas Laru",9412);

        List<Reserva> todasReservasUserTrueMock = new ArrayList<>();
        todasReservasUserTrueMock.add(new Reserva(1L,usRespuestaMock,null, 1L,true,new Date(2023,8,1),1500,null));
        todasReservasUserTrueMock.add(new Reserva(3L,usRespuestaMock,null, 2L,true,new Date(2023,8,3),1300,null));

        List<Vuelo> todosVuelosMock = new ArrayList<>();
        todosVuelosMock.add(new Vuelo(1L,null,"Buenos Aires","Miami",new Date(2023,9,1),1500.5,false,"AA"));
        todosVuelosMock.add(new Vuelo(2L,null,"Buenos Aires","Roma",new Date(2023,9,2),1400.5,false,"Latam"));
        todosVuelosMock.add(new Vuelo(3L,null,"Buenos Aires","Toronto",new Date(2023,9,3),1300.5,false,"AA"));

        List<VueloDtoSA> vueloDtoSAMock = new ArrayList<>();
        vueloDtoSAMock.add(new VueloDtoSA("Buenos Aires","Miami","AA",new Date(2023,9,1)));
        vueloDtoSAMock.add(new VueloDtoSA("Buenos Aires","Roma","Latam",new Date(2023,9,2)));

        //2-Creación del valor Esperado
        RtaHistorialDto expected = new RtaHistorialDto("Historial y Preferencias de Vuelo del Cliente Pechugas Laru",vueloDtoSAMock);

        //3-Definición del comportamiento del método Mock
        when(usuarioRepository.findById(usIdConsulta)).thenReturn(Optional.of(usConsultaMock));
        when(usuarioRepository.findById(usIdRespuesta)).thenReturn(Optional.of(usRespuestaMock));

        //when(reservaRepositoryConMock.findAll()).thenReturn(todasReservasMock);
        when(reservaRepository.findByUsuario(usRespuestaMock)).thenReturn(todasReservasUserTrueMock);

        when(flyRepository.findAll()).thenReturn(todosVuelosMock);

        //4-Act
        RtaHistorialDto result = flyService.getHistorial(solHistorialDtoMock);

        //5-Assert
        assertEquals(expected,result);

    }

    @Test
    @DisplayName("validar Ventas Diarias")
    void validarConsultaVentaDiarias(){

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

        Pago pago = new Pago();
        pago.setReserva(persistReserva);
        pago.setMonto(157.35);
        pago.setTipoPago(tarjeta_debito);
        pago.setFechaPago(new Date());
        pago.setPagado(true);

        List<Pago> pagos = new ArrayList();
        pagos.add(pago);

        Usuario usuarioAdmin = new Usuario();
        usuarioAdmin.setUsuarioID(2L);
        usuarioAdmin.setTipoUsuario(ADMINISTRADOR);

        when(pagoRepository.findAll()).thenReturn(pagos);
        when(usuarioRepository.findById(2L)).thenReturn(java.util.Optional.of(usuarioAdmin));

        SolVentasDiariasDto solventasDiarias = new SolVentasDiariasDto();
        solventasDiarias.setUsuarioIdAdministrador(2L);

        // Act
        RespVentasDiarias result = flyService.getVentasDiarias(solventasDiarias);
        RespVentasDiarias expected = new RespVentasDiarias(1, 157.35);

        //assert
        assertEquals(expected,result);

    }


    @Test
    @DisplayName("US4-Ex1: llega al controller un usuario de Agente que no existe en la BDD")
    public void testEx1_ThrowsNotFoundException() {
        // Arrange
        long usIdConsulta = 1L;
        long usIdRespuesta = 2L;

        SolHistorialDto solHistorialDto = new SolHistorialDto();
        solHistorialDto.setUsuarioIdAgente(usIdConsulta);
        solHistorialDto.setUsuarioIdCliente(usIdRespuesta);

        when(usuarioRepository.findById(usIdConsulta)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            flyService.getHistorial(solHistorialDto);
        });
    }

    @Test
    @DisplayName("US4-Ex2: Usuario que hace la consulta no es AGENTE DE VENTAS")
    public void testEx2_ThrowsNotFoundException() {
        // Arrange
        long usIdConsulta = 1L;
        long usIdRespuesta = 2L;

        SolHistorialDto solHistorialDto = new SolHistorialDto();
        solHistorialDto.setUsuarioIdAgente(usIdConsulta);
        solHistorialDto.setUsuarioIdCliente(usIdRespuesta);

        Usuario usConsultaMock = new Usuario(1L, TipoUsuarioEnum.ADMINISTRADOR,"Homero S",8810);

        when(usuarioRepository.findById(usIdConsulta)).thenReturn(Optional.of(usConsultaMock));

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            flyService.getHistorial(solHistorialDto);
        });
    }

    @Test
    @DisplayName("US4-Ex3: llega al controller un usuario a consultar que no existe en la BDD")
    public void testEx3_ThrowsNotFoundException() {
        // Arrange
        long usIdConsulta = 1L;
        long usIdRespuesta = 2L;

        SolHistorialDto solHistorialDto = new SolHistorialDto();
        solHistorialDto.setUsuarioIdAgente(usIdConsulta);
        solHistorialDto.setUsuarioIdCliente(usIdRespuesta);

        Usuario usConsultaMock = new Usuario(1L, TipoUsuarioEnum.AGENTE_VENTAS, "Sole", 8810);

        when(usuarioRepository.findById(usIdConsulta)).thenReturn(Optional.of(usConsultaMock));
        when(usuarioRepository.findById(usIdRespuesta)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            flyService.getHistorial(solHistorialDto);
        });
    }

    @Test
    @DisplayName("US4-Ex4: Usuario sobre quien se consulta no es CLIENTE")
    public void testEx4_ThrowsNotFoundException() {
        // Arrange
        long usIdConsulta = 1L;
        long usIdRespuesta = 2L;

        SolHistorialDto solHistorialDto = new SolHistorialDto();
        solHistorialDto.setUsuarioIdAgente(usIdConsulta);
        solHistorialDto.setUsuarioIdCliente(usIdRespuesta);

        Usuario usConsultaMock = new Usuario(1L, TipoUsuarioEnum.AGENTE_VENTAS, "Sole", 8810);
        Usuario usRespuestaMock = new Usuario(2L, TipoUsuarioEnum.ADMINISTRADOR,"Diego",8810);

        //3-Definición del comportamiento del método Mock
        when(usuarioRepository.findById(usIdConsulta)).thenReturn(Optional.of(usConsultaMock));
        when(usuarioRepository.findById(usIdRespuesta)).thenReturn(Optional.of(usRespuestaMock));

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            flyService.getHistorial(solHistorialDto);
        });
    }

    @Test
    @DisplayName("US4-Ex5: Cliente no tiene vuelos confirmados")
    public void testEx5_ThrowsNotFoundException() {
        // Arrange
        long usIdConsulta = 1L;
        long usIdRespuesta = 2L;

        SolHistorialDto solHistorialDto = new SolHistorialDto();
        solHistorialDto.setUsuarioIdAgente(usIdConsulta);
        solHistorialDto.setUsuarioIdCliente(usIdRespuesta);

        Usuario usConsultaMock = new Usuario(1L, TipoUsuarioEnum.AGENTE_VENTAS,"Max Power",8810);
        Usuario usRespuestaMock = new Usuario(2L,TipoUsuarioEnum.CLIENTE,"Pechugas Laru",9412);

        List<Reserva> todasReservasUserTrueMock = new ArrayList<>();
        todasReservasUserTrueMock.add(new Reserva(1L,usRespuestaMock,null, 1L,false,new Date(2023,8,1),1500,null));
        todasReservasUserTrueMock.add(new Reserva(3L,usRespuestaMock,null, 2L,false,new Date(2023,8,3),1300,null));

        when(usuarioRepository.findById(usIdConsulta)).thenReturn(Optional.of(usConsultaMock));
        when(usuarioRepository.findById(usIdRespuesta)).thenReturn(Optional.of(usRespuestaMock));

        when(reservaRepository.findByUsuario(usRespuestaMock)).thenReturn(todasReservasUserTrueMock);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            flyService.getHistorial(solHistorialDto);
        });

    }
}

