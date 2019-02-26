package mx.com.admoninmuebles.dto;

import lombok.Data;

@Data
public class ErrorDto {

	public ErrorDto() {
	}
	
	private long id;
	private String message;

}
