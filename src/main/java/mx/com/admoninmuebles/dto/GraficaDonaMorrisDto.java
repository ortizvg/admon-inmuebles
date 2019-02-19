package mx.com.admoninmuebles.dto;

import java.util.List;

import lombok.Data;

@Data
public class GraficaDonaMorrisDto {
	
	private String element;
	private List<GraficaDonaMorrisDataDto> data;
	private boolean resize;
	private List<String> colors;


}
