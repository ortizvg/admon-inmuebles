package mx.com.admoninmuebles.dto;

import lombok.Data;

@Data
public class ErrorDto {

	public ErrorDto() {
	}
	
	private int id;
	private String message;

}
