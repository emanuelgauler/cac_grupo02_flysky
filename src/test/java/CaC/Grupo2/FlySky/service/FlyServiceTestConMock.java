package CaC.Grupo2.FlySky.service;


import CaC.Grupo2.FlySky.dto.RtaHistorialDto;
import CaC.Grupo2.FlySky.dto.SolHistorialDto;
import CaC.Grupo2.FlySky.dto.VueloDtoSA;
import CaC.Grupo2.FlySky.entity.Reserva;
import CaC.Grupo2.FlySky.entity.Vuelo;
import CaC.Grupo2.FlySky.entity.usuario.TipoUsuarioEnum;
import CaC.Grupo2.FlySky.entity.usuario.Usuario;
import CaC.Grupo2.FlySky.exception.NotFoundException;
import CaC.Grupo2.FlySky.repository.FlyRepository;
import CaC.Grupo2.FlySky.repository.ReservaRepository;
import CaC.Grupo2.FlySky.repository.UsuarioRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class FlyServiceTestConMock {


    @Mock
    FlyRepository flyRepository;
    @Mock
    ReservaRepository reservaRepositoryConMock;
    @Mock
    UsuarioRepository usuarioRepositoryConMock;
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
    @DisplayName("US4-Camino Feliz")
    void getHistorialOKTest(){
        //ARRANGE
        //1-Creacion de los Objetos Mocks
        long usIdConsulta = 1;
        long usIdRespuesta = 2;

        SolHistorialDto solHistorialDtoMock = new SolHistorialDto();
        solHistorialDtoMock.setUsuarioIdConsulta(usIdConsulta);
        solHistorialDtoMock.setUsuarioIdRespuenta(usIdRespuesta);

        Usuario usConsultaMock = new Usuario(1L, TipoUsuarioEnum.AGENTE_VENTAS,"Max Power",8810);
        Usuario usRespuestaMock = new Usuario(2L,TipoUsuarioEnum.CLIENTE,"Pechugas Laru",9412);

        List<Reserva> todasReservasMock = new ArrayList<>();
        todasReservasMock.add(new Reserva(1L,usRespuestaMock,null, 1L,true,new Date(2023,8,1),1500,null));
        todasReservasMock.add(new Reserva(2L,usRespuestaMock,null, 2L,false,new Date(2023,8,2),1400,null));
        todasReservasMock.add(new Reserva(3L,usRespuestaMock,null, 2L,true,new Date(2023,8,3),1300,null));
        todasReservasMock.add(new Reserva(4L,usConsultaMock,null, 1L,true,new Date(2023,8,4),1200,null));

        List<Reserva> todasReservasUserMock = new ArrayList<>();
        todasReservasUserMock.add(new Reserva(1L,usRespuestaMock,null, 1L,true,new Date(2023,8,1),1500,null));
        todasReservasUserMock.add(new Reserva(2L,usRespuestaMock,null, 2L,false,new Date(2023,8,2),1400,null));
        todasReservasUserMock.add(new Reserva(3L,usRespuestaMock,null, 2L,true,new Date(2023,8,3),1300,null));

        List<Reserva> todasReservasUserTrueMock = new ArrayList<>();
        todasReservasUserTrueMock.add(new Reserva(1L,usRespuestaMock,null, 1L,true,new Date(2023,8,1),1500,null));
        todasReservasUserTrueMock.add(new Reserva(3L,usRespuestaMock,null, 2L,true,new Date(2023,8,3),1300,null));

        List<Vuelo> todosVuelosMock = new ArrayList<>();
        todosVuelosMock.add(new Vuelo(1L,null,"Buenos Aires","Miami",new Date(2023,9,1),1500.5,false,"AA"));
        todosVuelosMock.add(new Vuelo(2L,null,"Buenos Aires","Roma",new Date(2023,9,2),1400.5,false,"Latam"));
        todosVuelosMock.add(new Vuelo(3L,null,"Buenos Aires","Toronto",new Date(2023,9,3),1300.5,false,"AA"));

        List<Vuelo> todosVuelosCliMock = new ArrayList<>();
        todosVuelosCliMock.add(new Vuelo(1L,null,"Buenos Aires","Miami",new Date(2023,9,1),1500.5,false,"AA"));
        todosVuelosCliMock.add(new Vuelo(2L,null,"Buenos Aires","Roma",new Date(2023,9,2),1400.5,false,"Latam"));

        List<VueloDtoSA> vueloDtoSAMock = new ArrayList<>();
        vueloDtoSAMock.add(new VueloDtoSA("Buenos Aires","Miami","AA",new Date(2023,9,1)));
        vueloDtoSAMock.add(new VueloDtoSA("Buenos Aires","Roma","Latam",new Date(2023,9,2)));

        //2-Creación del valor Esperado
        RtaHistorialDto expected = new RtaHistorialDto("Historial y Preferencias de Vuelo del Cliente Pechugas Laru",vueloDtoSAMock);

        //3-Definición del comportamiento del método Mock
        when(usuarioRepositoryConMock.findById(usIdConsulta)).thenReturn(Optional.of(usConsultaMock));
        when(usuarioRepositoryConMock.findById(usIdRespuesta)).thenReturn(Optional.of(usRespuestaMock));

        //when(reservaRepositoryConMock.findAll()).thenReturn(todasReservasMock);
        when(reservaRepositoryConMock.findByUsuario(usRespuestaMock)).thenReturn(todasReservasUserTrueMock);

        when(flyRepository.findAll()).thenReturn(todosVuelosMock);

        //4-Act
        RtaHistorialDto result = flyService.getHistorial(solHistorialDtoMock);

        //5-Assert
        assertEquals(expected,result);

    }

}
