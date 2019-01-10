package mx.com.admoninmuebles.service;

import java.io.BufferedReader;

import mx.com.admoninmuebles.dto.CargaSocioDto;

public interface CargaSocioService {
	
	CargaSocioDto validaCSVSocios(BufferedReader br);

}
