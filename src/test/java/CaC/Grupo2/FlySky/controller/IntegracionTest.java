package CaC.Grupo2.FlySky.controller;

import CaC.Grupo2.FlySky.dto.request.SolVentasDiariasDto;
import CaC.Grupo2.FlySky.dto.response.ErrorDto;
import CaC.Grupo2.FlySky.dto.request.ReservaDto;
import CaC.Grupo2.FlySky.repository.*;
import CaC.Grupo2.FlySky.service.FlyService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.io.File;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class IntegracionTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    UsuarioRepository usuarioRepository;
    @Mock
    PagoRepository pagoRepository;
    @Mock
    ReservaRepository reservaRepository;
    @Mock
    FlyRepository flyRepository;

    @Mock
    AsientoRepository asientoRepository;

    @InjectMocks
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

    @Test
    void testvalidarConsultaVentaDiarias() throws Exception {
        SolVentasDiariasDto solventasDiariasDtoMock = new SolVentasDiariasDto();
        solventasDiariasDtoMock.setUsuarioIdAdministrador(1L);

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE,false)
                .writer();

        String payloadDto = writer.writeValueAsString(solventasDiariasDtoMock);

        MvcResult mvcResult = mockMvc.perform(get("/getVentasDiarias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadDto))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.totalVentas").value(1))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.ingresosGenerados").value(175.5))
                        .andReturn();
    }
    /*

    @Test
    void testvalidarConsultaVentaDiarias2() throws Exception {

        // arrange

        SolVentasDiarias solVentasDiarias = new SolVentasDiarias();
        solVentasDiarias.setUsuarioIdAdministrador(1L);

        Usuario usuarioAdmin = new Usuario();
        usuarioAdmin.setUsuarioID(1L);
        usuarioAdmin.setTipoUsuario(ADMINISTRADOR);

        List<Pago> pagos = new ArrayList<>();

        Pago pago1 = new Pago();
        pago1.setFechaPago(new Date());
        pago1.setPagado(true);
        pago1.setMonto(100.0);
        pagos.add(pago1);

        Pago pago2 = new Pago();
        pago2.setFechaPago(new Date());
        pago2.setPagado(true);
        pago2.setMonto(200.0);
        pagos.add(pago2);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioAdmin));
        when(pagoRepository.findAll()).thenReturn(pagos);


        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE,false)
                .writer();

        String payloadDto = writer.writeValueAsString(solVentasDiarias);

        MvcResult mvcResult = mockMvc.perform(get("/getVentasDiarias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadDto))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalVentas").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ingresosGenerados").value(175.5))
                .andReturn();
    }

     */


}
