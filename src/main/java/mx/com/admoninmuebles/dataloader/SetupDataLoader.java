package mx.com.admoninmuebles.dataloader;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import mx.com.admoninmuebles.persistence.model.Privilegio;
import mx.com.admoninmuebles.persistence.model.Reservacion;
import mx.com.admoninmuebles.persistence.model.Rol;
import mx.com.admoninmuebles.persistence.model.Ticket;
import mx.com.admoninmuebles.persistence.model.TipoAsentamiento;
import mx.com.admoninmuebles.persistence.model.Usuario;
import mx.com.admoninmuebles.persistence.model.Zona;
import mx.com.admoninmuebles.persistence.repository.AreaComunRepository;
import mx.com.admoninmuebles.persistence.repository.AreaServicioRepository;
import mx.com.admoninmuebles.persistence.repository.AsentamientoRepository;
import mx.com.admoninmuebles.persistence.repository.DatosAdicionalesRepository;
import mx.com.admoninmuebles.persistence.repository.DireccionRepository;
import mx.com.admoninmuebles.persistence.repository.InmuebleRepository;
import mx.com.admoninmuebles.persistence.repository.PrivilegioRepository;
import mx.com.admoninmuebles.persistence.repository.ReservacionRepository;
import mx.com.admoninmuebles.persistence.repository.RolRepository;
import mx.com.admoninmuebles.persistence.repository.TicketRepository;
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
    private PasswordEncoder passwordEncoder;

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
        Privilegio gestionarRepBi = createPrivilegioIfNotFound(PrivilegioConst.GESTIONAR_REP_BI);
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
        Rol socioBi = createRolIfNotFound(RolConst.ROLE_SOCIO_BI, "Socio", privilegiosSocioBi);

        List<Privilegio> privilegiosRepBi = new ArrayList<>();
        privilegiosRepBi.add(historialPagoInmuble);
        privilegiosRepBi.add(estadoFinancieroInmueble);
        privilegiosRepBi.add(listaSocios);
        privilegiosRepBi.add(reporteMorosos);
        privilegiosSocioBi.addAll(privilegiosRepBi);
        Rol repBi = createRolIfNotFound(RolConst.ROLE_REP_BI, "Representante de bien inmueble",  privilegiosSocioBi);

        List<Privilegio> privilegiosAdminBi = new ArrayList<>();
        privilegiosAdminBi.addAll(privilegiosRepBi);
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
        privilegiosAdminBi.add(gestionarRepBi);
        privilegiosAdminBi.add(gestionarAdminBi);
        privilegiosAdminBi.add(gestionarProveedor);
        privilegiosAdminBi.add(estadoFinancieroColonia);
        Rol adminBi = createRolIfNotFound(RolConst.ROLE_ADMIN_BI, "Administrador de bien inmueble", privilegiosAdminBi);

        List<Privilegio> privilegiosAdminZona = new ArrayList<>();
        privilegiosAdminZona.addAll(privilegiosAdminBi);
        privilegiosAdminZona.add(estadoFinancieroZona);
        Rol adminZona = createRolIfNotFound(RolConst.ROLE_ADMIN_ZONA, "Administrador de zona", privilegiosAdminZona);

        List<Privilegio> privilegiosAdminCorp = new ArrayList<>();
        privilegiosAdminCorp.addAll(privilegiosAdminZona);
        privilegiosAdminCorp.add(gestionarZona);
        privilegiosAdminCorp.add(gestionarAdminZona);
        privilegiosAdminCorp.add(gestionarAdminCorp);
        privilegiosAdminCorp.add(reportes);
        Rol adminCorp = createRolIfNotFound(RolConst.ROLE_ADMIN_CORP, "Administrador corporativo", privilegiosAdminCorp);

        Usuario usuarioProveedorJardineria = createUsuarioIfNotFound("proveedor_jardineria", "Proveedor", "Jardineria", "", "proveedor", new ArrayList<>(Arrays.asList(proveedor)), "correo@gmail.com");
        Usuario usuarioProveedorLimpieza = createUsuarioIfNotFound("proveedor_limpieza", "Proveedor", "Limpieza", "", "proveedor", new ArrayList<>(Arrays.asList(proveedor)), "correo@gmail.com");
        Usuario usuarioProveedorConstruccion = createUsuarioIfNotFound("proveedor_construccion", "Proveedor", "Construccion", "", "proveedor", new ArrayList<>(Arrays.asList(proveedor)), "correo@gmail.com");
        Usuario usuarioSocioBi = createUsuarioIfNotFound("socio_bi", "Socio", "Bi", "Inmueble", "socio_bi", new ArrayList<>(Arrays.asList(socioBi)), "correo@gmail.com");
        createUsuarioIfNotFound("rep_bi", "Representante", "Bien", "Inmubele", "rep_bi", new ArrayList<>(Arrays.asList(repBi)), "correo@gmail.com");
        Usuario usuarioAdminBi = createUsuarioIfNotFound("admin_bi", "Administrador", "Bien", "Inmueble", "admin_bi", new ArrayList<>(Arrays.asList(adminBi)), "correo@gmail.com");
        Usuario usuarioAdminB2 = createUsuarioIfNotFound("admin_bi2", "Administrador2", "Bien", "Inmueble", "admin_bi2", new ArrayList<>(Arrays.asList(adminBi)), "correo@gmail.com");
        Usuario usuarioAdminZona = createUsuarioIfNotFound("admin_zona", "Administrador", "Zona", "", "admin_zona", new ArrayList<>(Arrays.asList(adminZona)), "correo@gmail.com");
        Usuario usuarioAdminZona2 = createUsuarioIfNotFound("admin_zona2", "Administrador2", "Zona", "", "admin_zona2", new ArrayList<>(Arrays.asList(adminZona)), "correo@gmail.com");
        createUsuarioIfNotFound("admin_corp", "Administrador", "Corporativo", "", "admin_corp", new ArrayList<>(Arrays.asList(adminCorp)), "correo@gmail.com");

        Zona zona = createZonaIfNotFound("zona1", "Zona 1", usuarioAdminZona, usuarioAdminBi);
        createZonaIfNotFound("zona2", "CDMX", usuarioAdminZona, null);
        createZonaIfNotFound("zona3", "Aguascalientes", usuarioAdminZona2, usuarioAdminB2);
        createZonaIfNotFound("zona4", "Querétaro", usuarioAdminZona2, null);
        createZonaIfNotFound("zona5", "Cancún", usuarioAdminZona2, null);
        Asentamiento asentamiento = updateAsentamientoIfFound(1L, zona);

        Inmueble inmueble = createInmuebleIfNotFound(1L, "Inmueble", asentamiento, usuarioAdminBi, usuarioSocioBi);

        AreaComun areaComun = createAreaComunIfNotFound(1L, "Area comun 1", "Area para 30 personas", inmueble);

        createReservacionIfNotFound(1L, "Fiesta Pablito", areaComun, usuarioSocioBi);

        AreaServicio areaServicioJardineria = createAreaServicioIfNotFound(1L, "Jardineria", usuarioProveedorJardineria);
        createAreaServicioIfNotFound(2L, "Limpieza", usuarioProveedorLimpieza);
        createAreaServicioIfNotFound(3L, "Construcción", usuarioProveedorConstruccion);
        createTicketIfNotFound(1L, "Podar cesped", "Quiero que poden el ceped de mi casa.", areaServicioJardineria, usuarioSocioBi, usuarioProveedorJardineria, EstatusTicketConst.ASIGNADO);

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
            final Collection<Rol> roles, final String correo) {
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
    public final Inmueble createInmuebleIfNotFound(final Long id, final String nombre, final Asentamiento asentamiento, final Usuario adminBi, final Usuario socio) {
        Optional<Inmueble> optInmueble = inmuebleRepository.findById(id);
        Inmueble inmueble = optInmueble.orElse(new Inmueble());
        if (!optInmueble.isPresent()) {
            inmueble.setNombre(nombre);
            inmueble.setDiaCuotaOrdinaria(11);
            inmueble.setMontoCuotaOrdinaria(new BigDecimal("11.11"));
            inmueble.setAdminBi(adminBi);
            inmueble.setImagenUrl("/files/inmueble.jpg");

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
            datosAdicionales.setNumeroCuenta("numeroCuenta");
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
            reservacion.setStart(LocalDate.now());
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
    public final Ticket createTicketIfNotFound(final Long id, final String titulo, final String descripcion, final AreaServicio areaServicio, final Usuario usuarioCreador, final Usuario usuarioAsignado,
            final String estatus) {
        Optional<Ticket> optTicket = ticketRepository.findById(id);
        Ticket ticket = optTicket.orElse(new Ticket());
        if (!optTicket.isPresent()) {
            ticket.setTitulo(titulo);
            ticket.setDescripcion(descripcion);
            ticket.setEstatus(estatus);
            ticket.setAreaServicio(areaServicio);
            ticket.setUsuarioCreador(usuarioCreador);
            ticket.setUsuarioAsignado(usuarioAsignado);
            ticket = ticketRepository.save(ticket);
        }
        return ticket;
    }
}