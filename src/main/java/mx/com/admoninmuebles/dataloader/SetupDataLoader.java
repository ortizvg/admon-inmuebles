package mx.com.admoninmuebles.dataloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import mx.com.admoninmuebles.constant.EstatusTicketConst;
import mx.com.admoninmuebles.constant.PrivilegioConst;
import mx.com.admoninmuebles.constant.RolConst;
import mx.com.admoninmuebles.persistence.model.AreaComun;
import mx.com.admoninmuebles.persistence.model.AreaServicio;
import mx.com.admoninmuebles.persistence.model.Asentamiento;
import mx.com.admoninmuebles.persistence.model.DatosAdicionales;
import mx.com.admoninmuebles.persistence.model.Direccion;
import mx.com.admoninmuebles.persistence.model.Inmueble;
import mx.com.admoninmuebles.persistence.model.Municipio;
import mx.com.admoninmuebles.persistence.model.Pago;
import mx.com.admoninmuebles.persistence.model.Privilegio;
import mx.com.admoninmuebles.persistence.model.Reservacion;
import mx.com.admoninmuebles.persistence.model.Rol;
import mx.com.admoninmuebles.persistence.model.Ticket;
import mx.com.admoninmuebles.persistence.model.TipoAsentamiento;
import mx.com.admoninmuebles.persistence.model.TipoPago;
import mx.com.admoninmuebles.persistence.model.TipoSocio;
import mx.com.admoninmuebles.persistence.model.TipoTicket;
import mx.com.admoninmuebles.persistence.model.Usuario;
import mx.com.admoninmuebles.persistence.model.Zona;
import mx.com.admoninmuebles.persistence.repository.AreaComunRepository;
import mx.com.admoninmuebles.persistence.repository.AreaServicioRepository;
import mx.com.admoninmuebles.persistence.repository.AsentamientoRepository;
import mx.com.admoninmuebles.persistence.repository.DatosAdicionalesRepository;
import mx.com.admoninmuebles.persistence.repository.DireccionRepository;
import mx.com.admoninmuebles.persistence.repository.InmuebleRepository;
import mx.com.admoninmuebles.persistence.repository.PagoRepository;
import mx.com.admoninmuebles.persistence.repository.PrivilegioRepository;
import mx.com.admoninmuebles.persistence.repository.ReservacionRepository;
import mx.com.admoninmuebles.persistence.repository.RolRepository;
import mx.com.admoninmuebles.persistence.repository.TicketRepository;
import mx.com.admoninmuebles.persistence.repository.TipoSocioRepository;
import mx.com.admoninmuebles.persistence.repository.TipoTicketRepository;
import mx.com.admoninmuebles.persistence.repository.UsuarioRepository;
import mx.com.admoninmuebles.persistence.repository.ZonaRepository;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PrivilegioRepository privilegioRepository;

    @Autowired
    private ZonaRepository zonaRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private DatosAdicionalesRepository datosAdicionalesRepository;

    @Autowired
    private InmuebleRepository inmuebleRepository;

    @Autowired
    private AsentamientoRepository asentamientoRepository;

    @Autowired
    private AreaServicioRepository areaServicioRepository;

    @Autowired
    private AreaComunRepository areaComunRepository;

    @Autowired
    private ReservacionRepository reservacionRepository;

    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private TipoTicketRepository tipoTicketRepository;
    
    @Autowired
    private TipoSocioRepository tipoSocioRepository;

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        Privilegio tablero = createPrivilegioIfNotFound(PrivilegioConst.TABLERO);
        Privilegio notificarPago = createPrivilegioIfNotFound(PrivilegioConst.NOTIFICAR_PAGO);
        Privilegio historialPagos = createPrivilegioIfNotFound(PrivilegioConst.HISTORIAL_PAGOS);
        Privilegio historialPagoInmuble = createPrivilegioIfNotFound(PrivilegioConst.HISTORIAL_PAGOS_INMUEBLE);
        Privilegio verificarPago = createPrivilegioIfNotFound(PrivilegioConst.VERIFICAR_PAGO);
        Privilegio abrirTicket = createPrivilegioIfNotFound(PrivilegioConst.ABRIR_TICKET);
        Privilegio verTicket = createPrivilegioIfNotFound(PrivilegioConst.VER_TICKET);
        Privilegio asignarTicket = createPrivilegioIfNotFound(PrivilegioConst.ASIGNAR_TICKET);
        Privilegio aceptarTicket = createPrivilegioIfNotFound(PrivilegioConst.ACEPTAR_TICKET);
        Privilegio atenderTicket = createPrivilegioIfNotFound(PrivilegioConst.ATENDER_TICKET);
        Privilegio rechazarTicket = createPrivilegioIfNotFound(PrivilegioConst.RECHAZAR_TICKET);
        Privilegio cerrarTicket = createPrivilegioIfNotFound(PrivilegioConst.CERRAR_TICKET);
        Privilegio cancelarTicket = createPrivilegioIfNotFound(PrivilegioConst.CANCELAR_TICKET);
        Privilegio listaSocios = createPrivilegioIfNotFound(PrivilegioConst.LISTA_SOCIOS);
        Privilegio estadoFinancieroInmueble = createPrivilegioIfNotFound(PrivilegioConst.ESTADO_FINANCIERO_INMUEBLE);
        Privilegio estadoFinancieroColonia = createPrivilegioIfNotFound(PrivilegioConst.ESTADO_FINANCIERO_COLONIA);
        Privilegio estadoFinancieroZona = createPrivilegioIfNotFound(PrivilegioConst.ESTADO_FINANCIERO_ZONA);
        Privilegio gestionarColonia = createPrivilegioIfNotFound(PrivilegioConst.GESTIONAR_COLONIA);
        Privilegio gestionarZona = createPrivilegioIfNotFound(PrivilegioConst.GESTIONAR_ZONA);
        Privilegio gestionarBienesInmubeles = createPrivilegioIfNotFound(PrivilegioConst.GESTIONAR_INMUEBLES);
        Privilegio gestionarServicios = createPrivilegioIfNotFound(PrivilegioConst.GESTIONAR_SERVICIOS);
        Privilegio gestionarPreguntas = createPrivilegioIfNotFound(PrivilegioConst.GESTIONAR_PREGUNTAS);
        Privilegio gestionarSocioBi = createPrivilegioIfNotFound(PrivilegioConst.GESTIONAR_SOCIO_BI);
        Privilegio gestionarAdminBi = createPrivilegioIfNotFound(PrivilegioConst.GESTIONAR_ADMIN_BI);
        Privilegio gestionarAdminZona = createPrivilegioIfNotFound(PrivilegioConst.GESTIONAR_ADMIN_ZONA);
        Privilegio gestionarProveedor = createPrivilegioIfNotFound(PrivilegioConst.GESTIONAR_PROVEEDOR);
        Privilegio gestionarAdminCorp = createPrivilegioIfNotFound(PrivilegioConst.GESTIONAR_ADMIN_CORP);
        Privilegio reportes = createPrivilegioIfNotFound(PrivilegioConst.REPORTES);
        Privilegio reporteMorosos = createPrivilegioIfNotFound(PrivilegioConst.REPORTE_MOROSOS);

        List<Privilegio> privilegiosProveedor = new ArrayList<>();
        privilegiosProveedor.add(aceptarTicket);
        privilegiosProveedor.add(verTicket);
        privilegiosProveedor.add(atenderTicket);
        privilegiosProveedor.add(rechazarTicket);
        Rol proveedor = createRolIfNotFound(RolConst.ROLE_PROVEEDOR, "Proveedor", privilegiosProveedor);

        List<Privilegio> privilegiosSocioBi = new ArrayList<>();
        privilegiosSocioBi.add(tablero);
        privilegiosSocioBi.add(notificarPago);
        privilegiosSocioBi.add(historialPagos);
        privilegiosSocioBi.add(verTicket);
        privilegiosSocioBi.add(abrirTicket);
        privilegiosSocioBi.add(cancelarTicket);
        Rol socioBi = createRolIfNotFound(RolConst.ROLE_SOCIO_BI, "Condómino", privilegiosSocioBi);


        List<Privilegio> privilegiosAdminBi = new ArrayList<>();
        privilegiosAdminBi.add(asignarTicket);
        privilegiosAdminBi.add(verTicket);
        privilegiosAdminBi.add(cerrarTicket);
        privilegiosAdminBi.add(cancelarTicket);
        privilegiosAdminBi.add(gestionarSocioBi);
        privilegiosAdminBi.add(verificarPago);
        privilegiosAdminBi.add(historialPagos);
        privilegiosAdminBi.add(gestionarColonia);
        privilegiosAdminBi.add(gestionarBienesInmubeles);
        privilegiosAdminBi.add(gestionarServicios);
        privilegiosAdminBi.add(gestionarPreguntas);
        privilegiosAdminBi.add(gestionarSocioBi);
        privilegiosAdminBi.add(gestionarAdminBi);
        privilegiosAdminBi.add(gestionarProveedor);
        privilegiosAdminBi.add(estadoFinancieroColonia);
        Rol adminBi = createRolIfNotFound(RolConst.ROLE_ADMIN_BI, "Administrador de bien inmueble", privilegiosAdminBi);

        List<Privilegio> privilegiosAdminZona = new ArrayList<>();
        privilegiosAdminZona.addAll(privilegiosAdminBi);
        privilegiosAdminZona.add(estadoFinancieroZona);
        Rol adminZona = createRolIfNotFound(RolConst.ROLE_ADMIN_ZONA, "Director de área", privilegiosAdminZona);

        List<Privilegio> privilegiosAdminCorp = new ArrayList<>();
        privilegiosAdminCorp.addAll(privilegiosAdminZona);
        privilegiosAdminCorp.add(gestionarZona);
        privilegiosAdminCorp.add(gestionarAdminZona);
        privilegiosAdminCorp.add(gestionarAdminCorp);
        privilegiosAdminCorp.add(reportes);
        Rol adminCorp = createRolIfNotFound(RolConst.ROLE_ADMIN_CORP, "Director corporativo", privilegiosAdminCorp);
        
        List<Privilegio> privilegiosContador = new ArrayList<>();
        privilegiosAdminCorp.add(notificarPago);
        privilegiosAdminCorp.add(historialPagos);
        privilegiosAdminCorp.add(historialPagoInmuble);
        privilegiosAdminCorp.add(verificarPago);
        Rol contador = createRolIfNotFound(RolConst.ROLE_CONTADOR, "Contador", privilegiosContador);
        
        TipoSocio condominoEs = createTipoSocioIfNotFound(1l, "CONDOMINO", "Condómino (propietario)", "es");
        createTipoSocioIfNotFound(2l, "RESIDENTE", "Residente (Arrendatario)", "es");
        createTipoSocioIfNotFound(3l, "CONDOMINO", "Condominium (owner)", "en");
        createTipoSocioIfNotFound(4l, "RESIDENTE", "Resident (Tenant)", "en");

        Usuario usuarioProveedorJardineria = createUsuarioIfNotFound("proveedor_jardineria", "Proveedor", "Jardineria", "", "proveedor", new ArrayList<>(Arrays.asList(proveedor)), "correo@gmail.com", null);
        Usuario usuarioProveedorLimpieza = createUsuarioIfNotFound("proveedor_limpieza", "Proveedor", "Limpieza", "", "proveedor", new ArrayList<>(Arrays.asList(proveedor)), "correo@gmail.com", null);
        Usuario usuarioProveedorConstruccion = createUsuarioIfNotFound("proveedor_construccion", "Proveedor", "Construccion", "", "proveedor", new ArrayList<>(Arrays.asList(proveedor)), "correo@gmail.com", null);
        Usuario usuarioSocioBi = createUsuarioIfNotFound("socio_bi", "Socio", "Bi", "Inmueble", "socio_bi", new ArrayList<>(Arrays.asList(socioBi)), "correo@gmail.com", condominoEs);
        Usuario usuarioSocioBi2 = createUsuarioIfNotFound("socio_bi2", "Socio2", "Bi2", "Inmueble2", "socio_bi2", new ArrayList<>(Arrays.asList(socioBi)), "correo@gmail.com", condominoEs);
        Usuario usuarioAdminBi = createUsuarioIfNotFound("admin_bi", "Administrador", "Bien", "Inmueble", "admin_bi", new ArrayList<>(Arrays.asList(adminBi)), "correo@gmail.com", null);
        Usuario usuarioAdminB2 = createUsuarioIfNotFound("admin_bi2", "Administrador2", "Bien", "Inmueble", "admin_bi2", new ArrayList<>(Arrays.asList(adminBi)), "correo@gmail.com", null);
        Usuario usuarioAdminZona = createUsuarioIfNotFound("admin_zona", "Administrador", "Zona", "", "admin_zona", new ArrayList<>(Arrays.asList(adminZona)), "correo@gmail.com", null);
        Usuario usuarioAdminZona2 = createUsuarioIfNotFound("admin_zona2", "Administrador2", "Zona", "", "admin_zona2", new ArrayList<>(Arrays.asList(adminZona)), "correo@gmail.com", null);
        createUsuarioIfNotFound("admin_corp", "Administrador", "Corporativo", "", "admin_corp", new ArrayList<>(Arrays.asList(adminCorp)), "correo@gmail.com", null);
        Usuario contadorBI = createUsuarioIfNotFound("contador", "Contador", "Contador", "", "contador", new ArrayList<>(Arrays.asList(contador)), "correo@gmail.com", null);

        Zona zona = createZonaIfNotFound("zona1", "Zona 1", usuarioAdminZona, usuarioAdminBi);
        createZonaIfNotFound("zona2", "CDMX", usuarioAdminZona, null);
        createZonaIfNotFound("zona3", "Aguascalientes", usuarioAdminZona2, usuarioAdminB2);
        createZonaIfNotFound("zona4", "Querétaro", usuarioAdminZona2, null);
        createZonaIfNotFound("zona5", "Cancún", usuarioAdminZona2, null);
        Asentamiento asentamiento = updateAsentamientoIfFound(1L, zona);

        Inmueble inmueble = createInmuebleIfNotFound(1L, "Inmueble", asentamiento, usuarioAdminBi, usuarioSocioBi,  contadorBI);
        Inmueble inmueble2 = createInmuebleIfNotFound(2L, "Inmueble2", asentamiento, usuarioAdminBi, usuarioSocioBi2,  contadorBI);

        AreaComun areaComun = createAreaComunIfNotFound(1L, "Area comun 1", "Area para 30 personas", inmueble);

        createReservacionIfNotFound(1L, "Fiesta Pablito", areaComun, usuarioSocioBi);

        AreaServicio areaServicioJardineria = createAreaServicioIfNotFound(1L, "Jardineria", usuarioProveedorJardineria);
        createAreaServicioIfNotFound(2L, "Limpieza", usuarioProveedorLimpieza);
        createAreaServicioIfNotFound(3L, "Construcción", usuarioProveedorConstruccion);
        
        TipoTicket tipoTicketAdministrativo = createTipoTicketIfNotFound(1L, "Administrativo");
        TipoTicket tipoTicketSolServ = createTipoTicketIfNotFound(2L, "Solicitud de servicios");
        TipoTicket tipoTicketQuejas = createTipoTicketIfNotFound(3L, "Quejas y sugerencias");
        createTicketIfNotFound(1L, "Podar cesped", "Quiero que poden el ceped de mi casa.", usuarioSocioBi, usuarioProveedorJardineria, EstatusTicketConst.ABIERTO,tipoTicketSolServ);

        
//        createPagoIfNotFound(1L, "4444444", "234242sadads", BigDecimal.valueOf(24l), "Pago1", usuarioSocioBi, "asdsadsadsadasdasd", null);
//        createPagoIfNotFound(2L, "5555555", "dasd324243324", BigDecimal.valueOf(45l), "Pago2", usuarioSocioBi, "sadsadsad24324", null);
//        createPagoIfNotFound(3L, "6666666", "6666sdfsfs", BigDecimal.valueOf(100l), "Pago3", usuarioSocioBi2, "sdfsfs999999", null);
        
        alreadySetup = true;
    }

    @Transactional
    public final Privilegio createPrivilegioIfNotFound(final String nombre) {
        Optional<Privilegio> optPrivilegio = privilegioRepository.findByNombre(nombre);
        Privilegio privilegio = optPrivilegio.orElse(new Privilegio());
        if (!optPrivilegio.isPresent()) {
            privilegio.setNombre(nombre);
            privilegio = privilegioRepository.save(privilegio);
        }
        return privilegio;
    }

    @Transactional
    public final Rol createRolIfNotFound(final String nombre, final String descripcion, final Collection<Privilegio> privilegios) {
        Optional<Rol> optRol = rolRepository.findByNombre(nombre);
        Rol rol = optRol.orElse(new Rol());
        if (!optRol.isPresent()) {
            rol.setNombre(nombre);
            rol.setPrivilegios(privilegios);
            rol.setDescripcion(descripcion);
            rol = rolRepository.save(rol);
        }

        return rol;
    }

    @Transactional
    public final Usuario createUsuarioIfNotFound(final String username, final String firstNombre, final String apellidoPatarno, final String apellidoMaterno, final String contrasenia,
            final Collection<Rol> roles, final String correo, TipoSocio tipoSocio) {
        Optional<Usuario> optUsuario = usuarioRepository.findByUsername(username);
        Usuario usuario = optUsuario.orElse(new Usuario());
        if (!optUsuario.isPresent()) {
            usuario.setUsername(username);
            usuario.setNombre(firstNombre);
            usuario.setApellidoPaterno(apellidoPatarno);
            usuario.setApellidoMaterno(apellidoMaterno);
            usuario.setCorreo(correo);
            usuario.setContrasenia(passwordEncoder.encode(contrasenia));
            usuario.setRoles(roles);
            usuario.setReferenciaPagoSocio("123456");
            usuario.setCuentaPagoSocio("343242453556464");
            usuario.setCoutaMensualPagoSocio(BigDecimal.valueOf(100.29));
            usuario.setTipoSocio(tipoSocio);

            usuario = usuarioRepository.save(usuario);
        }
        return usuario;
    }

    @Transactional
    public final Zona createZonaIfNotFound(final String codigo, final String nombre, final Usuario adminZona, final Usuario usuarioAdminBi) {
        Optional<Zona> optZona = zonaRepository.findById(codigo);
        Zona zona = optZona.orElse(new Zona());
        if (!optZona.isPresent()) {
            zona.setCodigo(codigo);
            zona.setNombre(nombre);
            zona.setAdminZona(adminZona);
            zona.addAdminBi(usuarioAdminBi);
            zona = zonaRepository.save(zona);
        }
        return zona;
    }

    @Transactional
    public final Asentamiento updateAsentamientoIfFound(final Long id, final Zona zona) {
        Optional<Asentamiento> optAsentamiento = asentamientoRepository.findById(id);
        Asentamiento asentamiento = null;
        if (optAsentamiento.isPresent()) {
            asentamiento = optAsentamiento.get();
            asentamiento.setZona(zona);
            asentamientoRepository.save(asentamiento);
        } else {
            Municipio municpio = new Municipio();
            municpio.setId(9010l);
            TipoAsentamiento tipoAsentamiento = new TipoAsentamiento();
            tipoAsentamiento.setId(9l);
            asentamiento = new Asentamiento();
            asentamiento.setCodigoPostal("01000");
            asentamiento.setNombre("San Ángel");
            asentamiento.setMunicipio(municpio);
            asentamiento.setTipoAsentamiento(tipoAsentamiento);
            asentamiento.setZona(zona);
            asentamientoRepository.save(asentamiento);
        }
        return asentamiento;
    }

    @Transactional
    public final Inmueble createInmuebleIfNotFound(final Long id, final String nombre, final Asentamiento asentamiento, final Usuario adminBi, final Usuario socio, final Usuario contador) {
        Optional<Inmueble> optInmueble = inmuebleRepository.findById(id);
        Inmueble inmueble = optInmueble.orElse(new Inmueble());
        if (!optInmueble.isPresent()) {
            inmueble.setNombre(nombre);
            inmueble.setDiaCuotaOrdinaria(11);
            inmueble.setMontoCuotaOrdinaria(new BigDecimal("11.11"));
            inmueble.setAdminBi(adminBi);
            inmueble.setImagenUrl("/files/inmueble.jpg");
            inmueble.setContador(contador);

            Direccion direccion = new Direccion();
            direccion.setAsentamiento(asentamiento);
            direccion.setCalle("calle");
            direccion.setEntreCalles("entreCalles");
            direccion.setNumeroExterior("numeroExterior");
            direccion.setNumeroInterior("numeroInterior");
            direccion.setReferencias("referencias");
            inmueble.setDireccion(direccionRepository.save(direccion));

            DatosAdicionales datosAdicionales = new DatosAdicionales();
            datosAdicionales.setCorreo("correo");
            datosAdicionales.setNombreRepresentante("nombreRepresentante");
            datosAdicionales.setNumeroCuenta("1234567");
            datosAdicionales.setRazonSocial("razonSocial");
            datosAdicionales.setRfc("rfc");
            datosAdicionales.setTelefono("telefono");
            inmueble.setDatosAdicionales(datosAdicionalesRepository.save(datosAdicionales));

            inmueble.addSocio(socio);

            inmueble = inmuebleRepository.save(inmueble);
        }
        return inmueble;
    }

    @Transactional
    public final AreaComun createAreaComunIfNotFound(final Long id, final String nombre, final String descripcion, final Inmueble inmueble) {
        Optional<AreaComun> optAreaComun = areaComunRepository.findById(id);
        AreaComun areaComun = optAreaComun.orElse(new AreaComun());
        if (!optAreaComun.isPresent()) {
            areaComun.setNombre(nombre);
            areaComun.setDescripcion(descripcion);
            inmueble.addAreaComun(areaComun);
            areaComun = areaComunRepository.save(areaComun);
        }
        return areaComun;
    }

    @Transactional
    public final Reservacion createReservacionIfNotFound(final Long id, final String title, final AreaComun areaComun, final Usuario usuarioSocioBi) {
        Optional<Reservacion> optReservacion = reservacionRepository.findById(id);
        Reservacion reservacion = optReservacion.orElse(new Reservacion());
        if (!optReservacion.isPresent()) {
            reservacion.setTitle(title);
            reservacion.setStart(LocalDateTime.now());
            reservacion.setAreaComun(areaComun);
            reservacion.setSocio(usuarioSocioBi);
            reservacion = reservacionRepository.save(reservacion);
        }
        return reservacion;
    }

    @Transactional
    public final AreaServicio createAreaServicioIfNotFound(final Long id, final String nombre, final Usuario proveedor) {
        Optional<AreaServicio> optAreaServicio = areaServicioRepository.findById(id);
        AreaServicio areaServicio = optAreaServicio.orElse(new AreaServicio());
        if (!optAreaServicio.isPresent()) {
            areaServicio.setNombre(nombre);
            areaServicio.addProveedor(proveedor);
            areaServicio = areaServicioRepository.save(areaServicio);
            usuarioRepository.save(proveedor);
        }

        return areaServicio;
    }

    @Transactional
    public final Ticket createTicketIfNotFound(final Long id, final String titulo, final String descripcion, final Usuario usuarioCreador, final Usuario usuarioAsignado,
            final String estatus, final TipoTicket tipoTicket) {
        Optional<Ticket> optTicket = ticketRepository.findById(id);
        Ticket ticket = optTicket.orElse(new Ticket());
        if (!optTicket.isPresent()) {
            ticket.setFechaCreacion(LocalDate.now());
            ticket.setTitulo(obtenNombreArchivo());
            ticket.setDescripcion(descripcion);
            ticket.setEstatus(estatus);
            //ticket.setAreaServicio(areaServicio);
            ticket.setUsuarioCreador(usuarioCreador);
            ticket.setUsuarioAsignado(usuarioAsignado);
            ticket.setTipoTicket(tipoTicket);
            ticket.setArchivoEvidencia(obtenImagenBlob());
            ticket = ticketRepository.save(ticket);
        }
        return ticket;
    }
    
    @Transactional
    public final Pago createPagoIfNotFound(final Long id, final String numeroTransaccion, final String referencia, final BigDecimal monto, final String concepto, final Usuario socio,
            final String comprobantePagoUrl, TipoPago tipoPago) {
        Optional<Pago> optPago = pagoRepository.findById(id);
        Pago pago = optPago.orElse(new Pago());
        if (!optPago.isPresent()) {
        	pago.setConcepto(concepto);
        	pago.setMonto(monto);
        	pago.setNumeroTransaccion(numeroTransaccion);
        	pago.setReferencia(referencia);
        	pago.setTipoPago(tipoPago);
        	pago.setUsuario(socio);
        	pago.setVerificado(false);
        	pago = pagoRepository.save(pago);
        }
        return pago;
    }

    private String obtenNombreArchivo() {
    	File file = new File("C:\\Users\\infoworks\\Pictures\\armandito.jpg");
		return file.getName();
	}

	@Transactional
    public final TipoTicket createTipoTicketIfNotFound(final Long id, final String nombre) {
        Optional<TipoTicket> optTipoTicket = tipoTicketRepository.findById(id);
        TipoTicket tipoTicket = optTipoTicket.orElse(new TipoTicket());
        if (!optTipoTicket.isPresent()) {
        	tipoTicket.setId(id);
        	tipoTicket.setNombre(nombre);
        	tipoTicket = tipoTicketRepository.save(tipoTicket);
        }
        return tipoTicket;
    }
	
	@Transactional
    public final TipoSocio createTipoSocioIfNotFound(final Long id, final String nombre, final String descripcion, final String lang) {
        Optional<TipoSocio> optTipoSocio = tipoSocioRepository.findById(id);
        TipoSocio tipoSocio = optTipoSocio.orElse(new TipoSocio());
        if (!optTipoSocio.isPresent()) {
        	tipoSocio.setId(id);
        	tipoSocio.setName(nombre);
        	tipoSocio.setDescripction(descripcion);
        	tipoSocio.setLang(lang);
        	tipoSocio = tipoSocioRepository.save(tipoSocio);
        }
        return tipoSocio;
    }
    
	private static byte[] obtenImagenBlob() {
		
		File file = new File("C:\\Users\\infoworks\\Pictures\\armandito.jpg");
		byte[] blob = new byte[(int) file.length()];
		try {
			FileInputStream input = new FileInputStream(file);
			input.read(blob);
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return blob;
	}
}