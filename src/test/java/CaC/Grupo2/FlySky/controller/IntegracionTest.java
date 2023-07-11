package CaC.Grupo2.FlySky.controller;

import CaC.Grupo2.FlySky.dto.*;
import CaC.Grupo2.FlySky.entity.Reserva;
import CaC.Grupo2.FlySky.entity.usuario.TipoUsuarioEnum;
import CaC.Grupo2.FlySky.entity.usuario.Usuario;
import CaC.Grupo2.FlySky.repository.ReservaRepository;
import CaC.Grupo2.FlySky.repository.UsuarioRepository;
import CaC.Grupo2.FlySky.service.FlyService;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegracionTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    FlyService flyService;


    @Test
    void testGetAllVuelos() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/getAllVuelos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].origen").value("Mendoza"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].destino").value("Bs. As"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].precio").value(150.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].aerolinea").value("Jestmar"))
                .andReturn();

        assertEquals("application/json",mvcResult.getResponse().getContentType());
    }

    @Test
    void testReservaVuelo() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        String rutaArchivo = "src/test/java/CaC/Grupo2/FlySky/controller/jsonReservaVuelo.json";

        File archivoJson = new File(rutaArchivo);

        ReservaDto reservaDto = objectMapper.readValue(archivoJson, ReservaDto.class);

        String jsonPayload = objectMapper.writeValueAsString(reservaDto);

        MvcResult mvcResult = mockMvc.perform(post("/reservaVuelo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        assertEquals("application/json",mvcResult.getResponse().getContentType());
        String responseContent = mvcResult.getResponse().getContentAsString();
        JsonNode jsonResponse = objectMapper.readTree(responseContent);
        String mensaje = jsonResponse.get("mensaje").asText();
        assertEquals("Su reserva se realizo con exito... Tienes 10 minutos para realizar el pago",mensaje);
    }

    @Test
    void testReservaVueloNoExistente() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        ErrorDto errorDto = new ErrorDto("No se encontro el vuelo con el ID especificado");

        String rutaArchivo = "src/test/java/CaC/Grupo2/FlySky/controller/jsonReservaVueloNoExistete.json";

        File archivoJson = new File(rutaArchivo);

        ReservaDto reservaDto = objectMapper.readValue(archivoJson, ReservaDto.class);

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE,false)
                .writer();

        String jsonPayload = objectMapper.writeValueAsString(reservaDto);
        String errorExpected = writer.writeValueAsString(errorDto);

        MvcResult mvcResult = mockMvc.perform(post("/reservaVuelo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                        .andDo(print())
                        .andReturn();

        assertEquals(errorExpected,mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testPagarReserva(){


    }

    @BeforeEach
       public void setupGetHistorial(){
           Usuario usConsultaMock = new Usuario(1L, TipoUsuarioEnum.AGENTE_VENTAS,"Max Power",8810);
           Usuario usRespuestaMock = new Usuario(2L,TipoUsuarioEnum.CLIENTE,"Pechugas Laru",9412);

           usuarioRepository.save(usConsultaMock);
           usuarioRepository.save(usRespuestaMock);

        Reserva todasReservasUserTrueMock = new Reserva(1L,usRespuestaMock,null, 1L,true,new Date(2023,8,1),1500,null);

        reservaRepository.save(todasReservasUserTrueMock);

       }
    @Test
    void getHistorialTest() throws Exception {
        //Valor Recibido DTO
        SolHistorialDto solHistorialDtoI = new SolHistorialDto(1L,2L);

        //Act
        //Usuario usConsultaMock = new Usuario(2L, TipoUsuarioEnum.AGENTE_VENTAS,"Max Power",8810);
        //Usuario usRespuestaMock = new Usuario(3L,TipoUsuarioEnum.CLIENTE,"Pechugas Laru",9412);

        //when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usConsultaMock));
        //when(usuarioRepository.findById(3L)).thenReturn(Optional.of(usRespuestaMock));

        //List<Reserva> todasReservasUserTrueMock = new ArrayList<>();
        //todasReservasUserTrueMock.add(new Reserva(1L,usRespuestaMock,null, 1L,true,new Date(2023,8,1),1500,null));
        //todasReservasUserTrueMock.add(new Reserva(3L,usRespuestaMock,null, 2L,true,new Date(2023,8,3),1300,null));

        //when(reservaRepository.findByUsuario(usRespuestaMock)).thenReturn(todasReservasUserTrueMock);

        List<VueloDtoSA> vueloDtoSAI = new ArrayList<>();
        vueloDtoSAI.add(new VueloDtoSA("Mendoza","Bs. As","Jestmar",new Date(123,8,9)));

        RtaHistorialDto expected = new RtaHistorialDto("Historial y Preferencias de Vuelo del Cliente Pechugas Laru",vueloDtoSAI);

        //Transformo los objetos a Json
        ObjectWriter objToJson = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE,false)
                .writer();

        String jsonPayloadEntrada = objToJson.writeValueAsString(solHistorialDtoI);
        String JsonPayLoadSalida = objToJson.writeValueAsString(expected);
        //System.out.println(jsonPayloadEntrada);
        //System.out.println(JsonPayLoadSalida);

        //Act
        MvcResult mvcResult = mockMvc.perform(get("/getHistorial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayloadEntrada))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //Assert
       assertEquals(JsonPayLoadSalida,mvcResult.getResponse().getContentAsString());
    }


}
