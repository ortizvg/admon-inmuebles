package mx.com.admoninmuebles.service;

import java.io.BufferedReader;
import java.util.List;

import mx.com.admoninmuebles.dto.CargaSocioDto;
import mx.com.admoninmuebles.dto.UsuarioDto;

public interface CargaSocioService {
	
	CargaSocioDto validaCSVSocios(BufferedReader br);
	
	void enviarCorreoMasivo(List<UsuarioDto> listaSocios, final String urlContext);

}
