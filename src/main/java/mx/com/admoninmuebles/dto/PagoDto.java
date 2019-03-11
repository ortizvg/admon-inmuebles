package mx.com.admoninmuebles.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.ToString;
import mx.com.admoninmuebles.constant.ComunConst;

@Data
@ToString(includeFieldNames=true)
public class PagoDto {

	private Long id;

	private String numeroTransaccion;

	@Size(min = 0, max = 50)
	private String referencia;

	@Size(min = 0, max = 50)
	private String numeroCuenta;

	@Digits(integer = 7, fraction = 2)
	private BigDecimal monto;

	@Size(min = 0, max = 100)
	private String concepto;

	@Size(min = 0, max = 100)
	private String comprobantePagoUrl;

	private Boolean verificado;
	
    @DateTimeFormat(iso = ISO.NONE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date fechaPago;
    
    @DateTimeFormat(iso = ISO.NONE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date fechaCreacion;
    
    @JsonFormat(pattern="dd-MM-yyyy hh:mm:ss")
    private Date fechaVerificacion;

	private Long tipoPagoBancarioId;
	private String tipoPagoBancarioDescripction;

	private Long tipoPagoId;
	private String tipoPagoName;
	private String tipoPagoDescripction;

	private Long estatusPagoId;
	private String estatusPagoName;
	private String estatusPagoDescripction;

	private Long usuarioId;
	private String usuarioNombre;
	private String usuarioApellidoPaterno;
	private String usuarioApellidoMaterno;
	private String usuarioCorreo;
	
	private Long usuarioGeneradorId;
	private String usuarioGeneradorNombre;
	private String usuarioGeneradorApellidoPaterno;
	private String usuarioGeneradorApellidoMaterno;
	private String usuarioGeneradorusuarioCorreo;
	
	private Long usuarioVerificadorId;
	private String usuarioVerificadorNombre;
	private String usuarioVerificadorApellidoPaterno;
	private String usuarioVerificadorusuarioGeneradorApellidoMaterno;
	private String usuarioVerificadorusuarioCorreo;
	
//	private byte[] comprobantePago;
    private MultipartFile comprobantePagoMf;
    
    private Long inmuebleId;
    
    private String comprobantePagoId;
    private String comprobantePagoNombre;
	
	public String getSocio() {
		return this.usuarioNombre + " " + this.usuarioApellidoPaterno + " " + this.usuarioApellidoMaterno;
	}
	
	public String getUsuarioVerificador() {
		if(this.usuarioVerificadorId == null ) {
			return ComunConst.CADENA_VACIA;
		}
		StringBuffer usuarioVerificador = new StringBuffer()
		.append( this.usuarioVerificadorNombre )
		.append( ComunConst.CADENA_ESPACIO )
		.append( this.usuarioVerificadorApellidoPaterno )
		.append( ComunConst.CADENA_ESPACIO )
		.append( this.usuarioVerificadorusuarioGeneradorApellidoMaterno == null ? ComunConst.CADENA_VACIA : this.usuarioVerificadorusuarioGeneradorApellidoMaterno );
		
		return usuarioVerificador.toString();
	}
	
	public String getUsuarioGenerador() {
		if(this.usuarioGeneradorId == null ) {
			return ComunConst.USUARIO_SISTEMA;
		}
		StringBuffer usuarioGenerador = new StringBuffer()
			.append( this.usuarioGeneradorNombre )
			.append( ComunConst.CADENA_ESPACIO )
			.append( this.usuarioGeneradorApellidoPaterno )
			.append( ComunConst.CADENA_ESPACIO )
			.append( this.usuarioGeneradorApellidoMaterno == null ? ComunConst.CADENA_VACIA : this.usuarioGeneradorApellidoMaterno );
		
		return usuarioGenerador.toString();
	}

}
