package CaC.Grupo2.FlySky.service;

import CaC.Grupo2.FlySky.dto.request.*;
import CaC.Grupo2.FlySky.dto.response.*;
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
import static CaC.Grupo2.FlySky.entity.usuario.TipoUsuarioEnum.ADMINISTRADOR;
import static CaC.Grupo2.FlySky.entity.usuario.TipoUsuarioEnum.CLIENTE;
import CaC.Grupo2.FlySky.helperDate.DateFormatHelper;


@Service
@Component
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
        vuelos.forEach(c-> vuelosDto.add(mapper.map(c, VueloDto.class)));

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
                .orElseThrow(() -> new NotFoundException("El usuario no existe"));

        if(usuario.getTipoUsuario() !=CLIENTE ){
            throw new IllegalArgumentException("Por favor Registrese para reservar un vuelo");
        }

        Vuelo vueloExistente = flyRepository.findById(reservaDto.getVueloID())
                .orElseThrow(() -> new IllegalArgumentException("No se encontro el vuelo con el ID especificado"));

        if(vueloExistente.getFecha().before(fechaActual)){
            throw new IllegalArgumentException("el vuelo que intenta reserva su fecha ya caduco");
        }

        List<Asiento> asientosReservados = new ArrayList<>();
        for (AsientoDto asientoDto : reservaDto.getAsientos()) {
            Asiento asientoExistente = asientoRepository.findById(asientoDto.getAsientoID())
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró el asiento con el ID especificado"));

            Vuelo  vuelo=asientoExistente.getVuelo();
            if(!Objects.equals(vuelo.getVueloID(), reservaDto.getVueloID())){
                throw new IllegalArgumentException("el asiento que intenta reservar no pertenece a este Vuelo");
            }

            if(asientoExistente.isOcupado()){
                throw new IllegalArgumentException("el asiento que intenta reservar ya se encuentra ocupado");
            }

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
        resp.setMensaje("Su reserva se realizo con exito... Tienes 10 minutos para realizar el pago");
        return resp;

    }

    // Tarea programada para liberar las reservas expirados no pagadas
    @Scheduled(fixedDelay = 10000) //600000 cada 10 minutos
    public void liberarReservasExpiradas() {
        List<Reserva> reservas =reservaRepository.findAll();

        for(Reserva reserva: reservas){
            if(haPasadoTiempoLimiteDePago(reserva) && !reserva.isReservaConfirmada()){
                List<Asiento> asientos = reserva.getAsientos();
                for (Asiento asiento : asientos) {
                    asiento.setPasajero(null);
                    asiento.setOcupado(false);
                    asiento.setUbicacion(null);
                    asientoRepository.save(asiento);
                }
            }

        }

    }

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
                .orElseThrow(() -> new NotFoundException("No se encontro la reserva con el ID especificado"));

        if(reservaExistente.isReservaConfirmada()){
           throw new IllegalArgumentException("ya se realizo el pago de esta reserva");
        }

        if(haPasadoTiempoLimiteDePago(reservaExistente)){
            throw new IllegalArgumentException("El tiempo maximos para pagar vencio, por favor realice otra reserva");
        }

        if(pagoDto.getMonto() != reservaExistente.getMonto()){
            throw new IllegalArgumentException("No ingreso el monto correcto");
        }

        Pago pago = new Pago();
        pago.setPagado(true);
        pago.setTipoPago(pagoDto.getTipoPago());
        pago.setFechaPago(fechaActual);
        pago.setMonto(pagoDto.getMonto());
        pago.setReserva(reservaExistente);

        Pago persistPago = pagoRepository.save(pago);

        reservaExistente.setReservaConfirmada(true);
        reservaRepository.save(reservaExistente);

        return "Reserva pagada exitosamente";
    }

    @Override
    public RtaHistorialDto getHistorial(SolHistorialDto solHistorialDto){

        Optional<Usuario> usuarioCta = usuarioRepository.findById(solHistorialDto.getUsuarioIdAgente());
        if (usuarioCta.isEmpty() ) {
            throw new NotFoundException("ERROR!: El usuario que solicita la información no existe en el sistema");
        }
        if (usuarioCta.get().getTipoUsuario()!= TipoUsuarioEnum.AGENTE_VENTAS){
            throw new NotFoundException("ERROR!: Usted no es Agente de ventas, no puede realizar esta consulta");
        }

        Optional<Usuario> usuarioRta = usuarioRepository.findById(solHistorialDto.getUsuarioIdCliente());
        if (usuarioRta.isEmpty() ) {
            throw new NotFoundException("ERROR!: El usuario consultado no existe en el sistema");
        }
        if(usuarioRta.get().getTipoUsuario()!=TipoUsuarioEnum.CLIENTE){
            throw new NotFoundException("ERROR!: El usuario por el que se quiere consultar no es cliente");
        }


        Usuario usuarioSolicitado = usuarioRta.get();
        List<Reserva> histReservas = reservaRepository.findByUsuario(usuarioSolicitado);

        List<Reserva> histReservasTrue = histReservas.stream().filter(Reserva::isReservaConfirmada).collect(Collectors.toList());


        if (histReservasTrue.isEmpty() ) {
            throw new NotFoundException("ERROR!: Al momento, este cliente no ha confirmado ningún vuelo");
        }


        List<Vuelo> respTodosVuelos = flyRepository.findAll();
        List<Vuelo> histVueloCli = respTodosVuelos.stream()
                .filter(e->histReservasTrue.stream()
                        .anyMatch(e2-> Objects.equals(e2.getVueloID(), e.getVueloID())))
                .collect(Collectors.toList());

        ModelMapper mapperUs4 = new ModelMapper();

        // Creamos una configuración personalizada para el mapeo
        mapperUs4.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        mapperUs4.createTypeMap(Vuelo.class, VueloDtoSA.class)
                //.addMappings(mapping -> mapping.map(Vuelo::getFecha, (dest, value) -> dest.setFecha(formato2.format(value))))
                .addMapping(Vuelo::getFecha, VueloDtoSA::setFecha)
                .addMapping(Vuelo::getAerolinea,VueloDtoSA::setAerolinea)
                .addMapping(Vuelo::getOrigen,VueloDtoSA::setOrigen)
                .addMapping(Vuelo::getDestino,VueloDtoSA::setDestino);


        // Creamos una lista de VueloDtoSA a partir de la lista de Vuelo utilizando el mapeo personalizado
        List<VueloDtoSA> resultados = histVueloCli.stream()
                .map(h -> mapperUs4.map(h, VueloDtoSA.class))
                .collect(Collectors.toList());

        RtaHistorialDto respUs4= new RtaHistorialDto();
        respUs4.setVuelosUsuarios(resultados);
        respUs4.setMensaje("Historial y Preferencias de Vuelo del Cliente "+usuarioRta.get().getNombreCompletoUsuario());
        return respUs4;
    }

    @Override
    public RespVentasDiarias getVentasDiarias(SolVentasDiariasDto solVentasDiarias) {

        Optional<Usuario> usuario = usuarioRepository.findById(solVentasDiarias.getUsuarioIdAdministrador());
        if (usuario.isEmpty() ) {
            throw new NotFoundException("El usuario no existe");
        }
        if (usuario.get().getTipoUsuario()!= ADMINISTRADOR){
            throw new IllegalArgumentException("Usted no es ADMINISTRADOR, no puede realizar la consulta");
        }

        List<Pago> pagos = pagoRepository.findAll();

        DateFormatHelper date = new DateFormatHelper();

        double ingresosDiarios = 0.0;
        int totalpagos=0;
        for(Pago pago :pagos){
            if(Objects.equals(date.fechaStirng(new Date()), date.fechaStirng(pago.getFechaPago())) && pago.isPagado()){
                ingresosDiarios+=pago.getMonto();
                totalpagos+=1;
            }
        }

        RespVentasDiarias resp= new RespVentasDiarias();
        resp.setTotalVentas(totalpagos);
        resp.setIngresosGenerados(ingresosDiarios);


        return resp;

    }


}
