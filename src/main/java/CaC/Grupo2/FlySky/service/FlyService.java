package CaC.Grupo2.FlySky.service;

import CaC.Grupo2.FlySky.dto.*;
import CaC.Grupo2.FlySky.entity.Asiento;
import CaC.Grupo2.FlySky.entity.Pago.Pago;
import CaC.Grupo2.FlySky.entity.Reserva;
import CaC.Grupo2.FlySky.entity.usuario.Usuario;
import CaC.Grupo2.FlySky.entity.Vuelo;
import CaC.Grupo2.FlySky.exception.NotFoundException;
import CaC.Grupo2.FlySky.exception.IllegalArgumentException;
import CaC.Grupo2.FlySky.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static CaC.Grupo2.FlySky.entity.usuario.TipoUsuarioEnum.CLIENTE;

@Service
//@Qualifier("flyService")
public class FlyService implements IFlyService{

    FlyRepository flyRepository;
    ReservaRepository reservaRepository;
    AsientoRepository asientoRepository;
    PagoRepository pagoRepository;
    UsuarioRepository usuarioRepository;

    public FlyService( FlyRepository flyRepository, ReservaRepository reservaRepository,AsientoRepository asientoRepository,PagoRepository pagoRepository,UsuarioRepository usuarioRepository) {
        this.flyRepository = flyRepository;
        this.reservaRepository = reservaRepository;
        this.asientoRepository = asientoRepository;
        this.pagoRepository =pagoRepository;
        this.usuarioRepository =usuarioRepository;
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

        Usuario usuario = usuarioRepository.findById(reservaDto.getUsuarioID())
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));

        if(usuario.getTipoUsuario() !=CLIENTE ){
            throw new IllegalArgumentException("Por favor Registrese para reservar un vuelo");
        }

        Vuelo vueloExistente = flyRepository.findById(reservaDto.getVueloID())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el vuelo con el ID especificado"));

        if(vueloExistente.getFecha().before(fechaActual)){
            throw new IllegalArgumentException("el vuelo que intenta reserva su fecha ya caduco");
        }

        List<Asiento> asientosReservados = new ArrayList<>();

        for (AsientoDto asientoDto : reservaDto.getAsientos()) {
            Asiento asientoExistente = asientoRepository.findById(asientoDto.getAsientoID())
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró el asiento con el ID especificado"));

            if(asientoExistente.isOcupado()){
                throw new IllegalArgumentException("el asiento ya se encuentra ocupado");
            }
            //asientoExistente.setNombreAsiento(asientoDto.getNombreAsiento());
            asientoExistente.setPasajero(asientoDto.getPasajero());
            asientoExistente.setOcupado(true);
            asientoExistente.setUbicacion(asientoDto.getUbicacion());

            asientosReservados.add(asientoExistente);
        }

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setAsientos(asientosReservados);
        reserva.setFechaReserva(fechaActual);
        reserva.setVueloID(reservaDto.getVueloID());

        Reserva persistReserva = reservaRepository.save(reserva);

        RespReservaDto resp = new RespReservaDto();

        SimpleDateFormat fechaHora= new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String fechaHoraActual=fechaHora.format(persistReserva.getFechaReserva());

        ReservaDto reservaDto1= new ReservaDto();

        reservaDto1.setNumeroReserva(persistReserva.getReservaID());
        reservaDto1.setUsuarioID(persistReserva.getUsuario().getUsuarioID());

        List<AsientoDto> asientosDto = new ArrayList<>();

        for (Asiento asiento : persistReserva.getAsientos()) {
            AsientoDto asientoDto = modelMapper.map(asiento, AsientoDto.class);
            asientosDto.add(asientoDto);
        }

        reservaDto1.setAsientos(asientosDto);
        reservaDto1.setFechaReserva(fechaHoraActual);
        reservaDto1.setVueloID(persistReserva.getVueloID());

        resp.setReserva(reservaDto1);
        resp.setMonto(asientosReservados.size() * vueloExistente.getPrecio());
        resp.setMensaje("Su reserva se realizó con éxito...");
        return resp;
    }

/*
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
    /*
        List<ReservaDto> reservaDto = new ArrayList<>();
        reservaEnt.forEach(c-> reservaDto.add(mapper.map(c,ReservaDto.class)));
        return reservaDto;
    }



     */
    @Override
    public String pagarReserva(PagoDto pagoDto) {
        ModelMapper modelMapper = new ModelMapper();
        Date fechaActual = new Date();

        Reserva reservaExistente = reservaRepository.findById(pagoDto.getReservaID())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la reserva con el ID especificado"));

        Pago pago= reservaExistente.getPago();

        if(pagoDto.getMonto() != pago.getMonto()){
            throw new NotFoundException("No ingreso el monto correcto");
        }

        pago.setPagado(true);
        pago.setTipoPago(pagoDto.getTipoPago());
        pago.setFechaPago(fechaActual);
        pago.setMonto(pago.getMonto());
        pago.setReserva(reservaExistente);

        Pago persistPago = pagoRepository.save(pago);

        return "Reserva pagada exitosamente";
    }


}
