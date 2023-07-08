package CaC.Grupo2.FlySky.service;

import CaC.Grupo2.FlySky.dto.*;
import CaC.Grupo2.FlySky.entity.Asiento;
import CaC.Grupo2.FlySky.entity.Reserva;
import CaC.Grupo2.FlySky.entity.usuario.TipoUsuarioEnum;
import CaC.Grupo2.FlySky.entity.usuario.Usuario;
import CaC.Grupo2.FlySky.entity.Vuelo;
import CaC.Grupo2.FlySky.exception.NotFoundException;
import CaC.Grupo2.FlySky.exception.IllegalArgumentException;
import CaC.Grupo2.FlySky.repository.AsientoRepository;
import CaC.Grupo2.FlySky.repository.FlyRepository;
import CaC.Grupo2.FlySky.repository.ReservaRepository;
import CaC.Grupo2.FlySky.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
//@Qualifier("flyService")
public class FlyService implements IFlyService{

    FlyRepository flyRepository;
    ReservaRepository reservaRepository;
    AsientoRepository asientoRepository;

    UsuarioRepository usuarioRepository;

    public FlyService(FlyRepository flyRepository, ReservaRepository reservaRepository, AsientoRepository asientoRepository, UsuarioRepository usuarioRepository) {
        this.flyRepository = flyRepository;
        this.reservaRepository = reservaRepository;
        this.asientoRepository = asientoRepository;
        this.usuarioRepository = usuarioRepository;
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

    //US4: Agente de vtas accede al historial de reservas de un cliente dado
    @Override
    public RtaHistorialDto getHistorial(SolHistorialDto solHistorialDto){

        Optional<Usuario> usuarioCta = usuarioRepository.findById(solHistorialDto.getUsuarioIdConsulta());
        if (usuarioCta.isEmpty() ) {
            throw new NotFoundException("ERROR: No encuentro USUARIO en el sistema");
        }
        if (usuarioCta.get().getTipoUsuario()!= TipoUsuarioEnum.AGENTE_VENTAS){
            throw new NotFoundException("ERROR: Usted no es Agente de ventas, no puede realizar la consulta");
        }

        System.out.println(usuarioCta);

        //Preguntar si el usuario es agente

        Optional<Usuario> usuarioRta = usuarioRepository.findById(solHistorialDto.getUsuarioIdRespuenta());
        if (usuarioRta.isEmpty() ) {
            throw new NotFoundException("ERROR: No encuentro a ese USUARIO en el sistema");
        }
        if(usuarioRta.get().getTipoUsuario()!=TipoUsuarioEnum.CLIENTE){
            throw new NotFoundException("ERROR: El usuario por el que se quiere consultar no es cliente");
        }
        System.out.println(usuarioRta);

        List<Reserva> respTodasReservas = reservaRepository.findAll();

        System.out.println("ESTAS TODAS LAS RESERVAS");
        System.out.println(respTodasReservas);
        System.out.println(respTodasReservas.getClass().getSimpleName());


        List<Reserva> histReservas = respTodasReservas.stream()
                                    .filter(e->e.getUsuario().getUsuarioID().equals(solHistorialDto.getUsuarioIdRespuenta()))
                                    .collect(Collectors.toList());
        //System.out.println(histReservas.get(0).isEstadoReserva());
        //System.out.println(histReservas.get(1).isEstadoReserva());
        //System.out.println(histReservas.get(2).isEstadoReserva());
        //System.out.println(histReservas.getClass().getSimpleName());

        List<Reserva> histReservasTrue = histReservas.stream().filter(Reserva::isEstadoReserva).collect(Collectors.toList());
        //System.out.println(histReservasTrue);
        //System.out.println(histReservas.get(0).isEstadoReserva());

        if (histReservasTrue.isEmpty() ) {
            throw new NotFoundException("ERROR: Ese cliente no realizó ningún vuelo");
        }
        //System.out.println(histReservas.getClass().getSimpleName());
        //el usuario no tiene vuelos

        ModelMapper mapperUs4 = new ModelMapper();
        List<ReservaDto> histReservaDto = new ArrayList<>();
        histReservasTrue.forEach(c-> histReservaDto.add(mapperUs4.map(c,ReservaDto.class)));

        RtaHistorialDto respUs4= new RtaHistorialDto();
        respUs4.setReservaDto(histReservaDto);
        respUs4.setMensaje("Historial y Preferencias de Vuelo del Cliente");
        return respUs4;
        }

}
