package CaC.Grupo2.FlySky.service;

import CaC.Grupo2.FlySky.dto.*;
import CaC.Grupo2.FlySky.entity.Asiento;
import CaC.Grupo2.FlySky.entity.Reserva;
import CaC.Grupo2.FlySky.entity.usuario.Usuario;
import CaC.Grupo2.FlySky.entity.Vuelo;
import CaC.Grupo2.FlySky.exception.NotFoundException;
import CaC.Grupo2.FlySky.exception.IllegalArgumentException;
import CaC.Grupo2.FlySky.repository.AsientoRepository;
import CaC.Grupo2.FlySky.repository.FlyRepository;
import CaC.Grupo2.FlySky.repository.ReservaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
//@Qualifier("flyService")
public class FlyService implements IFlyService{

    FlyRepository flyRepository;
    ReservaRepository reservaRepository;
    AsientoRepository asientoRepository;

    public FlyService( FlyRepository flyRepository, ReservaRepository reservaRepository,AsientoRepository asientoRepository) {
        this.flyRepository = flyRepository;
        this.reservaRepository = reservaRepository;
        this.asientoRepository = asientoRepository;
    }


    @Override
    public List<VueloDto> buscarTodosVuelos() {
        ModelMapper mapper = new ModelMapper();

        List<Vuelo> vuelosEnt = flyRepository.findAll();

        if (vuelosEnt.isEmpty()) {
            throw new NotFoundException("la lista de vuelos esta vacía");
        }
        List<Vuelo> vuelos = new ArrayList<>();
        Date fechaActual= new Date();
        for (Vuelo vuelo : vuelosEnt){
            Date fechaVuelo= vuelo.getFecha();

            if(fechaVuelo.after(fechaActual) && checkAsiento(vuelo) ){
                   vuelos.add(vuelo);
            }

        }
        List<VueloDto> vuelosDto = new ArrayList<>();
        vuelos.forEach(c-> vuelosDto.add(mapper.map(c,VueloDto.class)));

        return vuelosDto;
    }

    private boolean checkAsiento(Vuelo vuelo) {
        for(Asiento asiento : vuelo.getAsientos()){
                if(!asiento.isOcupado()){
                    return true;
                }
        }
        return false;
    }

    @Override
    public RespReservaDto reservarVuelo(ReservaDto reservaDto)  {
        Date fechaActual = new Date();
        ModelMapper modelMapper = new ModelMapper();

        Usuario usuario = modelMapper.map(reservaDto.getUsuario(), Usuario.class);
        Vuelo vueloExistente = flyRepository.findById(reservaDto.getVuelo().getVueloID())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el vuelo con el ID especificado"));

        List<Asiento> asientosReservados = new ArrayList<>();

        for (AsientoDto asientoDto : reservaDto.getVuelo().getAsientos()) {
            Asiento asientoExistente = asientoRepository.findById(asientoDto.getAsientoID())
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró el asiento con el ID especificado"));

            if(asientoExistente.isOcupado()){
                throw new IllegalArgumentException("el asiento ya se encuentra ocupado");
            }
            asientoExistente.setNombreAsiento(asientoDto.getNombreAsiento());
            asientoExistente.setPasajero(asientoDto.getPasajero());
            asientoExistente.setOcupado(true);
            asientoExistente.setUbicacion(asientoDto.getUbicacion());

            asientosReservados.add(asientoExistente);
        }

        vueloExistente.setAsientos(asientosReservados);

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setVuelo(vueloExistente);
        reserva.setFechaReserva(fechaActual);

        Reserva persistReserva = reservaRepository.save(reserva);

        RespReservaDto resp = new RespReservaDto();

        SimpleDateFormat fechaHora= new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String fechaHoraActual=fechaHora.format(persistReserva.getFechaReserva());

        ReservaDto reservaDto1= new ReservaDto();

        reservaDto1.setNumeroReserva(persistReserva.getReservaID());
        reservaDto1.setUsuario(modelMapper.map(persistReserva.getUsuario(), UsuarioDto.class));
        reservaDto1.setVuelo(modelMapper.map(persistReserva.getVuelo(), VueloDto.class));
        reservaDto1.setFechaReserva(fechaHoraActual);

        resp.setReserva(reservaDto1);
        resp.setMensaje("Su reserva se realizó con éxito...");
        return resp;
    }


    @Override
    public List<ReservaDto> buscarTodasReservas() {
        ModelMapper mapper = new ModelMapper();

        List<Reserva> reservaEnt = reservaRepository.findAll();

        if (reservaEnt.isEmpty() ) {
            throw new NotFoundException("la lista de reserva esta vacía");
        }
        /*
        reservaEnt.stream().filter(reserva -> {
            reserva.getVuelo().
        });


         */
        List<ReservaDto> reservaDto = new ArrayList<>();
        reservaEnt.forEach(c-> reservaDto.add(mapper.map(c,ReservaDto.class)));
        return reservaDto;
    }


}
