package CaC.Grupo2.FlySky.service;

import CaC.Grupo2.FlySky.dto.*;
import CaC.Grupo2.FlySky.entity.Asiento;
import CaC.Grupo2.FlySky.entity.Pago.Pago;
import CaC.Grupo2.FlySky.entity.Reserva;
import CaC.Grupo2.FlySky.entity.usuario.Usuario;
import CaC.Grupo2.FlySky.entity.Vuelo;
import CaC.Grupo2.FlySky.exception.NotFoundException;
import CaC.Grupo2.FlySky.exception.IllegalArgumentException;
import CaC.Grupo2.FlySky.repository.AsientoRepository;
import CaC.Grupo2.FlySky.repository.FlyRepository;
import CaC.Grupo2.FlySky.repository.PagoRepository;
import CaC.Grupo2.FlySky.repository.ReservaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
//@Qualifier("flyService")
public class FlyService implements IFlyService{

    FlyRepository flyRepository;
    ReservaRepository reservaRepository;
    AsientoRepository asientoRepository;
    PagoRepository pagoRepository;

    public FlyService( FlyRepository flyRepository, ReservaRepository reservaRepository,AsientoRepository asientoRepository,PagoRepository pagoRepository) {
        this.flyRepository = flyRepository;
        this.reservaRepository = reservaRepository;
        this.asientoRepository = asientoRepository;
        this.pagoRepository =pagoRepository;
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
        ModelMapper modelMapper = new ModelMapper();
        Date fechaActual = new Date();
        Usuario usuario = modelMapper.map(reservaDto.getUsuario(), Usuario.class);
        Vuelo vueloExistente = flyRepository.findById(reservaDto.getVuelo().getVueloID())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el vuelo con el ID especificado"));
        
        List<Long> asientos_solicitados
                = asientos_solicitados_en(reservaDto);
        
        List<Asiento> asientos_del_vuelo_actual
                = vueloExistente.recuperarLosAsientosSegunLosIds(
                        asientos_solicitados_en(reservaDto)
        );

        /*
        for( Asiento asiento_del_vuelo : asientos_del_vuelo_actual ) {
            if( asiento_del_vuelo.isOcupado() )
                throw new IllegalArgumentException(
                        String.format("El asiento %s está ocupado", asiento_del_vuelo.getNombreAsiento())
                        );
            
            Long id_actual = asiento_del_vuelo.getAsientoID();
            Optional<Asiento> x = asientos_solicitados
                    .stream().filter( a -> Objects.equals(a.getAsientoID(), id_actual))
                    .findAny();
            if( x.isPresent() ) {
                asiento_del_vuelo.setPasajero(x.get().getPasajero());
                asiento_del_vuelo.setOcupado( true );
            }
        }

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
         */


        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setFechaReserva(fechaActual);
        reserva.setAsientos( asientos_del_vuelo_actual );
        Reserva persistReserva = reservaRepository.save(reserva);

        RespReservaDto resp = new RespReservaDto();

        SimpleDateFormat fechaHora= new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String fechaHoraActual=fechaHora.format(persistReserva.getFechaReserva());

        ReservaDto reservaDto1= new ReservaDto();

        reservaDto1.setNumeroReserva(persistReserva.getReservaID());
        reservaDto1.setUsuario(modelMapper.map(persistReserva.getUsuario(), UsuarioDto.class));
        //reservaDto1.setVuelo(modelMapper.map(persistReserva.getVuelo(), VueloDto.class));
        
        reservaDto1.setFechaReserva(fechaHoraActual);

        resp.setReserva(reservaDto1);
        resp.setMensaje("Su reserva se realizó con éxito...");
        return resp;
    }
    
    private static List<Long> asientos_solicitados_en(ReservaDto reservaDto ) {
        ModelMapper modelMapper = new ModelMapper();
        List<Asiento> asientos = reservaDto.getVuelo()
                .getAsientos().stream()
                .map(asientoDto -> modelMapper.map(asientoDto, Asiento.class))
                .collect(Collectors.toList());
        return asientos.stream().map(Asiento::getAsientoID).collect(Collectors.toList());
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
