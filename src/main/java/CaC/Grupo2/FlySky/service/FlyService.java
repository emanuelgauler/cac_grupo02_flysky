package CaC.Grupo2.FlySky.service;

import CaC.Grupo2.FlySky.dto.*;
import CaC.Grupo2.FlySky.entity.Asiento;
import CaC.Grupo2.FlySky.entity.Pago.Pago;
import CaC.Grupo2.FlySky.entity.Reserva;
import CaC.Grupo2.FlySky.entity.usuario.TipoUsuarioEnum;
import CaC.Grupo2.FlySky.entity.usuario.Usuario;
import CaC.Grupo2.FlySky.entity.Vuelo;
import CaC.Grupo2.FlySky.exception.NotFoundException;
import CaC.Grupo2.FlySky.exception.IllegalArgumentException;
import CaC.Grupo2.FlySky.repository.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static CaC.Grupo2.FlySky.entity.usuario.TipoUsuarioEnum.CLIENTE;

@Service
@Component
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
            setFechaExpiracionAsiento(asientoExistente,10);
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
        reserva.setMonto(asientosReservados.size() * vueloExistente.getPrecio());

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
        resp.setMensaje("Su reserva se realizó con éxito... Tienes 10 minutos para realizar el pago");
        return resp;

    }

    public void setFechaExpiracionAsiento(Asiento asiento, int duracionReservaEnMin) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, duracionReservaEnMin); // Duración de la reserva en minutos
        Date fechaExpiracion = calendar.getTime();
        asiento.setFechaExpiracion(fechaExpiracion);
    }


    // Tarea programada para liberar los asientos expirados
    @Scheduled(fixedDelay = 10000) //600000 cada 10 minutos
    public void liberarAsientosExpirados() {
        List<Asiento> asientos = asientoRepository.findAll();
        Date fechaActual = new Date();

        for (Asiento asiento : asientos) {
            if (asiento.getFechaExpiracion() != null && asiento.getFechaExpiracion().before(fechaActual)) {
                asiento.setFechaExpiracion(null);
                asiento.setPasajero(null);
                asiento.setOcupado(false);
                asiento.setUbicacion(null);
                asientoRepository.save(asiento);
            }
        }
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

    public boolean haPasadoTiempoLimiteDePago(Reserva reserva) {
        Date fechaActual = new Date();
        Date fechaCreacion = reserva.getFechaReserva();
        long tiempoTranscurrido = fechaActual.getTime() - fechaCreacion.getTime();
        long minutosTranscurridos = TimeUnit.MINUTES.convert(tiempoTranscurrido, TimeUnit.MILLISECONDS);
        return minutosTranscurridos >= 10;
    }
    @Override
    public String pagarReserva(PagoDto pagoDto) {
        Date fechaActual = new Date();

        Reserva reservaExistente = reservaRepository.findById(pagoDto.getReservaID())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la reserva con el ID especificado"));

        if(reservaExistente.isEstadoReserva()){
           throw new IllegalArgumentException("ya se realizo el pago de esta reserva");
        }

        if(haPasadoTiempoLimiteDePago(reservaExistente)){
            throw new IllegalArgumentException("El tiempo maximos para pagar vencio, por favor realice otra reserva");
        }

        if(pagoDto.getMonto() != reservaExistente.getMonto()){
            throw new IllegalArgumentException("No ingreso el monto correcto");
        }

        //Falta Validar Tipo de Pago!!!

        Pago pago = new Pago();

        pago.setPagado(true);
        pago.setTipoPago(pagoDto.getTipoPago());
        pago.setFechaPago(fechaActual);
        pago.setMonto(pagoDto.getMonto());
        pago.setReserva(reservaExistente);

        Pago persistPago = pagoRepository.save(pago);

        reservaExistente.setEstadoReserva(true);
        List<Asiento> asientos = reservaExistente.getAsientos();

        for (Asiento asiento : asientos) {
            if (asiento.getFechaExpiracion() != null){
                asiento.setFechaExpiracion(null);
                asientoRepository.save(asiento);
            }
        }

        reservaRepository.save(reservaExistente);

        return "Reserva pagada exitosamente";
    }

    @Override
    public RtaHistorialDto getHistorial(SolHistorialDto solHistorialDto){

        Optional<Usuario> usuarioCta = usuarioRepository.findById(solHistorialDto.getUsuarioIdConsulta());
        if (usuarioCta.isEmpty() ) {
            throw new NotFoundException("ERROR: No encuentro USUARIO en el sistema");
        }
        if (usuarioCta.get().getTipoUsuario()!= TipoUsuarioEnum.AGENTE_VENTAS){
            throw new NotFoundException("ERROR: Usted no es Agente de ventas, no puede realizar la consulta");
        }


        Optional<Usuario> usuarioRta = usuarioRepository.findById(solHistorialDto.getUsuarioIdRespuenta());
        if (usuarioRta.isEmpty() ) {
            throw new NotFoundException("ERROR: No encuentro a ese USUARIO en el sistema");
        }
        if(usuarioRta.get().getTipoUsuario()!=TipoUsuarioEnum.CLIENTE){
            throw new NotFoundException("ERROR: El usuario por el que se quiere consultar no es cliente");
        }
        //System.out.println(usuarioRta);

        List<Reserva> respTodasReservas = reservaRepository.findAll();

        List<Reserva> histReservas = respTodasReservas.stream()
                .filter(e->e.getUsuario().getUsuarioID().equals(solHistorialDto.getUsuarioIdRespuenta()))
                .collect(Collectors.toList());


        List<Reserva> histReservasTrue = histReservas.stream().filter(Reserva::isEstadoReserva).collect(Collectors.toList());
        //System.out.println(histReservasTrue.get(0).getReservaID());
        //System.out.println(histReservasTrue.get(0).isEstadoReserva());
        //System.out.println(histReservasTrue.get(0).getVueloID());


        if (histReservasTrue.isEmpty() ) {
            throw new NotFoundException("ERROR: Ese cliente no realizó ningún vuelo");
        }


        List<Vuelo> respTodosVuelos = flyRepository.findAll();
        List<Vuelo> histVueloCli = respTodosVuelos.stream()
                .filter(e->histReservasTrue.stream()
                        .anyMatch(e2-> Objects.equals(e2.getVueloID(), e.getVueloID())))
                .collect(Collectors.toList());

        //System.out.println(histVueloCli.get(0).getOrigen());


        //return null;

        ModelMapper mapperUs4 = new ModelMapper();

        // Creamos una configuración personalizada para el mapeo
        mapperUs4.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        /*mapperUs4.createTypeMap(Reserva.class, VueloDtoSA.class)
                .addMapping(Reserva::getUsuario, VueloDtoSA::setUsuario);*/

        mapperUs4.createTypeMap(Vuelo.class, VueloDtoSA.class)
                .addMapping(Vuelo::getVueloID, VueloDtoSA::setVueloID)
                .addMapping(Vuelo::getAerolinea,VueloDtoSA::setAerolinea)
                .addMapping(Vuelo::getOrigen,VueloDtoSA::setOrigen)
                .addMapping(Vuelo::getDestino,VueloDtoSA::setDestino);


        // Creamos una lista de VueloDtoSA a partir de la lista de Vuelo utilizando el mapeo personalizado
        List<VueloDtoSA> resultados = histVueloCli.stream()
                .map(h -> mapperUs4.map(h, VueloDtoSA.class))
                .collect(Collectors.toList());

        /*List<VueloDtoSA> resultados = Stream.concat(
                histReservasTrue.stream().map(h -> mapperUs4.map(h, VueloDtoSA.class)),
                histVueloCli.stream().map(h -> mapperUs4.map(h, VueloDtoSA.class)))
                .collect(Collectors.toList());*/

        //List<VueloDto> histVueloDto = new ArrayList<>();
        //histVueloCli.forEach(c-> histVueloDto.add(mapperUs4.map(c,VueloDto.class)));

        //System.out.println(histReservaDto.get(0).getNumeroReserva());

        RtaHistorialDto respUs4= new RtaHistorialDto();
        respUs4.setVuelosUsuarios(resultados);
        respUs4.setMensaje("Historial y Preferencias de Vuelo del Cliente "+usuarioRta.get().getNombreCompletoUsuario());
        return respUs4;
    }


}
