package CaC.Grupo2.FlySky.service;


import CaC.Grupo2.FlySky.entity.Vuelo;
import CaC.Grupo2.FlySky.exception.NotFoundException;
import CaC.Grupo2.FlySky.repository.FlyRepository;
import CaC.Grupo2.FlySky.repository.ReservaRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class FlyServiceTestConMock {


    @Mock
    FlyRepository flyRepository;
    @Mock
    ReservaRepository reservaRepository;
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

}
