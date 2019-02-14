package mx.com.admoninmuebles.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ReservacionDto {
    private Long id;
    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate start;
    
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
//    private LocalDateTime start;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate end;
    
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm",iso = DateTimeFormat.ISO.DATE_TIME)
//    private LocalDateTime end;

    private Long areaComunId;
    private String areaComunNombre;

    private Long socioId;
    
    private Long pagoId;
    private String pagoEstatusPagoName;
    private String pagoEstatusPagoDescripction;
}
