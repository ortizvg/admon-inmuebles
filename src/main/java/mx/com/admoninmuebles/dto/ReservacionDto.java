package mx.com.admoninmuebles.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import mx.com.admoninmuebles.persistence.model.Pago;

@Data
public class ReservacionDto {
    private Long id;
    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate start;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate end;

    private Long areaComunId;
    private String areaComunNombre;

    private Long socioId;
    
    private Long pagoId;
    private String pagoEstatusPagoName;
    private String pagoEstatusPagoDescripction;
}
